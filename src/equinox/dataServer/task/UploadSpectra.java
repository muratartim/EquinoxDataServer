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
import equinox.dataServer.remote.data.SpectrumInfo;
import equinox.dataServer.remote.data.SpectrumInfo.SpectrumInfoType;
import equinox.dataServer.remote.message.UploadSpectraRequest;
import equinox.dataServer.remote.message.UploadSpectraResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for upload spectra task.
 *
 * @author Murat Artim
 * @date 15 Mar 2018
 * @time 21:42:25
 */
public class UploadSpectra extends DatabaseQueryTask {

	/**
	 * Creates upload spectra task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public UploadSpectra(DataServer server, DataClient client, UploadSpectraRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for uploading spectra.");

		// create response message
		UploadSpectraResponse response = new UploadSpectraResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		UploadSpectraRequest request = (UploadSpectraRequest) request_;
		ArrayList<SpectrumInfo> infos = request.getSpectrumInfo();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// prepare statement for checking spectrum
				String sql = "select cdf_issue, delivery_ref from spectra where name = ?";
				try (PreparedStatement checkSpectrum = connection.prepareStatement(sql)) {

					// prepare statement for uploading spectrum info
					sql = "insert into spectra(name, ac_program, ac_section, fat_mission, fat_mission_issue, flp_issue, ";
					sql += "iflp_issue, cdf_issue, delivery_ref, description, data_size, data_url) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
					try (PreparedStatement uploadSpectrumInfo = connection.prepareStatement(sql)) {

						// upload spectra
						for (SpectrumInfo info : infos) {
							uploadSpectra(checkSpectrum, uploadSpectrumInfo, info);
						}
					}
				}

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
	 * Performs spectrum upload.
	 *
	 * @param checkSpectrum
	 *            Database statement for checking spectrum.
	 * @param uploadSpectrumInfo
	 *            Database statement for uploading spectrum info.
	 * @param info
	 *            Spectrum info.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void uploadSpectra(PreparedStatement checkSpectrum, PreparedStatement uploadSpectrumInfo, SpectrumInfo info) throws Exception {

		// get spectrum info
		String spectrumName = (String) info.getInfo(SpectrumInfoType.NAME);
		String program = (String) info.getInfo(SpectrumInfoType.AC_PROGRAM);
		String section = (String) info.getInfo(SpectrumInfoType.AC_SECTION);
		String mission = (String) info.getInfo(SpectrumInfoType.FAT_MISSION);
		String missionIssue = (String) info.getInfo(SpectrumInfoType.FAT_MISSION_ISSUE);
		String flpIssue = (String) info.getInfo(SpectrumInfoType.FLP_ISSUE);
		String iflpIssue = (String) info.getInfo(SpectrumInfoType.IFLP_ISSUE);
		String cdfIssue = (String) info.getInfo(SpectrumInfoType.CDF_ISSUE);
		String delRef = (String) info.getInfo(SpectrumInfoType.DELIVERY_REF);
		String description = (String) info.getInfo(SpectrumInfoType.DESCRIPTION);
		long dataSize = (long) info.getInfo(SpectrumInfoType.DATA_SIZE);
		String dataUrl = (String) info.getInfo(SpectrumInfoType.DATA_URL);

		// check values
		if (spectrumName.isEmpty() || program.isEmpty() || section.isEmpty() || mission.isEmpty() || missionIssue.isEmpty() || flpIssue.isEmpty() || iflpIssue.isEmpty() || cdfIssue.isEmpty() || dataUrl.isEmpty()) {
			String message = "The spectrum upload information has missing entries. Aborting operation.";
			throw new Exception(message);
		}

		// check spectrum
		checkSpectrum.setString(1, spectrumName);
		try (ResultSet resultSet = checkSpectrum.executeQuery()) {
			while (resultSet.next()) {

				// get data
				String cdfIssue2 = resultSet.getString("cdf_issue");
				String delRef2 = resultSet.getString("delivery_ref");

				// create error message
				String message = "Spectrum '" + spectrumName + "' already exists in the global database, with CDF issue '" + cdfIssue2;
				message += "' and delivery document reference '" + delRef2 + "'. ";
				message += "If applicable, please first delete the existing spectrum and then upload the new spectrum. Aborting operation.";

				// throw exception
				throw new Exception(message);
			}
		}

		// upload spectrum info
		uploadSpectrumInfo.setString(1, spectrumName);
		uploadSpectrumInfo.setString(2, program);
		uploadSpectrumInfo.setString(3, section);
		uploadSpectrumInfo.setString(4, mission);
		uploadSpectrumInfo.setString(5, missionIssue);
		uploadSpectrumInfo.setString(6, flpIssue);
		uploadSpectrumInfo.setString(7, iflpIssue);
		uploadSpectrumInfo.setString(8, cdfIssue);
		uploadSpectrumInfo.setString(9, delRef == null || delRef.isEmpty() ? "DRAFT" : delRef);
		if (description == null || description.isEmpty()) {
			uploadSpectrumInfo.setNull(10, java.sql.Types.VARCHAR);
		}
		else {
			uploadSpectrumInfo.setString(10, description);
		}
		uploadSpectrumInfo.setLong(11, dataSize);
		uploadSpectrumInfo.setString(12, dataUrl);
		uploadSpectrumInfo.executeUpdate();
	}
}
