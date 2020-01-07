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
import equinox.dataServer.remote.data.Wish;
import equinox.dataServer.remote.message.CloseWishRequest;
import equinox.dataServer.remote.message.CloseWishResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for close wish task.
 *
 * @author Murat Artim
 * @date 8 Feb 2018
 * @time 22:03:44
 */
public class CloseWish extends DatabaseQueryTask {

	/**
	 * Creates close wish task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public CloseWish(DataServer server, DataClient client, CloseWishRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for closing wish.");

		// create response message
		CloseWishResponse response = new CloseWishResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		CloseWishRequest request = (CloseWishRequest) request_;
		String closure = request.getClosure();
		long wishId = request.getWishId();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {

					// closure text supplied
					if (closure != null && !closure.isEmpty()) {
						statement.executeUpdate("update roadmap set status = '" + Wish.CLOSED + "', closure = '" + closure + "', closed_by = '" + client_.getUsername() + "' where id = " + wishId);
					}

					// no closure text given
					else {
						statement.executeUpdate("update roadmap set status = '" + Wish.CLOSED + "', closed_by = '" + client_.getUsername() + "' where id = " + wishId);
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
		response.setWishClosed(true);

		// respond to client
		client_.sendMessage(response);
	}
}
