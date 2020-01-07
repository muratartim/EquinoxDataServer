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
import equinox.dataServer.remote.data.HelpVideoInfo;
import equinox.dataServer.remote.data.HelpVideoInfo.HelpVideoInfoType;
import equinox.dataServer.remote.message.UploadHelpVideoRequest;
import equinox.dataServer.remote.message.UploadHelpVideoResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for upload help video task.
 *
 * @author Murat Artim
 * @date 2 Mar 2018
 * @time 11:58:46
 */
public class UploadHelpVideo extends DatabaseQueryTask {

	/**
	 * Creates upload help video task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public UploadHelpVideo(DataServer server, DataClient client, UploadHelpVideoRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for uploading help video.");

		// create response message
		UploadHelpVideoResponse response = new UploadHelpVideoResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		UploadHelpVideoRequest request = (UploadHelpVideoRequest) request_;
		HelpVideoInfo info = request.getInfo();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// update video info
				uploadVideoInfo(connection, info);

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
	 * Uploads video info to global database.
	 *
	 * @param info
	 *            Help video info.
	 * @param connection
	 *            Database connection.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void uploadVideoInfo(Connection connection, HelpVideoInfo info) throws Exception {

		// get video info
		String name = (String) info.getInfo(HelpVideoInfoType.NAME);
		String url = (String) info.getInfo(HelpVideoInfoType.DATA_URL);
		String duration = (String) info.getInfo(HelpVideoInfoType.DURATION);
		String description = (String) info.getInfo(HelpVideoInfoType.DESCRIPTION);
		long size = (Long) info.getInfo(HelpVideoInfoType.DATA_SIZE);

		// delete video (if exists)
		sendProgressMessage("Deleting video '" + name + "' from database...");
		try (Statement statement = connection.createStatement()) {

			// check if video exists
			boolean exists = false;
			String sql = "select data_url from videos where name = '" + name + "'";
			try (ResultSet resultSet = statement.executeQuery(sql)) {
				exists = resultSet.next();
			}

			// delete video info from database
			if (exists) {
				sql = "delete from videos where name = '" + name + "'";
				statement.executeUpdate(sql);
			}
		}

		// prepare insert info statement
		String sql = "insert into videos(name, description, duration, data_size, data_url) values(?, ?, ?, ?, ?)";
		try (PreparedStatement insertInfo = connection.prepareStatement(sql)) {

			// insert info
			insertInfo.setString(1, name);
			insertInfo.setString(2, description);
			insertInfo.setString(3, duration);
			insertInfo.setLong(4, size);
			insertInfo.setString(5, url);
			insertInfo.executeUpdate();
		}
	}
}
