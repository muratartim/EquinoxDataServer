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
import java.util.ArrayList;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.CheckForMaterialUpdatesRequest;
import equinox.dataServer.remote.message.CheckForMaterialUpdatesResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for check for material updates task.
 *
 * @author Murat Artim
 * @date 26 Jan 2018
 * @time 14:48:19
 */
public class CheckForMaterialUpdates extends DatabaseQueryTask {

	/**
	 * Creates check for material updates task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public CheckForMaterialUpdates(DataServer server, DataClient client, CheckForMaterialUpdatesRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for checking material updates.");

		// create response message
		CheckForMaterialUpdatesResponse response = new CheckForMaterialUpdatesResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get client's material ISAMI versions
		ArrayList<String> localMaterialIsamiVersions = ((CheckForMaterialUpdatesRequest) request_).getMaterialIsamiVersions();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// get library version from fatigue materials table
				try (ResultSet resultSet = statement.executeQuery("select distinct isami_version from fatigue_materials")) {
					while (resultSet.next()) {
						String materialIsamiVersion = resultSet.getString("isami_version");
						if (!localMaterialIsamiVersions.contains(materialIsamiVersion)) {
							response.addMaterialIsamiVersion(materialIsamiVersion);
						}
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
