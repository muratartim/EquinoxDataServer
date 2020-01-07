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
import equinox.dataServer.remote.data.BugReport;
import equinox.dataServer.remote.message.CloseBugReportRequest;
import equinox.dataServer.remote.message.CloseBugReportResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for close bug report task.
 *
 * @author Murat Artim
 * @date 8 Feb 2018
 * @time 12:27:21
 */
public class CloseBugReport extends DatabaseQueryTask {

	/**
	 * Creates close bug report task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public CloseBugReport(DataServer server, DataClient client, CloseBugReportRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for closing bug report.");

		// create response message
		CloseBugReportResponse response = new CloseBugReportResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		CloseBugReportRequest request = (CloseBugReportRequest) request_;
		String solution = request.getSolution();
		long reportId = request.getBugReportId();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {

					// closure text supplied
					if (solution != null && !solution.isEmpty()) {
						statement.executeUpdate("update bug_reports set status = '" + BugReport.CLOSED + "', solution = '" + solution + "', closed_by = '" + client_.getUsername() + "' where id = " + reportId);
					}

					// no closure text given
					else {
						statement.executeUpdate("update bug_reports set status = '" + BugReport.CLOSED + "', closed_by = '" + client_.getUsername() + "' where id = " + reportId);
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

		// report closed
		response.setBugReportClosed(true);

		// respond to client
		client_.sendMessage(response);
	}
}
