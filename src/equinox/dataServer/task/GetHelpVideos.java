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
import equinox.dataServer.remote.data.HelpVideoInfo;
import equinox.dataServer.remote.data.HelpVideoInfo.HelpVideoInfoType;
import equinox.dataServer.remote.message.GetHelpVideosRequest;
import equinox.dataServer.remote.message.GetHelpVideosResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for get help videos task.
 *
 * @author Murat Artim
 * @date 16 Feb 2018
 * @time 11:00:47
 */
public class GetHelpVideos extends DatabaseQueryTask {

	/**
	 * Creates get help videos task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetHelpVideos(DataServer server, DataClient client, GetHelpVideosRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for retrieving help videos.");

		// create response message
		GetHelpVideosResponse response = new GetHelpVideosResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// execute query
				try (ResultSet resultSet = statement.executeQuery("select * from videos order by name asc")) {

					// loop over videos
					while (resultSet.next()) {

						// add video info
						HelpVideoInfo info = new HelpVideoInfo();
						for (HelpVideoInfoType type : HelpVideoInfoType.values()) {
							info.setInfo(type, resultSet.getObject(type.getColumnName()));
						}
						response.addVideo(info);
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
