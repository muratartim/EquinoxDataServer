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
import equinox.dataServer.remote.data.BugReport;
import equinox.dataServer.remote.data.BugReport.BugReportInfo;
import equinox.dataServer.remote.message.GetBugReportsRequest;
import equinox.dataServer.remote.message.GetBugReportsResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for get bug reports task.
 *
 * @author Murat Artim
 * @date 15 Feb 2018
 * @time 15:24:51
 */
public class GetBugReports extends DatabaseQueryTask {

	/**
	 * Creates get bug reports task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetBugReports(DataServer server, DataClient client, GetBugReportsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for retrieving bug reports.");

		// create response message
		GetBugReportsResponse response = new GetBugReportsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		GetBugReportsRequest request = (GetBugReportsRequest) request_;
		int status = request.getStatus();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// create query
				String sql = "";
				if (status == GetBugReportsRequest.ALL) {
					sql = "select * from bug_reports order by recorded desc";
				}
				else if (status == GetBugReportsRequest.OPEN) {
					sql = "select * from bug_reports where status = '" + BugReport.OPEN + "' order by recorded desc";
				}
				else if (status == GetBugReportsRequest.CLOSED) {
					sql = "select * from bug_reports where status = '" + BugReport.CLOSED + "' order by recorded desc";
				}

				// execute query
				try (ResultSet resultSet = statement.executeQuery(sql)) {
					while (resultSet.next()) {
						BugReport report = new BugReport();
						for (BugReportInfo info : BugReportInfo.values()) {
							report.setInfo(info, resultSet.getObject(info.getColumn()));
						}
						response.addReport(report);
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
