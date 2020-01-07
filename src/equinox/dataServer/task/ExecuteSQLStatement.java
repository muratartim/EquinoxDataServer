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
import equinox.dataServer.remote.message.ExecuteSQLStatementRequest;
import equinox.dataServer.remote.message.ExecuteSQLStatementResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for execute SQL statement task.
 *
 * @author Murat Artim
 * @date 15 Feb 2018
 * @time 11:38:32
 */
public class ExecuteSQLStatement extends DatabaseQueryTask {

	/**
	 * Creates execute SQL statement task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public ExecuteSQLStatement(DataServer server, DataClient client, ExecuteSQLStatementRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for executing SQL statement.");

		// create response message
		ExecuteSQLStatementResponse response = new ExecuteSQLStatementResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		ExecuteSQLStatementRequest request = (ExecuteSQLStatementRequest) request_;
		String sql = request.getSql();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {
					statement.executeUpdate(sql);
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

		// sql executed
		response.setSqlExecuted(true);

		// respond to client
		client_.sendMessage(response);
	}
}
