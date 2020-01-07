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
import equinox.dataServer.remote.message.DownloadHelpVideoRequest;
import equinox.dataServer.remote.message.DownloadHelpVideoResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for download help video task.
 *
 * @author Murat Artim
 * @date 10 Feb 2018
 * @time 22:02:06
 */
public class DownloadHelpVideo extends DatabaseQueryTask {

	/**
	 * Creates download help video task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public DownloadHelpVideo(DataServer server, DataClient client, DownloadHelpVideoRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for downloading help video.");

		// create response message
		DownloadHelpVideoResponse response = new DownloadHelpVideoResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		DownloadHelpVideoRequest request = (DownloadHelpVideoRequest) request_;

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// execute query
				String selectWithName = "select data_url from videos where name = '" + request.getVideoName() + "'";
				String selectWithId = "select data_url from videos where id = " + request.getVideoId();
				String sql = request.getVideoId() == -1L ? selectWithName : selectWithId;
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
