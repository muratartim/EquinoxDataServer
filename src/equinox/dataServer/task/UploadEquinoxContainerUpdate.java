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
import java.sql.Timestamp;
import java.util.ArrayList;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.EquinoxUpdate;
import equinox.dataServer.remote.data.EquinoxUpdate.EquinoxUpdateInfoType;
import equinox.dataServer.remote.message.UploadContainerUpdateRequest;
import equinox.dataServer.remote.message.UploadContainerUpdateResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for upload Equinox container update task.
 *
 * @author Murat Artim
 * @date 15 Mar 2018
 * @time 23:31:19
 */
public class UploadEquinoxContainerUpdate extends DatabaseQueryTask {

	/**
	 * Creates upload Equinox container update task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public UploadEquinoxContainerUpdate(DataServer server, DataClient client, UploadContainerUpdateRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for uploading Equinox Container update.");

		// create response message
		UploadContainerUpdateResponse response = new UploadContainerUpdateResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		UploadContainerUpdateRequest request = (UploadContainerUpdateRequest) request_;
		ArrayList<EquinoxUpdate> updates = request.getUpdates();
		String versionDescription = request.getVersionDescription();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// check whether version already exists
				checkVersion(connection, updates.get(0));

				// prepare statement for inserting new versions
				String sql = "insert into containers(version_number, version_description, upload_date, os_type, os_arch, data_size, data_url) values(?, ?, ?, ?, ?, ?, ?)";
				try (PreparedStatement insert = connection.prepareStatement(sql)) {
					for (EquinoxUpdate update : updates) {
						insert.setDouble(1, (double) update.getInfo(EquinoxUpdateInfoType.VERSION_NUMBER));
						insert.setString(2, versionDescription);
						insert.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
						insert.setString(4, (String) update.getInfo(EquinoxUpdateInfoType.OS_TYPE));
						insert.setString(5, (String) update.getInfo(EquinoxUpdateInfoType.OS_ARCH));
						insert.setLong(6, (long) update.getInfo(EquinoxUpdateInfoType.DATA_SIZE));
						insert.setString(7, (String) update.getInfo(EquinoxUpdateInfoType.DATA_URL));
						insert.executeUpdate();
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
	 * Checks if given version already exists in the global database.
	 *
	 * @param connection
	 *            Database connection.
	 * @param update
	 *            Equinox update.
	 * @throws Exception
	 *             If given version number already exists in the global database.
	 */
	private static void checkVersion(Connection connection, EquinoxUpdate update) throws Exception {
		try (Statement statement = connection.createStatement()) {
			double versionNumber = (double) update.getInfo(EquinoxUpdateInfoType.VERSION_NUMBER);
			String sql = "select id from containers where version_number = " + versionNumber;
			try (ResultSet resultSet = statement.executeQuery(sql)) {
				if (resultSet.next()) {
					String message = "Equinox Container update package for version '" + versionNumber + "' already exists in the global database. ";
					message += "Upload aborted.";
					throw new Exception(message);
				}
			}
		}
	}
}
