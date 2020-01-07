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
import equinox.dataServer.remote.data.ServerPluginInfo;
import equinox.dataServer.remote.data.ServerPluginInfo.PluginInfoType;
import equinox.dataServer.remote.message.DeletePluginRequest;
import equinox.dataServer.remote.message.DeletePluginResponse;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.FilerConnection;

/**
 * Class for delete plugin task.
 *
 * @author Murat Artim
 * @date 9 Feb 2018
 * @time 14:14:47
 */
public class DeletePlugin extends DatabaseQueryTask {

	/**
	 * Creates delete plugin task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public DeletePlugin(DataServer server, DataClient client, DeletePluginRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for deleting plugin.");

		// create response message
		DeletePluginResponse response = new DeletePluginResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		DeletePluginRequest request = (DeletePluginRequest) request_;
		ServerPluginInfo info = request.getPluginInfo();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {

					// delete plugin jar file from filer
					sendProgressMessage("Deleting plugin jar file from filer...");
					String url = (String) info.getInfo(PluginInfoType.DATA_URL);
					try (FilerConnection filer = getFilerConnection()) {
						if (filer.fileExists(url)) {
							filer.getSftpChannel().rm(url);
						}
					}

					// delete plugin image from filer
					sendProgressMessage("Deleting plugin image file from filer...");
					url = (String) info.getInfo(PluginInfoType.IMAGE_URL);
					try (FilerConnection filer = getFilerConnection()) {
						if (filer.fileExists(url)) {
							filer.getSftpChannel().rm(url);
						}
					}

					// delete plugin info
					sendProgressMessage("Deleting plugin info from global database...");
					long id = (long) info.getInfo(PluginInfoType.ID);
					statement.executeUpdate("delete from plugins where id = " + id);
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
		response.setPluginDeleted(true);

		// respond to client
		client_.sendMessage(response);
	}
}
