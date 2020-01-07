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
import equinox.dataServer.remote.data.FatigueMaterial;
import equinox.dataServer.remote.message.GetFatigueMaterialsRequest;
import equinox.dataServer.remote.message.GetFatigueMaterialsResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for get fatigue materials task.
 *
 * @author Murat Artim
 * @date 23 Feb 2018
 * @time 12:58:09
 */
public class GetFatigueMaterials extends DatabaseQueryTask {

	/**
	 * Creates get fatigue materials task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetFatigueMaterials(DataServer server, DataClient client, GetFatigueMaterialsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for retrieving fatigue materials.");

		// create response message
		GetFatigueMaterialsResponse response = new GetFatigueMaterialsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		GetFatigueMaterialsRequest request = (GetFatigueMaterialsRequest) request_;
		String materialIsamiVersion = request.getMaterialIsamiVersion();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// get all fatigue materials
				try (ResultSet materials = statement.executeQuery("select * from fatigue_materials where isami_version = '" + materialIsamiVersion + "'")) {

					// loop over materials
					while (materials.next()) {

						// get material attributes
						FatigueMaterial material = new FatigueMaterial(materials.getLong("id"));
						material.setName(materials.getString("name"));
						material.setSpecification(materials.getString("specification"));
						material.setLibraryVersion(materials.getString("library_version"));
						material.setFamily(materials.getString("family"));
						material.setOrientation(materials.getString("orientation"));
						material.setConfiguration(materials.getString("configuration"));
						material.setP(materials.getDouble("par_p"));
						material.setQ(materials.getDouble("par_q"));
						material.setM(materials.getDouble("par_m"));
						material.setIsamiVersion(materials.getString("isami_version"));

						// add material to response
						response.addMaterial(material);
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
