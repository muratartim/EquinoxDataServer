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
import equinox.dataServer.remote.data.PilotPointImageType;
import equinox.dataServer.remote.data.PilotPointInfo;
import equinox.dataServer.remote.data.PilotPointInfo.PilotPointInfoType;
import equinox.dataServer.remote.message.DeletePilotPointRequest;
import equinox.dataServer.remote.message.DeletePilotPointResponse;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.FilerConnection;

/**
 * Class for delete pilot point task.
 *
 * @author Murat Artim
 * @date 9 Feb 2018
 * @time 11:38:58
 */
public class DeletePilotPoint extends DatabaseQueryTask {

	/**
	 * Creates delete pilot point task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public DeletePilotPoint(DataServer server, DataClient client, DeletePilotPointRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for deleting pilot point.");

		// create response message
		DeletePilotPointResponse response = new DeletePilotPointResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		DeletePilotPointRequest request = (DeletePilotPointRequest) request_;
		PilotPointInfo info = request.getPilotPointInfo();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {

					// delete pilot point
					deletePilotPoint(statement, info);

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
		response.setPilotPointDeleted(true);

		// respond to client
		client_.sendMessage(response);
	}

	/**
	 * Deletes pilot point from global database.
	 *
	 * @param statement
	 *            Database statement.
	 * @param info
	 *            Pilot point info.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void deletePilotPoint(Statement statement, PilotPointInfo info) throws Exception {

		// get data URL
		sendProgressMessage("Getting pilot point data URL from global database...");
		String url = null;
		String sql = "select data_url from pilot_point_data where id = " + (long) info.getInfo(PilotPointInfoType.ID);
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				url = resultSet.getString("data_url");
			}
		}

		// delete data from database
		sendProgressMessage("Deleting pilot point data from global database...");
		sql = "delete from pilot_point_data where id = " + (long) info.getInfo(PilotPointInfoType.ID);
		statement.executeUpdate(sql);

		// get filer connection
		try (FilerConnection filer = getFilerConnection()) {

			// delete data from filer
			sendProgressMessage("Deleting pilot point data from filer...");
			if (filer.fileExists(url)) {
				filer.getSftpChannel().rm(url);
			}

			// get attribute URL
			sendProgressMessage("Getting pilot point attributes URL from global database...");
			url = null;
			sql = "select data_url from pilot_point_attribute where id = " + (long) info.getInfo(PilotPointInfoType.ID);
			try (ResultSet resultSet = statement.executeQuery(sql)) {
				while (resultSet.next()) {
					url = resultSet.getString("data_url");
				}
			}

			// delete attribute from filer
			if (url != null) {
				sendProgressMessage("Deleting pilot point attribute from filer...");
				if (filer.fileExists(url)) {
					filer.getSftpChannel().rm(url);
				}
			}

			// delete attribute from database
			sendProgressMessage("Deleting pilot point attribute from global database...");
			sql = "delete from pilot_point_attribute where id = " + (long) info.getInfo(PilotPointInfoType.ID);
			statement.executeUpdate(sql);

			// loop over pilot point images
			for (PilotPointImageType imageType : PilotPointImageType.values()) {

				// get image URL
				sendProgressMessage("Getting pilot point image URL for '" + imageType.getPageName() + "' from global database...");
				url = null;
				sql = "select image_url from " + imageType.getTableName() + " where id = " + (long) info.getInfo(PilotPointInfoType.ID);
				try (ResultSet resultSet = statement.executeQuery(sql)) {
					while (resultSet.next()) {
						url = resultSet.getString("image_url");
					}
				}

				// delete image from filer
				if (url != null) {
					sendProgressMessage("Deleting pilot point image for '" + imageType.getPageName() + "' from filer...");
					if (filer.fileExists(url)) {
						filer.getSftpChannel().rm(url);
					}
				}

				// delete image from database
				sendProgressMessage("Deleting pilot point image for '" + imageType.getPageName() + "' from global database...");
				sql = "delete from " + imageType.getTableName() + " where id = " + (long) info.getInfo(PilotPointInfoType.ID);
				statement.executeUpdate(sql);
			}
		}

		// delete pilot point info
		sendProgressMessage("Deleting pilot point info from global database...");
		sql = "delete from pilot_points where id = " + (long) info.getInfo(PilotPointInfoType.ID);
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
	 *            Pilot point info.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void deleteConnectedMultiplicationTables(Statement statement, Connection connection, PilotPointInfo info) throws Exception {

		// get connection to filer
		sendProgressMessage("Deleting connected multiplication table data from filer...");
		try (FilerConnection filer = getFilerConnection()) {

			// get multiplication table IDs
			String sql = "select data_url from mult_tables where pilot_point_name = '" + (String) info.getInfo(PilotPointInfoType.NAME) + "' and spectrum_name = '" + (String) info.getInfo(PilotPointInfoType.SPECTRUM_NAME) + "'";
			try (ResultSet resultSet = statement.executeQuery(sql)) {
				while (resultSet.next()) {
					String url = resultSet.getString("data_url");
					if (filer.fileExists(url)) {
						filer.getSftpChannel().rm(url);
					}
				}
			}
		}

		// delete connected multiplication table info
		sendProgressMessage("Deleting connected multiplication table info from global database...");
		String sql = "delete from mult_tables where pilot_point_name = '" + (String) info.getInfo(PilotPointInfoType.NAME) + "' and spectrum_name = '" + (String) info.getInfo(PilotPointInfoType.SPECTRUM_NAME) + "'";
		statement.executeUpdate(sql);
	}
}
