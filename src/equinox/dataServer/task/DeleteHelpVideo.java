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
import java.sql.Statement;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.HelpVideoInfo;
import equinox.dataServer.remote.data.HelpVideoInfo.HelpVideoInfoType;
import equinox.dataServer.remote.message.DeleteHelpVideoRequest;
import equinox.dataServer.remote.message.DeleteHelpVideoResponse;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.FilerConnection;

/**
 * Class for delete help video task.
 *
 * @author Murat Artim
 * @date 8 Feb 2018
 * @time 23:21:25
 */
public class DeleteHelpVideo extends DatabaseQueryTask {

	/**
	 * Creates delete help video task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public DeleteHelpVideo(DataServer server, DataClient client, DeleteHelpVideoRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for deleting help video.");

		// create response message
		DeleteHelpVideoResponse response = new DeleteHelpVideoResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		DeleteHelpVideoRequest request = (DeleteHelpVideoRequest) request_;
		HelpVideoInfo info = request.getVideoInfo();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {

					// remove video data file from filer
					sendProgressMessage("Deleting video data from filer...");
					String url = (String) info.getInfo(HelpVideoInfoType.DATA_URL);
					if (url != null) {
						try (FilerConnection filer = getFilerConnection()) {
							if (filer.fileExists(url)) {
								filer.getSftpChannel().rm(url);
							}
						}
					}

					// delete spectrum info
					sendProgressMessage("Deleting video info from global database...");
					statement.executeUpdate("delete from videos where id = " + (long) info.getInfo(HelpVideoInfoType.ID));
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

		// video deleted
		response.setVideoDeleted(true);

		// respond to client
		client_.sendMessage(response);
	}
}
