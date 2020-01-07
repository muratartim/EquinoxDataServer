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
import equinox.dataServer.remote.message.GetAircraftProgramsForPilotPointsRequest;
import equinox.dataServer.remote.message.GetAircraftProgramsForPilotPointsResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for get aircraft programs for pilot points task.
 *
 * @author Murat Artim
 * @date 15 Feb 2018
 * @time 12:06:44
 */
public class GetAircraftProgramsForPilotPoints extends DatabaseQueryTask {

	/**
	 * Creates get aircraft programs for pilot points task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetAircraftProgramsForPilotPoints(DataServer server, DataClient client, GetAircraftProgramsForPilotPointsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for getting aircraft programs for pilot points.");

		// create response message
		GetAircraftProgramsForPilotPointsResponse response = new GetAircraftProgramsForPilotPointsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// get programs
				try (ResultSet resultSet = statement.executeQuery("select distinct ac_program from pilot_points order by ac_program asc")) {
					while (resultSet.next()) {
						response.addProgram(resultSet.getString("ac_program"));
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
