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

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.PilotPointImageType;
import equinox.dataServer.remote.data.SpectrumInfo;
import equinox.dataServer.remote.data.SpectrumInfo.SpectrumInfoType;
import equinox.dataServer.remote.message.DeleteSpectrumRequest;
import equinox.dataServer.remote.message.DeleteSpectrumResponse;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.FilerConnection;

/**
 * Class for delete spectrum task.
 *
 * @author Murat Artim
 * @date 10 Feb 2018
 * @time 00:06:55
 */
public class DeleteSpectrum extends DatabaseQueryTask {

	/**
	 * Creates delete spectrum task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public DeleteSpectrum(DataServer server, DataClient client, DeleteSpectrumRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for deleting spectrum.");

		// create response message
		DeleteSpectrumResponse response = new DeleteSpectrumResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		DeleteSpectrumRequest request = (DeleteSpectrumRequest) request_;
		SpectrumInfo info = request.getSpectrumInfo();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {

					// delete spectrum
					deleteSpectrum(statement, info);

					// delete connected pilot points
					deleteConnectedPilotPoints(statement, connection, info);

					// delete connected multiplication tables
					deleteConnectedMultiplicationTables(statement, connection, info);
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

		// video deleted
		response.setSpectrumDeleted(true);

		// respond to client
		client_.sendMessage(response);
	}

	/**
	 * Deletes spectrum from global database.
	 *
	 * @param statement
	 *            Database statement.
	 * @param info
	 *            Spectrum info.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void deleteSpectrum(Statement statement, SpectrumInfo info) throws Exception {

		// delete spectrum data from filer
		sendProgressMessage("Deleting spectrum data from filer...");
		String url = (String) info.getInfo(SpectrumInfoType.DATA_URL);
		try (FilerConnection filer = getFilerConnection()) {
			if (filer.fileExists(url)) {
				filer.getSftpChannel().rm(url);
			}
		}

		// delete spectrum info
		sendProgressMessage("Deleting spectrum info from global database...");
		statement.executeUpdate("delete from spectra where id = " + (long) info.getInfo(SpectrumInfoType.ID));
	}

	/**
	 * Deletes connected pilot points from global database.
	 *
	 * @param statement
	 *            Database statement.
	 * @param connection
	 *            Database connection.
	 * @param info
	 *            Spectrum info.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void deleteConnectedPilotPoints(Statement statement, Connection connection, SpectrumInfo info) throws Exception {

		// progress info
		sendProgressMessage("Deleting connected pilot point data and images from global database...");

		// get connection to filer
		try (FilerConnection filer = getFilerConnection()) {

			// prepare statement to get pilot point data URL
			String sql = "select data_url from pilot_point_data where id = ?";
			try (PreparedStatement getPilotPointDataUrl = connection.prepareStatement(sql)) {

				// prepare statement to delete pilot point data
				sql = "delete from pilot_point_data where id = ?";
				try (PreparedStatement deletePilotPointData = connection.prepareStatement(sql)) {

					// prepare statement to get pilot point attribute URL
					sql = "select data_url from pilot_point_attribute where id = ?";
					try (PreparedStatement getPilotPointAttributeUrl = connection.prepareStatement(sql)) {

						// prepare statement to delete pilot point attribute
						sql = "delete from pilot_point_attribute where id = ?";
						try (PreparedStatement deletePilotPointAttribute = connection.prepareStatement(sql)) {

							// create statement
							try (Statement statement2 = connection.createStatement()) {

								// get pilot point IDs
								sql = "select id from pilot_points where spectrum_name = '" + (String) info.getInfo(SpectrumInfoType.NAME) + "'";
								try (ResultSet ppIDs = statement.executeQuery(sql)) {

									// loop over pilot point IDs
									while (ppIDs.next()) {

										// delete pilot point data
										long id = ppIDs.getLong("id");

										// get pilot point data URL
										String url = null;
										getPilotPointDataUrl.setLong(1, id);
										try (ResultSet ppDataUrls = getPilotPointDataUrl.executeQuery()) {
											while (ppDataUrls.next()) {
												url = ppDataUrls.getString("data_url");
											}
										}

										// delete pilot point data from filer
										if (url != null) {
											if (filer.fileExists(url)) {
												filer.getSftpChannel().rm(url);
											}
										}

										// delete pilot point data from database
										deletePilotPointData.setLong(1, id);
										deletePilotPointData.executeUpdate();

										// get pilot point attribute URL
										url = null;
										getPilotPointAttributeUrl.setLong(1, id);
										try (ResultSet ppAttributeUrls = getPilotPointAttributeUrl.executeQuery()) {
											while (ppAttributeUrls.next()) {
												url = ppAttributeUrls.getString("data_url");
											}
										}

										// delete pilot point attribute from filer
										if (url != null) {
											if (filer.fileExists(url)) {
												filer.getSftpChannel().rm(url);
											}
										}

										// delete pilot point attribute from database
										deletePilotPointAttribute.setLong(1, id);
										deletePilotPointAttribute.executeUpdate();

										// delete pilot point images
										for (PilotPointImageType imageType : PilotPointImageType.values()) {

											// get pilot point image URL
											url = null;
											sql = "select image_url from " + imageType.getTableName() + " where id = " + id;
											try (ResultSet ppImageUrls = statement2.executeQuery(sql)) {
												while (ppImageUrls.next()) {
													url = ppImageUrls.getString("image_url");
												}
											}

											// delete pilot point image from filer
											if (url != null) {
												if (filer.fileExists(url)) {
													filer.getSftpChannel().rm(url);
												}
											}

											// delete pilot point image from database
											sql = "delete from " + imageType.getTableName() + " where id = " + id;
											statement2.executeUpdate(sql);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		// delete connected pilot point info
		sendProgressMessage("Deleting connected pilot point info from global database...");
		String sql = "delete from pilot_points where spectrum_name = '" + (String) info.getInfo(SpectrumInfoType.NAME) + "'";
		statement.executeUpdate(sql);
	}

	/**
	 * Deletes connected multiplication tables from global database.
	 *
	 * @param statement
	 *            Database statement.
	 * @param connection
	 *            Database connection.
	 * @param info
	 *            Spectrum info.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void deleteConnectedMultiplicationTables(Statement statement, Connection connection, SpectrumInfo info) throws Exception {

		// get connection to filer
		sendProgressMessage("Deleting connected multiplication table data from filer...");
		try (FilerConnection filer = getFilerConnection()) {

			// get multiplication table IDs
			String sql = "select data_url from mult_tables where spectrum_name = '" + (String) info.getInfo(SpectrumInfoType.NAME) + "'";
			try (ResultSet resultSet = statement.executeQuery(sql)) {
				while (resultSet.next()) {
					String url = resultSet.getString("data_url");
					if (url != null) {
						if (filer.fileExists(url)) {
							filer.getSftpChannel().rm(url);
						}
					}
				}
			}
		}

		// delete connected multiplication table info
		sendProgressMessage("Deleting connected multiplication table info from global database...");
		String sql = "delete from mult_tables where spectrum_name = '" + (String) info.getInfo(SpectrumInfoType.NAME) + "'";
		statement.executeUpdate(sql);
	}
}
