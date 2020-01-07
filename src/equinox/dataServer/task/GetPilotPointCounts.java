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
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.DataServerStatisticsRequestFailed;
import equinox.dataServer.remote.message.GetPilotPointCountsRequest;
import equinox.dataServer.remote.message.GetPilotPointCountsResponse;

/**
 * Class for get pilot point counts task.
 *
 * @author Murat Artim
 * @date 28 Jul 2018
 * @time 00:37:39
 */
public class GetPilotPointCounts extends ServerTask {

	/** Requesting client. */
	private final DataClient client;

	/** Request message. */
	private final GetPilotPointCountsRequest request;

	/**
	 * Creates get diagnostics task.
	 *
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetPilotPointCounts(DataClient client, GetPilotPointCountsRequest request) {
		super(client.getLobby().getServer());
		this.client = client;
		this.request = request;
	}

	@Override
	protected void runTask() throws Exception {

		// create response
		GetPilotPointCountsResponse response = new GetPilotPointCountsResponse();
		response.setListenerHashCode(request.getListenerHashCode());

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// get counts
				response.setPilotPointCounts(getPilotPointCounts(statement, 4));
			}
		}

		// send it
		client.sendMessage(response);
	}

	@Override
	protected void failed(Exception e) {

		// log exception
		server_.getLogger().log(Level.WARNING, "Get pilot point counts failed.", e);

		// no client
		if (client == null)
			return;

		// send query failed message to client
		DataServerStatisticsRequestFailed message = new DataServerStatisticsRequestFailed();
		message.setException(e);
		client.sendMessage(message);
	}

	/**
	 * Returns top 4 pilot point counts.
	 *
	 * @param statement
	 *            Database statement.
	 * @param limit
	 *            Number of items
	 * @return Top 4 pilot point counts.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static Map<String, Integer> getPilotPointCounts(Statement statement, int limit) throws Exception {

		// create mapping
		Map<String, Integer> counts = new HashMap<>();

		// set max hits
		statement.setMaxRows(4);

		// get counts
		String sql = "select ac_program, count(id) as ppCount from pilot_points group by ac_program order by ppCount desc";
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				String program = resultSet.getString("ac_program");
				int ppCount = resultSet.getInt("ppCount");
				counts.put(program, ppCount);
			}
		}

		// reset statement
		statement.setMaxRows(0);

		// sort and return counts
		limit = Math.min(limit, counts.size());
		return counts.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(limit).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
	}
}
