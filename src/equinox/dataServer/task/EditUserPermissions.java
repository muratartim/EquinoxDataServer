/*
 * Copyright 2018 Murat Artim (muratartim@gmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package equinox.dataServer.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.EditUserPermissionsRequest;
import equinox.dataServer.remote.message.EditUserPermissionsResponse;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.Permission;

/**
 * Class for edit user permissions task.
 *
 * @author Murat Artim
 * @date 6 Apr 2018
 * @time 13:28:40
 */
public class EditUserPermissions extends DatabaseQueryTask {

	/**
	 * Creates edit user permissions task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public EditUserPermissions(DataServer server, DataClient client, EditUserPermissionsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for editing user permissions.");

		// create response message
		EditUserPermissionsResponse response = new EditUserPermissionsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		EditUserPermissionsRequest request = (EditUserPermissionsRequest) request_;

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// check inputs
				checkInputs(request);

				// get user id
				long userId = getUserId(connection, request);

				// delete user permissions
				deleteUserPermissions(userId, connection);

				// insert new permissions
				String sql = "insert into permissions(user_id, name, is_admin) values (" + userId + ", ?, ?)";
				try (PreparedStatement insertPermissions = connection.prepareStatement(sql)) {

					// loop over permissions
					for (Permission p : request.getPermissions()) {
						insertPermissions.setString(1, p.toString());
						insertPermissions.setBoolean(2, p.isAdminPermission());
						insertPermissions.executeUpdate();
					}
				}

				// commit updates
				connection.commit();
				connection.setAutoCommit(true);
			}

			// exception occurred during process
			catch (Exception e) {

				// roll back updates
				if (connection != null) {
					connection.rollback();
					connection.setAutoCommit(true);
				}

				// propagate exception
				throw e;
			}
		}

		// set updated
		response.setEdited(true);

		// respond to client
		client_.sendMessage(response);
	}

	/**
	 * Deletes user permissions.
	 *
	 * @param userId
	 *            User id.
	 * @param connection
	 *            Database connection.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void deleteUserPermissions(long userId, Connection connection) throws Exception {

		// create statement
		try (Statement statement = connection.createStatement()) {

			// build sql
			String sql = "delete from permissions where user_id = " + userId;

			// execute
			statement.executeUpdate(sql);
		}
	}

	/**
	 * Retrieves and returns user id.
	 *
	 * @param connection
	 *            Database connection.
	 * @param request
	 *            Request message.
	 * @return User id.
	 * @throws Exception
	 *             if user id cannot be retrieved or user is an administrator.
	 */
	private static long getUserId(Connection connection, EditUserPermissionsRequest request) throws Exception {

		// initialize variables
		long userId = -1;

		// create statement
		try (Statement statement = connection.createStatement()) {

			// get user id
			String sql = "select id from users where alias = '" + request.getAlias() + "'";
			try (ResultSet resultSet = statement.executeQuery(sql)) {
				while (resultSet.next()) {
					userId = resultSet.getLong("id");
				}
			}

			// user could not be found
			if (userId == -1)
				throw new Exception("No user found with alias '" + request.getAlias() + "'. User permissions cannot be edited.");

			// check if user is admin
			sql = "select id from admins where user_id = " + userId;
			try (ResultSet resultSet = statement.executeQuery(sql)) {
				if (resultSet.next())
					throw new Exception("Administrator permissions cannot be edited with this operation. Operation aborted.");
			}
		}

		// return user id
		return userId;
	}

	/**
	 * Checks inputs.
	 *
	 * @param request
	 *            Request message.
	 * @throws Exception
	 *             If check fails.
	 */
	private static void checkInputs(EditUserPermissionsRequest request) throws Exception {

		// alias
		String alias = request.getAlias();
		if (alias == null || alias.trim().isEmpty())
			throw new Exception("Invalid user alias supplied for editing user permissions.");

		// permissions
		Permission[] permissions = request.getPermissions();
		if (permissions == null || permissions.length == 0)
			throw new Exception("Invalid user permissions supplied for editing user permissions.");
	}
}
