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
import equinox.dataServer.remote.data.PilotPointInfo;
import equinox.dataServer.remote.data.PilotPointInfo.PilotPointInfoType;
import equinox.dataServer.remote.message.SavePilotPointImageRequest;
import equinox.dataServer.remote.message.SavePilotPointImageResponse;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.FilerConnection;

/**
 * Class for save pilot point image task.
 *
 * @author Murat Artim
 * @date 21 Feb 2018
 * @time 14:47:26
 */
public class SavePilotPointImage extends DatabaseQueryTask {

	/**
	 * Creates save pilot point image task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public SavePilotPointImage(DataServer server, DataClient client, SavePilotPointImageRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for saving pilot point image.");

		// create response message
		SavePilotPointImageResponse response = new SavePilotPointImageResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		SavePilotPointImageRequest request = (SavePilotPointImageRequest) request_;
		PilotPointInfo info = request.getPilotPointInfo();
		PilotPointImageType imageType = request.getImageType();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// get image URL
				String imageUrl = getImageUrl(connection, info, imageType);

				// no image exists
				if (imageUrl == null) {

					// delete requested
					if (request.isDelete()) {
						response.setUploadToFiler(false);
						response.setRemoveFromFiler(false);
						response.setImageUrl(imageUrl);
					}

					// insert
					else {
						imageUrl = createUrl(info, imageType);
						insertImageUrl(connection, info, imageType, imageUrl);
						response.setUploadToFiler(true);
						response.setRemoveFromFiler(false);
						response.setImageUrl(imageUrl);
					}
				}

				// image exists
				else {

					// delete requested
					if (request.isDelete()) {
						deleteImageImageUrl(connection, info, imageType);
						response.setUploadToFiler(false);
						response.setRemoveFromFiler(true);
						response.setImageUrl(imageUrl);
					}

					// update
					else {
						response.setUploadToFiler(true);
						response.setRemoveFromFiler(true);
						response.setImageUrl(imageUrl);
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

		// respond to client
		client_.sendMessage(response);
	}

	/**
	 * Deletes image URL from database.
	 *
	 * @param connection
	 *            Database connection.
	 * @param info
	 *            Pilot point info.
	 * @param imageType
	 *            Pilot point image type.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void deleteImageImageUrl(Connection connection, PilotPointInfo info, PilotPointImageType imageType) throws Exception {
		try (Statement statement = connection.createStatement()) {
			String sql = "delete from " + imageType.getTableName() + " where id = " + (long) info.getInfo(PilotPointInfoType.ID);
			statement.executeUpdate(sql);
		}
	}

	/**
	 * Returns image URL, or null if no image found.
	 *
	 * @param connection
	 *            Database connection.
	 * @param info
	 *            Pilot point info.
	 * @param imageType
	 *            Pilot point image type.
	 * @return Image URL, or null if no image found.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static String getImageUrl(Connection connection, PilotPointInfo info, PilotPointImageType imageType) throws Exception {
		try (Statement statement = connection.createStatement()) {
			String sql = "select image_url from " + imageType.getTableName() + " where id = " + (long) info.getInfo(PilotPointInfoType.ID);
			try (ResultSet resultSet = statement.executeQuery(sql)) {
				if (resultSet.next())
					return resultSet.getString("image_url");
			}
		}
		return null;
	}

	/**
	 * Creates and returns image URL.
	 *
	 * @param info
	 *            Pilot point info.
	 * @param imageType
	 *            Pilot point image type.
	 * @return The newly created image URL.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private String createUrl(PilotPointInfo info, PilotPointImageType imageType) throws Exception {

		// upload image to filer
		String imageUrl = null;
		try (FilerConnection filer = getFilerConnection()) {

			// get pilot point info
			String name = (String) info.getInfo(PilotPointInfoType.NAME);
			String program = (String) info.getInfo(PilotPointInfoType.AC_PROGRAM);
			String section = (String) info.getInfo(PilotPointInfoType.AC_SECTION);
			String mission = (String) info.getInfo(PilotPointInfoType.FAT_MISSION);

			// create directories
			String dir = filer.createDirectories(filer.getDirectoryPath(imageType.getFilerDirectoryName()), program, section, mission);

			// set path to destination file
			imageUrl = dir + "/" + name + ".png";
		}

		// return image URL
		return imageUrl;
	}

	/**
	 * Inserts image URL into global database.
	 *
	 * @param connection
	 *            Database connection.
	 * @param info
	 *            Image info.
	 * @param imageType
	 *            Image type.
	 * @param imageUrl
	 *            Image URL.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void insertImageUrl(Connection connection, PilotPointInfo info, PilotPointImageType imageType, String imageUrl) throws Exception {
		String sql = "insert into " + imageType.getTableName() + "(id, image_url) values(?, ?)";
		try (PreparedStatement update = connection.prepareStatement(sql)) {
			update.setLong(1, (long) info.getInfo(PilotPointInfoType.ID));
			update.setString(2, imageUrl);
			update.executeUpdate();
		}
	}
}
