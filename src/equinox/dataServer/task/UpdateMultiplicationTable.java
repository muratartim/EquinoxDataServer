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
import equinox.dataServer.remote.data.MultiplicationTableInfo;
import equinox.dataServer.remote.data.MultiplicationTableInfo.MultiplicationTableInfoType;
import equinox.dataServer.remote.message.UpdateMultiplicationTableRequest;
import equinox.dataServer.remote.message.UpdateMultiplicationTableResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for update multiplication table task.
 *
 * @author Murat Artim
 * @date 28 Feb 2018
 * @time 15:39:37
 */
public class UpdateMultiplicationTable extends DatabaseQueryTask {

	/**
	 * Creates update multiplication table task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public UpdateMultiplicationTable(DataServer server, DataClient client, UpdateMultiplicationTableRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for updating multiplication table.");

		// create response message
		UpdateMultiplicationTableResponse response = new UpdateMultiplicationTableResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		UpdateMultiplicationTableRequest request = (UpdateMultiplicationTableRequest) request_;
		MultiplicationTableInfo info = request.getInfo();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// update multiplication table
				updateMultiplicationTable(connection, info);

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
	 * Updates multiplication table info in global database.
	 *
	 * @param connection
	 *            Database connection.
	 * @param info
	 *            Multiplication table info.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void updateMultiplicationTable(Connection connection, MultiplicationTableInfo info) throws Exception {

		// get multiplication table ID and name
		long id = info.getID();
		String name = (String) info.getInfo(MultiplicationTableInfoType.NAME);

		// update multiplication table info
		String sql = "update mult_tables set spectrum_name = ?, pilot_point_name = ?, ac_program = ?, ac_section = ?, fat_mission = ?, ";
		sql += "description = ?, delivery_ref_num = ?, issue = ? where id = " + Long.toString(id) + " and name = '" + name + "'";
		try (PreparedStatement update = connection.prepareStatement(sql)) {
			update.setString(1, (String) info.getInfo(MultiplicationTableInfoType.SPECTRUM_NAME));
			update.setString(2, (String) info.getInfo(MultiplicationTableInfoType.PILOT_POINT_NAME));
			update.setString(3, (String) info.getInfo(MultiplicationTableInfoType.AC_PROGRAM));
			update.setString(4, (String) info.getInfo(MultiplicationTableInfoType.AC_SECTION));
			update.setString(5, (String) info.getInfo(MultiplicationTableInfoType.FAT_MISSION));
			String description = (String) info.getInfo(MultiplicationTableInfoType.DESCRIPTION);
			if (description == null || description.trim().isEmpty()) {
				update.setNull(6, java.sql.Types.VARCHAR);
			}
			else {
				update.setString(6, description);
			}
			String delRef = (String) info.getInfo(MultiplicationTableInfoType.DELIVERY_REF);
			if (delRef == null || delRef.trim().isEmpty()) {
				update.setNull(7, java.sql.Types.VARCHAR);
			}
			else {
				update.setString(7, delRef);
			}
			String issue = (String) info.getInfo(MultiplicationTableInfoType.ISSUE);
			if (issue == null || issue.trim().isEmpty()) {
				update.setNull(8, java.sql.Types.VARCHAR);
			}
			else {
				update.setString(8, issue);
			}
			update.executeUpdate();
		}
	}
}
