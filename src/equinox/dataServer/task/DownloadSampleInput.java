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
import equinox.dataServer.remote.message.DownloadSampleInputRequest;
import equinox.dataServer.remote.message.DownloadSampleInputResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for download sample input task.
 *
 * @author Murat Artim
 * @date 12 Feb 2018
 * @time 00:16:38
 */
public class DownloadSampleInput extends DatabaseQueryTask {

	/**
	 * Creates download sample input task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public DownloadSampleInput(DataServer server, DataClient client, DownloadSampleInputRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for downloading sample input.");

		// create response message
		DownloadSampleInputResponse response = new DownloadSampleInputResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		DownloadSampleInputRequest request = (DownloadSampleInputRequest) request_;

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// download
				String sql = "select data_url from input_samples where name = '" + request.getName() + "'";
				try (ResultSet resultSet = statement.executeQuery(sql)) {
					if (resultSet.next()) {
						response.setDownloadUrl(resultSet.getString("data_url"));
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
