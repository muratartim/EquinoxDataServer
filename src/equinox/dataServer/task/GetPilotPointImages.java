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
import equinox.dataServer.remote.data.PilotPointImageType;
import equinox.dataServer.remote.message.GetPilotPointImagesRequest;
import equinox.dataServer.remote.message.GetPilotPointImagesResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for get pilot point images task.
 *
 * @author Murat Artim
 * @date 28 Mar 2018
 * @time 00:23:56
 */
public class GetPilotPointImages extends DatabaseQueryTask {

	/**
	 * Creates get pilot point images task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetPilotPointImages(DataServer server, DataClient client, GetPilotPointImagesRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for getting pilot point images.");

		// create response message
		GetPilotPointImagesResponse response = new GetPilotPointImagesResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		GetPilotPointImagesRequest request = (GetPilotPointImagesRequest) request_;
		long ppId = request.getPilotPointId();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// loop over image types
				for (PilotPointImageType imageType : PilotPointImageType.values()) {

					// create query
					String sql = "select image_url from " + imageType.getTableName() + " where id = " + ppId;
					try (ResultSet resultSet = statement.executeQuery(sql)) {
						if (resultSet.next()) {
							response.putImageUrl(imageType, resultSet.getString("image_url"));
						}
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
