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

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.DownloadPilotPointsRequest;
import equinox.dataServer.remote.message.DownloadPilotPointsResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for download pilot points task.
 *
 * @author Murat Artim
 * @date 11 Feb 2018
 * @time 22:55:14
 */
public class DownloadPilotPoints extends DatabaseQueryTask {

	/**
	 * Creates download pilot points task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public DownloadPilotPoints(DataServer server, DataClient client, DownloadPilotPointsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for downloading pilot points.");

		// create response message
		DownloadPilotPointsResponse response = new DownloadPilotPointsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		DownloadPilotPointsRequest request = (DownloadPilotPointsRequest) request_;

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			String sql = "select data_url from pilot_point_data where id = ?";
			try (PreparedStatement statement = connection.prepareStatement(sql)) {

				// loop over download ids
				for (long downloadId : request.getDownloadIds()) {

					// set to statement
					statement.setLong(1, downloadId);

					// execute query
					try (ResultSet resultSet = statement.executeQuery()) {

						// get download URL
						if (resultSet.next()) {
							String url = resultSet.getString("data_url");
							response.addDownloadUrl(downloadId, url);
						}
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
