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
import equinox.dataServer.remote.data.EquinoxUpdate;
import equinox.dataServer.remote.data.EquinoxUpdate.EquinoxUpdateInfoType;
import equinox.dataServer.remote.message.CheckForEquinoxUpdatesRequest;
import equinox.dataServer.remote.message.CheckForEquinoxUpdatesResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for check for Equinox container updates task.
 *
 * @author Murat Artim
 * @date 26 Jan 2018
 * @time 10:46:56
 */
public class CheckForEquinoxContainerUpdates extends DatabaseQueryTask {

	/**
	 * Creates check for Equinox container updates task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public CheckForEquinoxContainerUpdates(DataServer server, DataClient client, CheckForEquinoxUpdatesRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for checking Equinox Container updates.");

		// create response message
		CheckForEquinoxUpdatesResponse response = new CheckForEquinoxUpdatesResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// build query
		CheckForEquinoxUpdatesRequest request = (CheckForEquinoxUpdatesRequest) request_;
		String sql = "select * from containers where os_type = '" + request.getOsType() + "' and os_arch = '" + request.getOsArch();
		sql += "' and version_number > " + Double.toString(request.getVersionNumber()) + " order by version_number desc";

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// only 1 version
				statement.setMaxRows(1);

				// execute query
				try (ResultSet resultSet = statement.executeQuery(sql)) {
					if (resultSet.next()) {

						// create update info
						EquinoxUpdate update = new EquinoxUpdate();
						update.setInfo(EquinoxUpdateInfoType.DATA_SIZE, resultSet.getLong("data_size"));
						update.setInfo(EquinoxUpdateInfoType.DATA_URL, resultSet.getString("data_url"));
						update.setInfo(EquinoxUpdateInfoType.UPLOAD_DATE, resultSet.getTimestamp("upload_date"));
						update.setInfo(EquinoxUpdateInfoType.OS_ARCH, resultSet.getString("os_arch"));
						update.setInfo(EquinoxUpdateInfoType.OS_TYPE, resultSet.getString("os_type"));
						update.setInfo(EquinoxUpdateInfoType.VERSION_DESCRIPTION, resultSet.getString("version_description"));
						update.setInfo(EquinoxUpdateInfoType.VERSION_NUMBER, resultSet.getDouble("version_number"));

						// set it to response
						response.setUpdate(update);
					}
				}

				// reset statement
				statement.setMaxRows(0);
			}
		}

		// respond to client
		client_.sendMessage(response);
	}
}
