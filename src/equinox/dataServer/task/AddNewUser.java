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
import equinox.dataServer.remote.message.AddNewUserRequest;
import equinox.dataServer.remote.message.AddNewUserResponse;
import equinox.dataServer.server.DataServer;
import equinox.dataServer.utility.Utility;
import equinox.serverUtilities.Permission;

/**
 * Class for add new user task.
 *
 * @author Murat Artim
 * @date 4 Apr 2018
 * @time 16:46:29
 */
public class AddNewUser extends DatabaseQueryTask {

	/**
	 * Creates add new user task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public AddNewUser(DataServer server, DataClient client, AddNewUserRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for adding new user.");

		// create response message
		AddNewUserResponse response = new AddNewUserResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		AddNewUserRequest request = (AddNewUserRequest) request_;

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// check inputs
				checkInputs(request);

				// prepare statement for inserting user
				long userId = -1;
				String sql = "insert into users(alias, username, organization, email, image_url) values(?, ?, ?, ?, ?)";
				try (PreparedStatement insertUser = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

					// set parameters and execute update
					insertUser.setString(1, request.getAlias());
					insertUser.setString(2, request.getName());
					insertUser.setString(3, request.getOrganization());
					insertUser.setString(4, request.getEmail());
					if (request.getImageUrl() == null) {
						insertUser.setNull(5, java.sql.Types.VARCHAR);
					}
					else {
						insertUser.setString(5, request.getImageUrl());
					}
					insertUser.executeUpdate();

					// get wish ID
					try (ResultSet resultSet = insertUser.getGeneratedKeys()) {
						if (resultSet.next()) {
							userId = resultSet.getBigDecimal(1).longValue();
						}
					}
				}

				// user couldn't be created
				if (userId == -1)
					throw new Exception("User couldn't be created.");

				// insert permissions
				sql = "insert into permissions(user_id, name, is_admin) values (" + userId + ", ?, ?)";
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
		response.setAdded(true);

		// respond to client
		client_.sendMessage(response);
	}

	/**
	 * Checks inputs.
	 *
	 * @param request
	 *            Request message.
	 * @throws Exception
	 *             If check fails.
	 */
	private static void checkInputs(AddNewUserRequest request) throws Exception {

		// alias
		String alias = request.getAlias();
		if (alias == null || alias.trim().isEmpty())
			throw new Exception("Invalid user alias supplied for adding new user.");

		// name
		String name = request.getName();
		if (name == null || name.trim().isEmpty())
			throw new Exception("Invalid user name supplied for adding new user.");

		// organization
		String organization = request.getOrganization();
		if (organization == null || organization.trim().isEmpty())
			throw new Exception("Invalid user organization supplied for adding new user.");

		// email
		String email = request.getEmail();
		if (!Utility.isValidEmailAddress(email))
			throw new Exception("Invalid user email supplied for adding new user.");

		// permissions
		Permission[] permissions = request.getPermissions();
		if (permissions == null || permissions.length == 0)
			throw new Exception("Invalid user permissions supplied for adding new user.");
	}
}
