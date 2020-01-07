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

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.AccessRequest;
import equinox.dataServer.remote.message.CloseAccessRequestRequest;
import equinox.dataServer.remote.message.CloseAccessRequestResponse;
import equinox.dataServer.remote.message.DatabaseQueryPermissionDenied;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.Permission;

/**
 * Class for close user access request task.
 *
 * @author Murat Artim
 * @date 15 Apr 2018
 * @time 20:38:36
 */
public class CloseAccessRequest extends DatabaseQueryTask {

	/**
	 * Creates close user access request task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public CloseAccessRequest(DataServer server, DataClient client, CloseAccessRequestRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for closing user access request.");

		// create response message
		CloseAccessRequestResponse response = new CloseAccessRequestResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		boolean permissionDenied = false;
		CloseAccessRequestRequest request = (CloseAccessRequestRequest) request_;
		String closure = request.getClosure();
		long requestId = request.getRequestId();
		String status = request.isGrantAccess() ? AccessRequest.GRANTED : AccessRequest.REJECTED;

		// no closure text given
		if (closure == null || closure.trim().isEmpty())
			throw new Exception("No reason for closure given for user access request. Operation aborted.");

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {

					// initialize variables
					Long userId = null;
					String permissionName = null;
					Boolean isAdminPermission = null;

					// get request info
					String sql = "select user_id, permission_name, is_admin_permission from access_requests where id = " + requestId;
					try (ResultSet resultSet = statement.executeQuery(sql)) {
						while (resultSet.next()) {
							userId = resultSet.getLong("user_id");
							permissionName = resultSet.getString("permission_name");
							isAdminPermission = resultSet.getBoolean("is_admin_permission");
						}
					}

					// request info couldn't be obtained
					if (userId == null || permissionName == null || isAdminPermission == null)
						throw new Exception("Cannot retrieve request info from database. Operation aborted.");

					// no permission to close admin permission request
					if (isAdminPermission) {
						if (!client_.hasPermission(Permission.CLOSE_ADMIN_ACCESS_REQUEST)) {
							permissionDenied = true;
						}
					}

					// has permission
					if (!permissionDenied) {

						// grant access
						if (status.equals(AccessRequest.GRANTED)) {
							sql = "insert into permissions(user_id, name, is_admin) values (" + userId + ", '" + permissionName + "', " + (isAdminPermission ? 1 : 0) + ")";
							statement.executeUpdate(sql);
						}

						// closure text supplied
						if (closure != null && !closure.isEmpty()) {
							statement.executeUpdate("update access_requests set status = '" + status + "', closure = '" + closure + "', closed_by = '" + client_.getUsername() + "' where id = " + requestId);
						}

						// no closure text given
						else {
							statement.executeUpdate("update access_requests set status = '" + status + "', closed_by = '" + client_.getUsername() + "' where id = " + requestId);
						}
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

		// permission denied
		if (permissionDenied) {
			DatabaseQueryPermissionDenied deniedResponse = new DatabaseQueryPermissionDenied(Permission.CLOSE_ADMIN_ACCESS_REQUEST);
			deniedResponse.setListenerHashCode(request_.getListenerHashCode());
			client_.sendMessage(deniedResponse);
		}

		// has permission
		else {
			response.setAccessRequestClosed(true);
			client_.sendMessage(response);
		}
	}
}
