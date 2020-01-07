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
import equinox.dataServer.remote.data.Wish.WishInfo;
import equinox.dataServer.remote.message.LikeWishRequest;
import equinox.dataServer.remote.message.LikeWishResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for like wish task.
 *
 * @author Murat Artim
 * @date 18 Feb 2018
 * @time 20:34:35
 */
public class LikeWish extends DatabaseQueryTask {

	/**
	 * Creates like wish task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public LikeWish(DataServer server, DataClient client, LikeWishRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for liking wish.");

		// create response message
		LikeWishResponse response = new LikeWishResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		LikeWishRequest request = (LikeWishRequest) request_;
		Wish wish = request.getWish();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {
					int likes = (int) wish.getInfo(WishInfo.LIKES) + 1;
					statement.executeUpdate("update roadmap set likes = " + likes + " where id = " + (long) wish.getInfo(WishInfo.ID));
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

		// wish is liked
		response.setWishLiked(true);

		// respond to client
		client_.sendMessage(response);
	}
}
