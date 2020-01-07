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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.DataServerStatisticsRequestFailed;
import equinox.dataServer.remote.message.GetAccessRequestCountRequest;
import equinox.dataServer.remote.message.GetAccessRequestCountResponse;

/**
 * Class for get access request count task.
 *
 * @author Murat Artim
 * @date 28 Jul 2018
 * @time 00:45:45
 */
public class GetAccessRequestCount extends ServerTask {

	/** Requesting client. */
	private final DataClient client;

	/** Request message. */
	private final GetAccessRequestCountRequest request;

	/**
	 * Creates get diagnostics task.
	 *
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetAccessRequestCount(DataClient client, GetAccessRequestCountRequest request) {
		super(client.getLobby().getServer());
		this.client = client;
		this.request = request;
	}

	@Override
	protected void runTask() throws Exception {

		// create response
		GetAccessRequestCountResponse response = new GetAccessRequestCountResponse();
		response.setListenerHashCode(request.getListenerHashCode());

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// get counts
				response.setAccessRequestCounts(getAccessRequestCounts(statement));
			}
		}

		// send it
		client.sendMessage(response);
	}

	@Override
	protected void failed(Exception e) {

		// log exception
		server_.getLogger().log(Level.WARNING, "Get access request count failed.", e);

		// no client
		if (client == null)
			return;

		// send query failed message to client
		DataServerStatisticsRequestFailed message = new DataServerStatisticsRequestFailed();
		message.setException(e);
		client.sendMessage(message);
	}

	/**
	 * Returns access request counts.
	 *
	 * @param statement
	 *            Database statement.
	 * @return Access request counts.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static Map<String, Integer> getAccessRequestCounts(Statement statement) throws Exception {

		// create mapping
		Map<String, Integer> counts = new HashMap<>();

		// get counts
		String sql = "select status, count(id) as accessRequestCount from access_requests group by status";
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				String status = resultSet.getString("status");
				int accessRequestCount = resultSet.getInt("accessRequestCount");
				counts.put(status, accessRequestCount);
			}
		}

		// return counts
		return counts;
	}
}
