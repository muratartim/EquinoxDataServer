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
import equinox.dataServer.remote.message.GetBugReportCountRequest;
import equinox.dataServer.remote.message.GetBugReportCountResponse;

/**
 * Class for get bug report count task.
 *
 * @author Murat Artim
 * @date 28 Jul 2018
 * @time 00:40:15
 */
public class GetBugReportCount extends ServerTask {

	/** Requesting client. */
	private final DataClient client;

	/** Request message. */
	private final GetBugReportCountRequest request;

	/**
	 * Creates get diagnostics task.
	 *
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetBugReportCount(DataClient client, GetBugReportCountRequest request) {
		super(client.getLobby().getServer());
		this.client = client;
		this.request = request;
	}

	@Override
	protected void runTask() throws Exception {

		// create response
		GetBugReportCountResponse response = new GetBugReportCountResponse();
		response.setListenerHashCode(request.getListenerHashCode());

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// get counts
				response.setBugReportCounts(getBugReportCounts(statement));
			}
		}

		// send it
		client.sendMessage(response);
	}

	@Override
	protected void failed(Exception e) {

		// log exception
		server_.getLogger().log(Level.WARNING, "Get bug report count failed.", e);

		// no client
		if (client == null)
			return;

		// send query failed message to client
		DataServerStatisticsRequestFailed message = new DataServerStatisticsRequestFailed();
		message.setException(e);
		client.sendMessage(message);
	}

	/**
	 * Returns bug report counts.
	 *
	 * @param statement
	 *            Database statement.
	 * @return Bug report counts.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static Map<String, Integer> getBugReportCounts(Statement statement) throws Exception {

		// create mapping
		Map<String, Integer> counts = new HashMap<>();

		// get counts
		String sql = "select status, count(id) as bugCount from bug_reports group by status";
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				String status = resultSet.getString("status");
				int bugCount = resultSet.getInt("bugCount");
				counts.put(status, bugCount);
			}
		}

		// return counts
		return counts;
	}
}
