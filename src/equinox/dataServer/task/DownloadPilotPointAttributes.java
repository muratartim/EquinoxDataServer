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
import equinox.dataServer.remote.message.DownloadPilotPointAttributesRequest;
import equinox.dataServer.remote.message.DownloadPilotPointAttributesResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for download pilot point attributes task.
 *
 * @author Murat Artim
 * @date 7 Jul 2018
 * @time 00:59:23
 */
public class DownloadPilotPointAttributes extends DatabaseQueryTask {

	/**
	 * Creates download pilot point attributes task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public DownloadPilotPointAttributes(DataServer server, DataClient client, DownloadPilotPointAttributesRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for downloading pilot point attributes.");

		// create response message
		DownloadPilotPointAttributesResponse response = new DownloadPilotPointAttributesResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		DownloadPilotPointAttributesRequest request = (DownloadPilotPointAttributesRequest) request_;

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// execute query
				String sql = "select data_url from pilot_point_attribute where id = " + request.getDownloadId();
				try (ResultSet resultSet = statement.executeQuery(sql)) {

					// get download URL
					if (resultSet.next()) {
						String url = resultSet.getString("data_url");
						response.setDownloadUrl(url);
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
