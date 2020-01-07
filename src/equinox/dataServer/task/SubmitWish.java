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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.Wish;
import equinox.dataServer.remote.message.SubmitWishRequest;
import equinox.dataServer.remote.message.SubmitWishResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for submit wish task.
 *
 * @author Murat Artim
 * @date 22 Feb 2018
 * @time 14:16:05
 */
public class SubmitWish extends DatabaseQueryTask {

	/**
	 * Creates submit wish task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public SubmitWish(DataServer server, DataClient client, SubmitWishRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for submitting wish.");

		// create response message
		SubmitWishResponse response = new SubmitWishResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		SubmitWishRequest request = (SubmitWishRequest) request_;
		String title = request.getTitle();
		String description = request.getDescription();

		// check limits
		if (title == null)
			throw new Exception("No wish title supplied.");
		if (title.length() > Wish.MAX_TITLE_SIZE)
			throw new Exception("Wish title length exceeded the maximum allowed number of characters (" + Wish.MAX_TITLE_SIZE + ")");
		if (description != null && description.length() > Wish.MAX_DESCRIPTION_SIZE)
			throw new Exception("Wish description length exceeded the maximum allowed number of characters (" + Wish.MAX_DESCRIPTION_SIZE + ")");

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				String sql = "insert into roadmap(owner, title, description, recorded, likes, status) values(?, ?, ?, ?, ?, ?)";
				try (PreparedStatement update = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

					// set parameters and execute update
					String owner = client_.getUsername();
					if (owner == null) {
						update.setNull(1, java.sql.Types.VARCHAR);
					}
					else {
						update.setString(1, owner);
					}
					update.setString(2, title);
					if (description == null) {
						update.setNull(3, java.sql.Types.VARCHAR);
					}
					else {
						update.setString(3, description);
					}
					update.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
					update.setInt(5, 1);
					update.setString(6, Wish.OPEN);
					update.executeUpdate();

					// get wish ID
					try (ResultSet resultSet = update.getGeneratedKeys()) {
						if (resultSet.next()) {
							response.setWishId(resultSet.getBigDecimal(1).longValue());
						}
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

		// respond to client
		client_.sendMessage(response);
	}
}
