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
import equinox.dataServer.remote.data.AccessRequest.AccessRequestInfo;
import equinox.dataServer.remote.message.GetAccessRequestsRequest;
import equinox.dataServer.remote.message.GetAccessRequestsResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for get user access requests task.
 *
 * @author Murat Artim
 * @date 15 Apr 2018
 * @time 14:16:02
 */
public class GetAccessRequests extends DatabaseQueryTask {

	/**
	 * Creates get user access requests task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetAccessRequests(DataServer server, DataClient client, GetAccessRequestsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for retrieving user access requests.");

		// create response message
		GetAccessRequestsResponse response = new GetAccessRequestsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		GetAccessRequestsRequest request = (GetAccessRequestsRequest) request_;
		int status = request.getStatus();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// create query
				String sql = "";
				if (status == GetAccessRequestsRequest.ALL) {
					sql = "select r.id, r.user_id, r.permission_name, r.is_admin_permission, r.recorded, r.status, r.closure, r.closed_by, u.alias, u.username, u.organization, u.email ";
					sql += "from access_requests r inner join users u on r.user_id = u.id order by r.recorded desc";
				}
				else if (status == GetAccessRequestsRequest.PENDING) {
					sql = "select r.id, r.user_id, r.permission_name, r.is_admin_permission, r.recorded, r.status, r.closure, r.closed_by, u.alias, u.username, u.organization, u.email ";
					sql += "from access_requests r inner join users u on r.user_id = u.id where r.status = '" + AccessRequest.PENDING + "' order by r.recorded desc";
				}
				else if (status == GetAccessRequestsRequest.GRANTED) {
					sql = "select r.id, r.user_id, r.permission_name, r.is_admin_permission, r.recorded, r.status, r.closure, r.closed_by, u.alias, u.username, u.organization, u.email ";
					sql += "from access_requests r inner join users u on r.user_id = u.id where r.status = '" + AccessRequest.GRANTED + "' order by r.recorded desc";
				}
				else if (status == GetAccessRequestsRequest.REJECTED) {
					sql = "select r.id, r.user_id, r.permission_name, r.is_admin_permission, r.recorded, r.status, r.closure, r.closed_by, u.alias, u.username, u.organization, u.email ";
					sql += "from access_requests r inner join users u on r.user_id = u.id where r.status = '" + AccessRequest.REJECTED + "' order by r.recorded desc";
				}

				// execute query
				try (ResultSet resultSet = statement.executeQuery(sql)) {
					while (resultSet.next()) {
						AccessRequest accessRequest = new AccessRequest();
						for (AccessRequestInfo info : AccessRequestInfo.values()) {
							accessRequest.setInfo(info, resultSet.getObject(info.getColumn()));
						}
						response.addRequest(accessRequest);
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
