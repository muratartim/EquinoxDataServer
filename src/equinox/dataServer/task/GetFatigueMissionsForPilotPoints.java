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
import equinox.dataServer.remote.message.GetAircraftSectionsForPilotPointsRequest;
import equinox.dataServer.remote.message.GetFatigueMissionsForPilotPointsRequest;
import equinox.dataServer.remote.message.GetFatigueMissionsForPilotPointsResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for get fatigue missions for pilot points task.
 *
 * @author Murat Artim
 * @date 16 Feb 2018
 * @time 10:14:45
 */
public class GetFatigueMissionsForPilotPoints extends DatabaseQueryTask {

	/**
	 * Creates get fatigue missions for pilot points task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetFatigueMissionsForPilotPoints(DataServer server, DataClient client, GetFatigueMissionsForPilotPointsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for retrieving fatigue missions for pilot points.");

		// create response message
		GetFatigueMissionsForPilotPointsResponse response = new GetFatigueMissionsForPilotPointsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		GetFatigueMissionsForPilotPointsRequest request = (GetFatigueMissionsForPilotPointsRequest) request_;
		String program = request.getProgram();
		String section = request.getSection();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// get sections
				String sql = null;
				if (section.equals(GetAircraftSectionsForPilotPointsRequest.ALL_SECTIONS)) {
					sql = "select distinct fat_mission from pilot_points where ac_program = '" + program + "' order by fat_mission asc";
				}
				else {
					sql = "select distinct fat_mission from pilot_points where ac_program = '" + program + "' and ac_section = '" + section + "' order by fat_mission asc";
				}
				try (ResultSet resultSet = statement.executeQuery(sql)) {
					while (resultSet.next()) {
						response.addMission(resultSet.getString("fat_mission"));
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
