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
import equinox.dataServer.remote.data.ServerPluginInfo;
import equinox.dataServer.remote.data.ServerPluginInfo.PluginInfoType;
import equinox.dataServer.remote.message.UploadPluginRequest;
import equinox.dataServer.remote.message.UploadPluginResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for upload plugin task.
 *
 * @author Murat Artim
 * @date 15 Mar 2018
 * @time 11:43:41
 */
public class UploadPlugin extends DatabaseQueryTask {

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
	public UploadPlugin(DataServer server, DataClient client, UploadPluginRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for uploading Equinox plugin.");

		// create response message
		UploadPluginResponse response = new UploadPluginResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		UploadPluginRequest request = (UploadPluginRequest) request_;
		ServerPluginInfo info = request.getPluginInfo();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {

					// get plugin version
					double version = getPluginVersion(statement, info);

					// delete plugin (if exists)
					if (version != -1.0) {
						deletePlugin(statement, info);
					}

					// insert plugin
					insertPlugin(connection, info);
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
	 * Inserts the plugin to database.
	 *
	 * @param connection
	 *            Database connection.
	 * @param info
	 *            Server plugin info.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void insertPlugin(Connection connection, ServerPluginInfo info) throws Exception {
		String sql = "insert into plugins(name, jar_name, description, version_number, image_url, data_size, developer_name, developer_email, data_url) values(?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement insertPluginInfo = connection.prepareStatement(sql)) {
			insertPluginInfo.setString(1, (String) info.getInfo(PluginInfoType.NAME));
			insertPluginInfo.setString(2, (String) info.getInfo(PluginInfoType.JAR_NAME));
			insertPluginInfo.setString(3, (String) info.getInfo(PluginInfoType.DESCRIPTION));
			insertPluginInfo.setDouble(4, (double) info.getInfo(PluginInfoType.VERSION_NUMBER));
			insertPluginInfo.setString(5, (String) info.getInfo(PluginInfoType.IMAGE_URL));
			insertPluginInfo.setLong(6, (long) info.getInfo(PluginInfoType.DATA_SIZE));
			insertPluginInfo.setString(7, (String) info.getInfo(PluginInfoType.DEVELOPER_NAME));
			insertPluginInfo.setString(8, (String) info.getInfo(PluginInfoType.DEVELOPER_EMAIL));
			insertPluginInfo.setString(9, (String) info.getInfo(PluginInfoType.DATA_URL));
			insertPluginInfo.executeUpdate();
		}
	}

	/**
	 * Deletes plugin from filer and database.
	 *
	 * @param statement
	 *            Database statement.
	 * @param info
	 *            Server plugin info.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void deletePlugin(Statement statement, ServerPluginInfo info) throws Exception {
		sendProgressMessage("Deleting plugin '" + (String) info.getInfo(PluginInfoType.NAME) + "' from database...");
		String sql = "delete from plugins where name = '" + (String) info.getInfo(PluginInfoType.NAME) + "'";
		statement.executeUpdate(sql);
	}

	/**
	 * Returns plugin version number, or -1.0 if plugin doesn't exist.
	 *
	 * @param statement
	 *            Database statement.
	 * @param info
	 *            Server plugin info.
	 * @return Version number of plugin if it exists, or -1.0 if not.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static double getPluginVersion(Statement statement, ServerPluginInfo info) throws Exception {
		String sql = "select version_number from plugins where name = '" + (String) info.getInfo(PluginInfoType.NAME) + "'";
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next())
				return resultSet.getDouble("version_number");
		}
		return -1.0;
	}
}
