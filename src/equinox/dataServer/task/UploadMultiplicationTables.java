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
import java.sql.ResultSet;
import java.util.ArrayList;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.MultiplicationTableInfo;
import equinox.dataServer.remote.data.MultiplicationTableInfo.MultiplicationTableInfoType;
import equinox.dataServer.remote.message.UploadMultiplicationTablesRequest;
import equinox.dataServer.remote.message.UploadMultiplicationTablesResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for upload multiplication tables task.
 *
 * @author Murat Artim
 * @date 13 Mar 2018
 * @time 14:02:15
 */
public class UploadMultiplicationTables extends DatabaseQueryTask {

	/**
	 * Creates upload multiplication tables task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public UploadMultiplicationTables(DataServer server, DataClient client, UploadMultiplicationTablesRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for uploading multiplication tables.");

		// create response message
		UploadMultiplicationTablesResponse response = new UploadMultiplicationTablesResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		UploadMultiplicationTablesRequest request = (UploadMultiplicationTablesRequest) request_;
		ArrayList<MultiplicationTableInfo> infos = request.getMultiplicationTables();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// upload data
				uploadData(connection, infos);

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
		response.setUploaded(true);

		// respond to client
		client_.sendMessage(response);
	}

	/**
	 * Uploads given file data.
	 *
	 * @param connection
	 *            Database connection.
	 * @param infos
	 *            Multiplication table info.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void uploadData(Connection connection, ArrayList<MultiplicationTableInfo> infos) throws Exception {

		// prepare statement for getting spectrum
		String sql = "select id from spectra where name = ?";
		try (PreparedStatement getSpectrumInfo = connection.prepareStatement(sql)) {

			// prepare statement for getting pilot point
			sql = "select id from pilot_points where name = ? and spectrum_name = ?";
			try (PreparedStatement getPPInfo = connection.prepareStatement(sql)) {

				// prepare statement for checking multiplication table
				sql = "select id from mult_tables where spectrum_name = ? and name = ?";
				try (PreparedStatement checkMultTable = connection.prepareStatement(sql)) {

					// prepare statement for uploading multiplication table
					sql = "insert into mult_tables(spectrum_name, pilot_point_name, name, ac_program, ac_section, fat_mission, description, delivery_ref_num, issue, data_url) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					try (PreparedStatement uploadMultTable = connection.prepareStatement(sql)) {

						// loop over multiplication tables
						for (MultiplicationTableInfo info : infos) {

							// get pilot point info
							String tableName = (String) info.getInfo(MultiplicationTableInfoType.NAME);
							String spectrumName = (String) info.getInfo(MultiplicationTableInfoType.SPECTRUM_NAME);
							String pilotPointName = (String) info.getInfo(MultiplicationTableInfoType.PILOT_POINT_NAME);
							String program = (String) info.getInfo(MultiplicationTableInfoType.AC_PROGRAM);
							String section = (String) info.getInfo(MultiplicationTableInfoType.AC_SECTION);
							String mission = (String) info.getInfo(MultiplicationTableInfoType.FAT_MISSION);
							String issue = (String) info.getInfo(MultiplicationTableInfoType.ISSUE);
							String delRef = (String) info.getInfo(MultiplicationTableInfoType.DELIVERY_REF);
							String description = (String) info.getInfo(MultiplicationTableInfoType.DESCRIPTION);
							String dataUrl = (String) info.getInfo(MultiplicationTableInfoType.DATA_URL);

							// check values
							if (tableName.isEmpty() || spectrumName.isEmpty() || program.isEmpty() || section.isEmpty() || mission.isEmpty() || dataUrl.isEmpty()) {
								String message = "The upload information file multiplication table has missing entries. Aborting operation.";
								throw new Exception(message);
							}

							// progress info
							sendProgressMessage("Uploading multiplication table '" + tableName + "'...");

							// get spectrum info
							getSpectrumInfo.setString(1, spectrumName);
							try (ResultSet resultSet = getSpectrumInfo.executeQuery()) {

								// no spectrum found in database for given spectrum name
								if (!resultSet.next()) {

									// create error message
									String message = "Spectrum '" + spectrumName + "' does NOT exist in the global database. ";
									message += "Please make sure to upload all referenced spectra before uploading connected multiplication tables. Aborting operation.";

									// throw exception
									throw new Exception(message);
								}
							}

							// get pilot point info
							if (pilotPointName != null && !pilotPointName.trim().isEmpty()) {

								// get pilot point info
								getPPInfo.setString(1, pilotPointName);
								getPPInfo.setString(2, spectrumName);
								try (ResultSet resultSet = getPPInfo.executeQuery()) {

									// no pilot point found in database for given pilot point name
									if (!resultSet.next()) {

										// create error message
										String message = "Pilot point '" + pilotPointName + "' for spectrum '" + spectrumName + "' does NOT exist in the global database. ";
										message += "Please make sure to upload all referenced pilot points before uploading connected multiplication tables. Aborting operation.";

										// throw exception
										throw new Exception(message);
									}
								}
							}

							// check multiplication table
							checkMultTable.setString(1, spectrumName);
							checkMultTable.setString(2, tableName);
							try (ResultSet resultSet = checkMultTable.executeQuery()) {
								while (resultSet.next()) {

									// create error message
									String message = "Multiplication table '" + tableName + "' already exists in the global database for the spectrum '" + spectrumName;
									message += "'. If applicable, please first delete the existing multiplication table and then upload the new multiplication table. Aborting operation.";

									// throw exception
									throw new Exception(message);
								}
							}

							// upload multiplication table
							uploadMultTable.setString(1, spectrumName);
							uploadMultTable.setString(2, pilotPointName);
							uploadMultTable.setString(3, tableName);
							uploadMultTable.setString(4, program);
							uploadMultTable.setString(5, section);
							uploadMultTable.setString(6, mission);
							if (description == null || description.trim().isEmpty()) {
								uploadMultTable.setNull(7, java.sql.Types.VARCHAR);
							}
							else {
								uploadMultTable.setString(7, description);
							}
							if (delRef == null || delRef.trim().isEmpty()) {
								uploadMultTable.setNull(8, java.sql.Types.VARCHAR);
							}
							else {
								uploadMultTable.setString(8, delRef);
							}
							if (issue == null || issue.trim().isEmpty()) {
								uploadMultTable.setNull(9, java.sql.Types.VARCHAR);
							}
							else {
								uploadMultTable.setString(9, issue);
							}
							uploadMultTable.setString(10, dataUrl);
							uploadMultTable.executeUpdate();
						}
					}
				}
			}
		}
	}
}
