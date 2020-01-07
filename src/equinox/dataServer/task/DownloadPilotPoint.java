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
import equinox.dataServer.remote.message.DownloadPilotPointRequest;
import equinox.dataServer.remote.message.DownloadPilotPointResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for download pilot point task.
 *
 * @author Murat Artim
 * @date 11 Feb 2018
 * @time 22:10:43
 */
public class DownloadPilotPoint extends DatabaseQueryTask {

	/**
	 * Creates download pilot point task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public DownloadPilotPoint(DataServer server, DataClient client, DownloadPilotPointRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for downloading pilot point.");

		// create response message
		DownloadPilotPointResponse response = new DownloadPilotPointResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		DownloadPilotPointRequest request = (DownloadPilotPointRequest) request_;

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// execute query
				String sql = "select data_url from pilot_point_data where id = " + request.getDownloadId();
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
