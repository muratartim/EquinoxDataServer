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
import equinox.dataServer.remote.data.MultiplicationTableInfo;
import equinox.dataServer.remote.data.MultiplicationTableInfo.MultiplicationTableInfoType;
import equinox.dataServer.remote.message.DeleteMultiplicationTableRequest;
import equinox.dataServer.remote.message.DeleteMultiplicationTableResponse;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.FilerConnection;

/**
 * Class for delete multiplication table task.
 *
 * @author Murat Artim
 * @date 9 Feb 2018
 * @time 10:08:11
 */
public class DeleteMultiplicationTable extends DatabaseQueryTask {

	/**
	 * Creates delete multiplication table task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public DeleteMultiplicationTable(DataServer server, DataClient client, DeleteMultiplicationTableRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for deleting multiplication table.");

		// create response message
		DeleteMultiplicationTableResponse response = new DeleteMultiplicationTableResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		DeleteMultiplicationTableRequest request = (DeleteMultiplicationTableRequest) request_;
		MultiplicationTableInfo info = request.getMultiplicationTableInfo();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {

					// remove file from filer
					String url = (String) info.getInfo(MultiplicationTableInfoType.DATA_URL);
					if (url != null) {
						sendProgressMessage("Deleting multiplication table data from filer...");
						try (FilerConnection filerConnection = getFilerConnection()) {
							if (filerConnection.fileExists(url)) {
								filerConnection.getSftpChannel().rm(url);
							}
						}
					}

					// delete multiplication table info
					sendProgressMessage("Deleting multiplication table info from global database...");
					statement.executeUpdate("delete from mult_tables where id = " + (long) info.getInfo(MultiplicationTableInfoType.ID));
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
		response.setMultiplicationTableDeleted(true);

		// respond to client
		client_.sendMessage(response);
	}
}
