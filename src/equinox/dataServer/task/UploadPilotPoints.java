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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.PilotPointImageType;
import equinox.dataServer.remote.data.PilotPointInfo;
import equinox.dataServer.remote.data.PilotPointInfo.PilotPointInfoType;
import equinox.dataServer.remote.message.UploadPilotPointsRequest;
import equinox.dataServer.remote.message.UploadPilotPointsResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for upload pilot points task.
 *
 * @author Murat Artim
 * @date 14 Mar 2018
 * @time 12:40:00
 */
public class UploadPilotPoints extends DatabaseQueryTask {

	/**
	 * Creates upload pilot points task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public UploadPilotPoints(DataServer server, DataClient client, UploadPilotPointsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for uploading pilot points.");

		// create response message
		UploadPilotPointsResponse response = new UploadPilotPointsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		UploadPilotPointsRequest request = (UploadPilotPointsRequest) request_;
		ArrayList<PilotPointInfo> infos = request.getInfo();
		ArrayList<String> dataUrls = request.getDataUrls();
		ArrayList<String> attributeUrls = request.getAttributeUrls();
		ArrayList<HashMap<PilotPointImageType, String>> imageUrls = request.getImageUrls();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// upload info
				ArrayList<Long> ids = uploadInfo(connection, infos);

				// upload data URLs
				uploadDataUrls(connection, dataUrls, ids);

				// upload attribute URLs
				if (!attributeUrls.isEmpty()) {
					uploadAttributeUrls(connection, attributeUrls, ids);
				}

				// upload image URLs
				uploadImageUrls(connection, imageUrls, ids);

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
	 * Uploads pilot point image URLs.
	 *
	 * @param connection
	 *            Database connection.
	 * @param imageUrls
	 *            List of pilot point image URLs.
	 * @param ids
	 *            List of pilot point ids.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void uploadImageUrls(Connection connection, ArrayList<HashMap<PilotPointImageType, String>> imageUrls, ArrayList<Long> ids) throws Exception {

		// send progress info
		sendProgressMessage("Uploading pilot point image URLs to central database...");

		// prepare statement for uploading pilot point image
		String sql = "insert into pilot_point_image(id, image_url) values(?, ?)";
		try (PreparedStatement uploadPilotPointImage = connection.prepareStatement(sql)) {

			// prepare statement for uploading pilot point mission profile plot
			sql = "insert into pilot_point_mp(id, image_url) values(?, ?)";
			try (PreparedStatement uploadPilotPointMP = connection.prepareStatement(sql)) {

				// prepare statement for uploading pilot point longest flight plot
				sql = "insert into pilot_point_tf_l(id, image_url) values(?, ?)";
				try (PreparedStatement uploadPilotPointTFL = connection.prepareStatement(sql)) {

					// prepare statement for uploading pilot point flight with highest occurrence plot
					sql = "insert into pilot_point_tf_ho(id, image_url) values(?, ?)";
					try (PreparedStatement uploadPilotPointTFHO = connection.prepareStatement(sql)) {

						// prepare statement for uploading pilot point flight with highest stress plot
						sql = "insert into pilot_point_tf_hs(id, image_url) values(?, ?)";
						try (PreparedStatement uploadPilotPointTFHS = connection.prepareStatement(sql)) {

							// prepare statement for uploading pilot point level crossing plot
							sql = "insert into pilot_point_lc(id, image_url) values(?, ?)";
							try (PreparedStatement uploadPilotPointLC = connection.prepareStatement(sql)) {

								// prepare statement for uploading pilot point damage angle plot
								sql = "insert into pilot_point_da(id, image_url) values(?, ?)";
								try (PreparedStatement uploadPilotPointDA = connection.prepareStatement(sql)) {

									// prepare statement for uploading pilot point number of peaks plot
									sql = "insert into pilot_point_st_nop(id, image_url) values(?, ?)";
									try (PreparedStatement uploadPilotPointSTNOP = connection.prepareStatement(sql)) {

										// prepare statement for uploading pilot point flight occurrence plot
										sql = "insert into pilot_point_st_fo(id, image_url) values(?, ?)";
										try (PreparedStatement uploadPilotPointSTFO = connection.prepareStatement(sql)) {

											// prepare statement for uploading pilot point rainflow histogram plot
											sql = "insert into pilot_point_st_rh(id, image_url) values(?, ?)";
											try (PreparedStatement uploadPilotPointSTRH = connection.prepareStatement(sql)) {

												// prepare statement for uploading pilot point loadcase damage contribution plot
												sql = "insert into pilot_point_dc(id, image_url) values(?, ?)";
												try (PreparedStatement uploadPilotPointDC = connection.prepareStatement(sql)) {

													// prepare statement for uploading pilot point typical flight damage contribution plot
													sql = "insert into pilot_point_tf_dc(id, image_url) values(?, ?)";
													try (PreparedStatement uploadPilotPointTFDC = connection.prepareStatement(sql)) {

														// loop over pilot points
														for (int i = 0; i < ids.size(); i++) {

															// get pilot point id
															long id = ids.get(i);

															// get image URLs
															HashMap<PilotPointImageType, String> urls = imageUrls.get(i);

															// no images
															if (urls == null || urls.isEmpty()) {
																continue;
															}

															// upload image
															String url = urls.get(PilotPointImageType.IMAGE);
															if (url != null) {
																uploadPilotPointImage.setLong(1, id);
																uploadPilotPointImage.setString(2, url);
																uploadPilotPointImage.executeUpdate();
															}

															// upload pilot point mission profile plot
															url = urls.get(PilotPointImageType.MISSION_PROFILE);
															if (url != null) {
																uploadPilotPointMP.setLong(1, id);
																uploadPilotPointMP.setString(2, url);
																uploadPilotPointMP.executeUpdate();
															}

															// upload pilot point longest flight plot
															url = urls.get(PilotPointImageType.LONGEST_FLIGHT);
															if (url != null) {
																uploadPilotPointTFL.setLong(1, id);
																uploadPilotPointTFL.setString(2, url);
																uploadPilotPointTFL.executeUpdate();
															}

															// upload pilot point highest occurrence flight plot
															url = urls.get(PilotPointImageType.FLIGHT_WITH_HIGHEST_OCCURRENCE);
															if (url != null) {
																uploadPilotPointTFHO.setLong(1, id);
																uploadPilotPointTFHO.setString(2, url);
																uploadPilotPointTFHO.executeUpdate();
															}

															// upload pilot point highest stress flight plot
															url = urls.get(PilotPointImageType.FLIGHT_WITH_MAX_TOTAL_STRESS);
															if (url != null) {
																uploadPilotPointTFHS.setLong(1, id);
																uploadPilotPointTFHS.setString(2, url);
																uploadPilotPointTFHS.executeUpdate();
															}

															// upload pilot point level crossing plot
															url = urls.get(PilotPointImageType.LEVEL_CROSSING);
															if (url != null) {
																uploadPilotPointLC.setLong(1, id);
																uploadPilotPointLC.setString(2, url);
																uploadPilotPointLC.executeUpdate();
															}

															// upload pilot point damage angle plot
															url = urls.get(PilotPointImageType.DAMAGE_ANGLE);
															if (url != null) {
																uploadPilotPointDA.setLong(1, id);
																uploadPilotPointDA.setString(2, url);
																uploadPilotPointDA.executeUpdate();
															}

															// upload pilot point number of peaks plot
															url = urls.get(PilotPointImageType.NUMBER_OF_PEAKS);
															if (url != null) {
																uploadPilotPointSTNOP.setLong(1, id);
																uploadPilotPointSTNOP.setString(2, url);
																uploadPilotPointSTNOP.executeUpdate();
															}

															// upload pilot point flight occurrence plot
															url = urls.get(PilotPointImageType.FLIGHT_OCCURRENCE);
															if (url != null) {
																uploadPilotPointSTFO.setLong(1, id);
																uploadPilotPointSTFO.setString(2, url);
																uploadPilotPointSTFO.executeUpdate();
															}

															// upload pilot point rainflow histogram plot
															url = urls.get(PilotPointImageType.RAINFLOW_HISTOGRAM);
															if (url != null) {
																uploadPilotPointSTRH.setLong(1, id);
																uploadPilotPointSTRH.setString(2, url);
																uploadPilotPointSTRH.executeUpdate();
															}

															// upload pilot point loadcase damage contribution plot
															url = urls.get(PilotPointImageType.LOADCASE_DAMAGE_CONTRIBUTION);
															if (url != null) {
																uploadPilotPointDC.setLong(1, id);
																uploadPilotPointDC.setString(2, url);
																uploadPilotPointDC.executeUpdate();
															}

															// upload pilot point typical flight damage contribution plot
															url = urls.get(PilotPointImageType.FLIGHT_DAMAGE_CONTRIBUTION);
															if (url != null) {
																uploadPilotPointTFDC.setLong(1, id);
																uploadPilotPointTFDC.setString(2, url);
																uploadPilotPointTFDC.executeUpdate();
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Uploads pilot point data URLs.
	 *
	 * @param connection
	 *            Database connection.
	 * @param dataUrls
	 *            List of pilot point data URLs.
	 * @param ids
	 *            List of pilot point ids.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void uploadDataUrls(Connection connection, ArrayList<String> dataUrls, ArrayList<Long> ids) throws Exception {

		// send progress info
		sendProgressMessage("Uploading pilot point data URLs to central database...");

		// prepare statement for uploading pilot point data
		String sql = "insert into pilot_point_data(id, data_url) values(?, ?)";
		try (PreparedStatement uploadPilotPointData = connection.prepareStatement(sql)) {

			// loop over pilot points
			for (int i = 0; i < ids.size(); i++) {

				// upload pilot point data
				uploadPilotPointData.setLong(1, ids.get(i));
				uploadPilotPointData.setString(2, dataUrls.get(i));
				uploadPilotPointData.executeUpdate();
			}
		}
	}

	/**
	 * Uploads pilot point attribute URLs.
	 *
	 * @param connection
	 *            Database connection.
	 * @param attributeUrls
	 *            List of pilot point attribute URLs.
	 * @param ids
	 *            List of pilot point ids.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void uploadAttributeUrls(Connection connection, ArrayList<String> attributeUrls, ArrayList<Long> ids) throws Exception {

		// send progress info
		sendProgressMessage("Uploading pilot point attribute URLs to central database...");

		// prepare statement for uploading pilot point data
		String sql = "insert into pilot_point_attribute(id, data_url) values(?, ?)";
		try (PreparedStatement uploadPilotPointData = connection.prepareStatement(sql)) {

			// loop over pilot points
			for (int i = 0; i < ids.size(); i++) {

				// no attribute supplied
				String url = attributeUrls.get(i);
				if (url == null) {
					continue;
				}

				// upload pilot point data
				uploadPilotPointData.setLong(1, ids.get(i));
				uploadPilotPointData.setString(2, url);
				uploadPilotPointData.executeUpdate();
			}
		}
	}

	/**
	 * Uploads pilot point info.
	 *
	 * @param connection
	 *            Database connection.
	 * @param infos
	 *            Pilot point info list.
	 * @return List of pilot point ids. Note that, the returned list is in the same order as the supplied info list.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private ArrayList<Long> uploadInfo(Connection connection, ArrayList<PilotPointInfo> infos) throws Exception {

		// send progress info
		sendProgressMessage("Uploading pilot point info to central database...");

		// create list to store pilot point ids
		ArrayList<Long> ids = new ArrayList<>();

		// prepare statement for getting spectrum info
		String sql = "select id, ac_program, ac_section from spectra where name = ?";
		try (PreparedStatement getSpectrumInfo = connection.prepareStatement(sql)) {

			// prepare statement for checking pilot point
			sql = "select id from pilot_points where spectrum_name = ? and name = ?";
			try (PreparedStatement checkPilotPoint = connection.prepareStatement(sql)) {

				// prepare statement for uploading pilot point info
				sql = "insert into pilot_points(spectrum_name, name, ac_program, ac_section, fat_mission, description, element_type, frame_rib_position, ";
				sql += "stringer_position, data_source, generation_source, delivery_ref_num, issue, fatigue_material, preffas_material, linear_material, eid) ";
				sql += "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				try (PreparedStatement uploadPilotPointInfo = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

					// loop over pilot points
					for (PilotPointInfo info : infos) {

						// get pilot point info
						String name = (String) info.getInfo(PilotPointInfoType.NAME);
						String spectrumName = (String) info.getInfo(PilotPointInfoType.SPECTRUM_NAME);
						String mission = (String) info.getInfo(PilotPointInfoType.FAT_MISSION);
						String description = (String) info.getInfo(PilotPointInfoType.DESCRIPTION);
						String dataSource = (String) info.getInfo(PilotPointInfoType.DATA_SOURCE);
						String generationSource = (String) info.getInfo(PilotPointInfoType.GENERATION_SOURCE);
						String delRef = (String) info.getInfo(PilotPointInfoType.DELIVERY_REF_NUM);
						String issue = (String) info.getInfo(PilotPointInfoType.ISSUE);
						String eid = (String) info.getInfo(PilotPointInfoType.EID);
						String elementType = (String) info.getInfo(PilotPointInfoType.ELEMENT_TYPE);
						String framePos = (String) info.getInfo(PilotPointInfoType.FRAME_RIB_POSITION);
						String stringerPos = (String) info.getInfo(PilotPointInfoType.STRINGER_POSITION);
						String fatigueMaterial = (String) info.getInfo(PilotPointInfoType.FATIGUE_MATERIAL);
						String preffasMaterial = (String) info.getInfo(PilotPointInfoType.PREFFAS_MATERIAL);
						String linearMaterial = (String) info.getInfo(PilotPointInfoType.LINEAR_MATERIAL);

						// check values
						if (name.isEmpty() || spectrumName.isEmpty() || mission.isEmpty() || description.isEmpty() || dataSource.isEmpty() || generationSource.isEmpty() || delRef.isEmpty() || issue.isEmpty()) {
							String message = "The pilot point upload information has missing entries. Aborting operation.";
							throw new Exception(message);
						}

						// get spectrum ID
						long spectrumID = -1L;
						String program = null, section = null;
						getSpectrumInfo.setString(1, spectrumName);
						try (ResultSet resultSet = getSpectrumInfo.executeQuery()) {
							while (resultSet.next()) {
								spectrumID = resultSet.getLong("id");
								program = resultSet.getString("ac_program");
								section = resultSet.getString("ac_section");
							}
						}

						// no spectrum found in database for given spectrum name
						if (spectrumID == -1L || program == null || section == null) {

							// create error message
							String message = "Spectrum '" + spectrumName + "' does NOT exist in the global database. ";
							message += "Please make sure to upload all referenced spectra before uploading connected pilot points. Aborting operation.";

							// throw exception
							throw new Exception(message);
						}

						// check pilot point
						checkPilotPoint.setString(1, spectrumName);
						checkPilotPoint.setString(2, name);
						try (ResultSet resultSet = checkPilotPoint.executeQuery()) {
							while (resultSet.next()) {

								// create error message
								String message = "Pilot point '" + name + "' already exists in the global database for the spectrum '" + spectrumName;
								message += "'. If applicable, please first delete the existing pilot point and then upload the new pilot point. Aborting operation.";

								// throw exception
								throw new Exception(message);
							}
						}

						// check lengths
						if (description.length() >= 200) {
							description = description.substring(0, 194) + "...";
						}
						if (fatigueMaterial != null && fatigueMaterial.length() >= 500) {
							fatigueMaterial = fatigueMaterial.substring(0, 494) + "...";
						}
						if (preffasMaterial != null && preffasMaterial.length() >= 500) {
							preffasMaterial = preffasMaterial.substring(0, 494) + "...";
						}
						if (linearMaterial != null && linearMaterial.length() >= 500) {
							linearMaterial = linearMaterial.substring(0, 494) + "...";
						}

						// upload pilot point
						uploadPilotPointInfo.setString(1, spectrumName);
						uploadPilotPointInfo.setString(2, name);
						uploadPilotPointInfo.setString(3, program);
						uploadPilotPointInfo.setString(4, section);
						uploadPilotPointInfo.setString(5, mission);
						uploadPilotPointInfo.setString(6, description);
						if (elementType == null || elementType.isEmpty()) {
							uploadPilotPointInfo.setNull(7, java.sql.Types.VARCHAR);
						}
						else {
							uploadPilotPointInfo.setString(7, elementType);
						}
						if (framePos == null || framePos.isEmpty()) {
							uploadPilotPointInfo.setNull(8, java.sql.Types.VARCHAR);
						}
						else {
							uploadPilotPointInfo.setString(8, framePos);
						}
						if (stringerPos == null || stringerPos.isEmpty()) {
							uploadPilotPointInfo.setNull(9, java.sql.Types.VARCHAR);
						}
						else {
							uploadPilotPointInfo.setString(9, stringerPos);
						}
						uploadPilotPointInfo.setString(10, dataSource);
						uploadPilotPointInfo.setString(11, generationSource);
						uploadPilotPointInfo.setString(12, delRef);
						uploadPilotPointInfo.setString(13, issue);
						if (fatigueMaterial == null || fatigueMaterial.isEmpty()) {
							uploadPilotPointInfo.setNull(14, java.sql.Types.VARCHAR);
						}
						else {
							uploadPilotPointInfo.setString(14, fatigueMaterial);
						}
						if (preffasMaterial == null || preffasMaterial.isEmpty()) {
							uploadPilotPointInfo.setNull(15, java.sql.Types.VARCHAR);
						}
						else {
							uploadPilotPointInfo.setString(15, preffasMaterial);
						}
						if (linearMaterial == null || linearMaterial.isEmpty()) {
							uploadPilotPointInfo.setNull(16, java.sql.Types.VARCHAR);
						}
						else {
							uploadPilotPointInfo.setString(16, linearMaterial);
						}
						if (eid == null || eid.isEmpty()) {
							uploadPilotPointInfo.setNull(17, java.sql.Types.VARCHAR);
						}
						else {
							uploadPilotPointInfo.setString(17, eid);
						}
						uploadPilotPointInfo.executeUpdate();

						// get pilot point id
						long ppID = -1L;
						try (ResultSet resultSet = uploadPilotPointInfo.getGeneratedKeys()) {

							// return file ID
							resultSet.next();
							ppID = resultSet.getBigDecimal(1).longValue();
						}

						// add to ids
						ids.add(ppID);
					}
				}
			}
		}

		// return ids
		return ids;
	}
}
