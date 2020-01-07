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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.GetUserPermissionsRequest;
import equinox.dataServer.remote.message.GetUserPermissionsResponse;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.Permission;

/**
 * Class for get user permissions task.
 *
 * @author Murat Artim
 * @date 6 Apr 2018
 * @time 10:19:37
 */
public class GetUserPermissions extends DatabaseQueryTask {

	/**
	 * Creates get user permissions task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetUserPermissions(DataServer server, DataClient client, GetUserPermissionsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for retrieving user permissions.");

		// get request
		GetUserPermissionsRequest request = (GetUserPermissionsRequest) request_;

		// create response message
		GetUserPermissionsResponse response = new GetUserPermissionsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());
		response.setAlias(request.getAlias());

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// get user id
				long userId = -1;
				String sql = "select id from users where alias = '" + request.getAlias() + "'";
				try (ResultSet resultSet = statement.executeQuery(sql)) {
					while (resultSet.next()) {
						userId = resultSet.getLong("id");
					}
				}

				// user could not be found
				if (userId == -1)
					throw new Exception("No user found with alias '" + request.getAlias() + "'. User permissions cannot be retrieved.");

				// check if user is admin
				sql = "select id from admins where user_id = " + userId;
				try (ResultSet resultSet = statement.executeQuery(sql)) {
					if (resultSet.next())
						throw new Exception("Administrator permissions cannot be requested with this operation. Operation aborted.");
				}

				// get user permissions
				ArrayList<Permission> permissions = new ArrayList<>();
				sql = "select name from permissions where user_id = " + userId + " and is_admin = 0";
				try (ResultSet resultSet = statement.executeQuery(sql)) {
					while (resultSet.next()) {
						permissions.add(Permission.valueOf(resultSet.getString("name")));
					}
				}

				// set permissions to response
				response.setPermissions(permissions.toArray(new Permission[permissions.size()]));
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
