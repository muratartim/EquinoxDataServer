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
import java.sql.Timestamp;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.AccessRequest;
import equinox.dataServer.remote.message.SubmitAccessRequestRequest;
import equinox.dataServer.remote.message.SubmitAccessRequestResponse;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.Permission;

/**
 * Class for submit user access request task.
 *
 * @author Murat Artim
 * @date 14 Apr 2018
 * @time 23:17:39
 */
public class SubmitAccessRequest extends DatabaseQueryTask {

	/**
	 * Creates submit user access request task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public SubmitAccessRequest(DataServer server, DataClient client, SubmitAccessRequestRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for submitting access request.");

		// create response message
		SubmitAccessRequestResponse response = new SubmitAccessRequestResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		SubmitAccessRequestRequest request = (SubmitAccessRequestRequest) request_;
		Permission permission = request.getPermission();

		// check limits
		if (permission == null)
			throw new Exception("Requested permission not supplied.");

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// check if request already exists
				boolean exists = false;
				String sql = "select status from access_requests where user_id = " + client_.getID() + " and permission_name = '" + permission.toString() + "'";
				try (Statement statement = connection.createStatement()) {
					try (ResultSet resultSet = statement.executeQuery(sql)) {
						while (resultSet.next()) {
							String status = resultSet.getString("status");
							int serverResponse = status.equals(AccessRequest.PENDING) ? SubmitAccessRequestResponse.PENDING : SubmitAccessRequestResponse.REJECTED;
							response.setResponse(serverResponse);
							exists = true;
						}
					}
				}

				// create statement
				if (!exists) {
					sql = "insert into access_requests(user_id, permission_name, is_admin_permission, recorded, status) values(?, ?, ?, ?, ?)";
					try (PreparedStatement update = connection.prepareStatement(sql)) {
						update.setLong(1, client_.getID());
						update.setString(2, permission.toString());
						update.setBoolean(3, permission.isAdminPermission());
						update.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
						update.setString(5, AccessRequest.PENDING);
						update.executeUpdate();
						response.setResponse(SubmitAccessRequestResponse.SUBMITTED);
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

		// respond to client
		client_.sendMessage(response);
	}
}
