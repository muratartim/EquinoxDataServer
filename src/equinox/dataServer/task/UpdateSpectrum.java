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

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.SpectrumInfo;
import equinox.dataServer.remote.data.SpectrumInfo.SpectrumInfoType;
import equinox.dataServer.remote.message.UpdateSpectrumRequest;
import equinox.dataServer.remote.message.UpdateSpectrumResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for update spectrum task.
 *
 * @author Murat Artim
 * @date 28 Feb 2018
 * @time 17:42:29
 */
public class UpdateSpectrum extends DatabaseQueryTask {

	/**
	 * Creates update spectrum task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public UpdateSpectrum(DataServer server, DataClient client, UpdateSpectrumRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for updating spectrum.");

		// create response message
		UpdateSpectrumResponse response = new UpdateSpectrumResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		UpdateSpectrumRequest request = (UpdateSpectrumRequest) request_;
		SpectrumInfo info = request.getInfo();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// update spectrum info
				updateSpectrum(connection, info);

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

		// set updated
		response.setUpdated(true);

		// respond to client
		client_.sendMessage(response);
	}

	/**
	 * Updates spectrum info in global database.
	 *
	 * @param connection
	 *            Database connection.
	 * @param info
	 *            Spectrum info.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void updateSpectrum(Connection connection, SpectrumInfo info) throws Exception {

		// get spectrum ID and name
		long id = info.getID();
		String name = (String) info.getInfo(SpectrumInfoType.NAME);

		// update spectrum info
		String sql = "update spectra set ac_program = ?, ac_section = ?, fat_mission = ?, fat_mission_issue = ?, flp_issue = ?, ";
		sql += "iflp_issue = ?, cdf_issue = ?, delivery_ref = ?, description = ? where id = " + Long.toString(id) + " and name = '" + name + "'";
		try (PreparedStatement update = connection.prepareStatement(sql)) {
			update.setString(1, (String) info.getInfo(SpectrumInfoType.AC_PROGRAM));
			update.setString(2, (String) info.getInfo(SpectrumInfoType.AC_SECTION));
			update.setString(3, (String) info.getInfo(SpectrumInfoType.FAT_MISSION));
			update.setString(4, (String) info.getInfo(SpectrumInfoType.FAT_MISSION_ISSUE));
			update.setString(5, (String) info.getInfo(SpectrumInfoType.FLP_ISSUE));
			update.setString(6, (String) info.getInfo(SpectrumInfoType.IFLP_ISSUE));
			update.setString(7, (String) info.getInfo(SpectrumInfoType.CDF_ISSUE));
			update.setString(8, (String) info.getInfo(SpectrumInfoType.DELIVERY_REF));
			update.setString(9, (String) info.getInfo(SpectrumInfoType.DESCRIPTION));
			update.executeUpdate();
		}
	}
}
