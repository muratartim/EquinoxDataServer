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
import equinox.dataServer.remote.message.ResetExchangeTableRequest;
import equinox.dataServer.remote.message.ResetExchangeTableResponse;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.FilerConnection;

/**
 * Class for reset exchange table task.
 *
 * @author Murat Artim
 * @date 21 Feb 2018
 * @time 14:08:46
 */
public class ResetExchangeTable extends DatabaseQueryTask {

	/**
	 * Creates reset exchange table task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public ResetExchangeTable(DataServer server, DataClient client, ResetExchangeTableRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for resetting exchange table.");

		// create response message
		ResetExchangeTableResponse response = new ResetExchangeTableResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {

					// truncate table
					statement.executeUpdate("truncate table exchange");

					// delete all files in exchange directory in filer
					try (FilerConnection filer = getFilerConnection()) {
						filer.deleteFilesInDirectory(filer.getDirectoryPath(FilerConnection.EXCHANGE), null);
					}
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

		// wish closed
		response.setReset(true);

		// respond to client
		client_.sendMessage(response);
	}
}
