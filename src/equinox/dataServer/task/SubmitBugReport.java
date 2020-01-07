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
import java.sql.Timestamp;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.BugReport;
import equinox.dataServer.remote.message.SubmitBugReportRequest;
import equinox.dataServer.remote.message.SubmitBugReportResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for submit bug report task.
 *
 * @author Murat Artim
 * @date 22 Feb 2018
 * @time 13:37:51
 */
public class SubmitBugReport extends DatabaseQueryTask {

	/**
	 * Creates submit bug report task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public SubmitBugReport(DataServer server, DataClient client, SubmitBugReportRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for submitting bug report.");

		// create response message
		SubmitBugReportResponse response = new SubmitBugReportResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		SubmitBugReportRequest request = (SubmitBugReportRequest) request_;
		String report = request.getReport();
		String eventLog = request.getEventLog();
		String sysInfo = request.getSystemInfo();

		// check limits
		if (report == null)
			throw new Exception("No bug report description supplied.");
		if (report.length() > BugReport.MAX_REPORT_SIZE)
			throw new Exception("Report length exceeded the maximum allowed number of characters (" + BugReport.MAX_REPORT_SIZE + ")");
		if (eventLog != null && eventLog.length() > BugReport.MAX_LOG_SIZE)
			throw new Exception("Event log length exceeded the maximum allowed number of characters (" + BugReport.MAX_LOG_SIZE + ")");
		if (sysInfo != null && sysInfo.length() > BugReport.MAX_SYS_INFO_SIZE)
			throw new Exception("System information length exceeded the maximum allowed number of characters (" + BugReport.MAX_SYS_INFO_SIZE + ")");

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				String sql = "insert into bug_reports(owner, report, sys_info, event_log, recorded, status) values(?, ?, ?, ?, ?, ?)";
				try (PreparedStatement update = connection.prepareStatement(sql)) {

					// set parameters and execute update
					String owner = client_.getUsername();
					if (owner == null) {
						update.setNull(1, java.sql.Types.VARCHAR);
					}
					else {
						update.setString(1, owner);
					}
					update.setString(2, report);
					if (sysInfo == null) {
						update.setNull(3, java.sql.Types.VARCHAR);
					}
					else {
						update.setString(3, sysInfo);
					}
					if (eventLog == null) {
						update.setNull(4, java.sql.Types.VARCHAR);
					}
					else {
						update.setString(4, eventLog);
					}
					update.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
					update.setString(6, BugReport.OPEN);
					update.executeUpdate();
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

		// set submitted to response
		response.setReportSubmitted(true);

		// respond to client
		client_.sendMessage(response);
	}
}
