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
import equinox.dataServer.remote.data.Wish;
import equinox.dataServer.remote.data.Wish.WishInfo;
import equinox.dataServer.remote.message.GetWishesRequest;
import equinox.dataServer.remote.message.GetWishesResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for get wishes task.
 *
 * @author Murat Artim
 * @date 16 Feb 2018
 * @time 16:19:09
 */
public class GetWishes extends DatabaseQueryTask {

	/**
	 * Creates get wishes task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetWishes(DataServer server, DataClient client, GetWishesRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for retrieving wishes.");

		// create response message
		GetWishesResponse response = new GetWishesResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		GetWishesRequest request = (GetWishesRequest) request_;
		int status = request.getStatus();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// create query
				String sql = "";
				if (status == GetWishesRequest.ALL) {
					sql = "select * from roadmap order by likes desc";
				}
				else if (status == GetWishesRequest.OPEN) {
					sql = "select * from roadmap where status = '" + Wish.OPEN + "' order by likes desc";
				}
				else if (status == GetWishesRequest.CLOSED) {
					sql = "select * from roadmap where status = '" + Wish.CLOSED + "' order by likes desc";
				}

				// execute query
				try (ResultSet resultSet = statement.executeQuery(sql)) {
					while (resultSet.next()) {
						Wish wish = new Wish();
						for (WishInfo info : WishInfo.values()) {
							wish.setInfo(info, resultSet.getObject(info.getColumn()));
						}
						response.addWish(wish);
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
