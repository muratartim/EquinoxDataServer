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
import equinox.dataServer.remote.data.PilotPointInfo;
import equinox.dataServer.remote.data.PilotPointInfo.PilotPointInfoType;
import equinox.dataServer.remote.message.UpdatePilotPointRequest;
import equinox.dataServer.remote.message.UpdatePilotPointResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for update pilot point task.
 *
 * @author Murat Artim
 * @date 28 Feb 2018
 * @time 17:25:46
 */
public class UpdatePilotPoint extends DatabaseQueryTask {

	/**
	 * Creates update pilot point task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public UpdatePilotPoint(DataServer server, DataClient client, UpdatePilotPointRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for updating pilot point.");

		// create response message
		UpdatePilotPointResponse response = new UpdatePilotPointResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		UpdatePilotPointRequest request = (UpdatePilotPointRequest) request_;
		PilotPointInfo info = request.getInfo();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// update pilot point info
				updatePilotPoint(connection, info);

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
	 * Updates pilot point info in global database.
	 *
	 * @param connection
	 *            Database connection.
	 * @param info
	 *            Pilot point info.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void updatePilotPoint(Connection connection, PilotPointInfo info) throws Exception {

		// get pilot point ID and name
		long id = info.getID();
		String name = (String) info.getInfo(PilotPointInfoType.NAME);

		// update pilot point info
		String sql = "update pilot_points set spectrum_name = ?, ac_program = ?, ac_section = ?, fat_mission = ?, ";
		sql += "description = ?, element_type = ?, frame_rib_position = ?, stringer_position = ?, data_source = ?, ";
		sql += "generation_source = ?, delivery_ref_num = ?, issue = ?, fatigue_material = ?, preffas_material = ?, ";
		sql += "linear_material = ?, eid = ? where id = " + Long.toString(id) + " and name = '" + name + "'";
		try (PreparedStatement update = connection.prepareStatement(sql)) {
			update.setString(1, (String) info.getInfo(PilotPointInfoType.SPECTRUM_NAME));
			update.setString(2, (String) info.getInfo(PilotPointInfoType.AC_PROGRAM));
			update.setString(3, (String) info.getInfo(PilotPointInfoType.AC_SECTION));
			update.setString(4, (String) info.getInfo(PilotPointInfoType.FAT_MISSION));
			update.setString(5, (String) info.getInfo(PilotPointInfoType.DESCRIPTION));
			String elType = (String) info.getInfo(PilotPointInfoType.ELEMENT_TYPE);
			if (elType == null || elType.trim().isEmpty()) {
				update.setNull(6, java.sql.Types.VARCHAR);
			}
			else {
				update.setString(6, elType);
			}
			String framePos = (String) info.getInfo(PilotPointInfoType.FRAME_RIB_POSITION);
			if (framePos == null || framePos.trim().isEmpty()) {
				update.setNull(7, java.sql.Types.VARCHAR);
			}
			else {
				update.setString(7, framePos);
			}
			String stringerPos = (String) info.getInfo(PilotPointInfoType.STRINGER_POSITION);
			if (stringerPos == null || stringerPos.trim().isEmpty()) {
				update.setNull(8, java.sql.Types.VARCHAR);
			}
			else {
				update.setString(8, stringerPos);
			}
			update.setString(9, (String) info.getInfo(PilotPointInfoType.DATA_SOURCE));
			update.setString(10, (String) info.getInfo(PilotPointInfoType.GENERATION_SOURCE));
			update.setString(11, (String) info.getInfo(PilotPointInfoType.DELIVERY_REF_NUM));
			update.setString(12, (String) info.getInfo(PilotPointInfoType.ISSUE));
			String fatigueMaterial = (String) info.getInfo(PilotPointInfoType.FATIGUE_MATERIAL);
			if (fatigueMaterial == null || fatigueMaterial.trim().isEmpty()) {
				update.setNull(13, java.sql.Types.VARCHAR);
			}
			else {
				update.setString(13, fatigueMaterial);
			}
			String preffasMaterial = (String) info.getInfo(PilotPointInfoType.PREFFAS_MATERIAL);
			if (preffasMaterial == null || preffasMaterial.trim().isEmpty()) {
				update.setNull(14, java.sql.Types.VARCHAR);
			}
			else {
				update.setString(14, preffasMaterial);
			}
			String linearMaterial = (String) info.getInfo(PilotPointInfoType.LINEAR_MATERIAL);
			if (linearMaterial == null || linearMaterial.trim().isEmpty()) {
				update.setNull(15, java.sql.Types.VARCHAR);
			}
			else {
				update.setString(15, linearMaterial);
			}
			String eid = (String) info.getInfo(PilotPointInfoType.EID);
			if (eid == null || eid.trim().isEmpty()) {
				update.setNull(16, java.sql.Types.VARCHAR);
			}
			else {
				update.setString(16, eid);
			}
			update.executeUpdate();
		}
	}
}
