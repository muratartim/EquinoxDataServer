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
import java.util.ArrayList;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.DeleteUsersRequest;
import equinox.dataServer.remote.message.DeleteUsersResponse;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.FilerConnection;

/**
 * Class for delete users task.
 *
 * @author Murat Artim
 * @date 4 Apr 2018
 * @time 18:21:52
 */
public class DeleteUsers extends DatabaseQueryTask {

	/**
	 * Creates delete users task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public DeleteUsers(DataServer server, DataClient client, DeleteUsersRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for deleting users.");

		// create response message
		DeleteUsersResponse response = new DeleteUsersResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		DeleteUsersRequest request = (DeleteUsersRequest) request_;
		String[] aliases = request.getAliases();

		// check aliases
		checkAliases(aliases);

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// create statement
				try (Statement statement = connection.createStatement()) {

					// create array of ids and image urls
					ArrayList<Long> ids = new ArrayList<>();
					ArrayList<String> urls = new ArrayList<>();

					// get user ids
					getUserInfo(statement, aliases, ids, urls);

					// check if there is any admin
					checkForAdmin(ids, statement);

					// delete permissions
					deletePermissions(ids, statement);

					// delete users
					deleteUsers(ids, statement);

					// delete user profile images
					deleteProfileImages(urls);
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

		// users deleted
		response.setUsersDeleted(true);

		// respond to client
		client_.sendMessage(response);
	}

	/**
	 * Deletes user profile images.
	 *
	 * @param urls
	 *            User profile image urls.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void deleteProfileImages(ArrayList<String> urls) throws Exception {

		// no user image found
		if (urls == null || urls.isEmpty())
			return;

		// get connection to filer
		try (FilerConnection filer = getFilerConnection()) {

			// delete images
			for (String url : urls) {
				if (filer.fileExists(url)) {
					filer.getSftpChannel().rm(url);
				}
			}
		}
	}

	/**
	 * Deletes users for given ids.
	 *
	 * @param ids
	 *            User ids.
	 * @param statement
	 *            Database statement.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void deleteUsers(ArrayList<Long> ids, Statement statement) throws Exception {

		// build sql
		String sql = "delete from users where ";
		for (long id : ids) {
			sql += "id = " + id + " or ";
		}
		sql = sql.substring(0, sql.lastIndexOf(" or "));

		// execute
		statement.executeUpdate(sql);
	}

	/**
	 * Deletes user permissions connected to given users.
	 *
	 * @param ids
	 *            User ids.
	 * @param statement
	 *            Database statement.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void deletePermissions(ArrayList<Long> ids, Statement statement) throws Exception {

		// build sql
		String sql = "delete from permissions where ";
		for (long id : ids) {
			sql += "user_id = " + id + " or ";
		}
		sql = sql.substring(0, sql.lastIndexOf(" or "));

		// execute
		statement.executeUpdate(sql);
	}

	/**
	 * Checks if there is any administrator id in given list.
	 *
	 * @param ids
	 *            User ids.
	 * @param statement
	 *            Database statement.
	 * @throws Exception
	 *             If check fails.
	 */
	private static void checkForAdmin(ArrayList<Long> ids, Statement statement) throws Exception {

		// build sql
		String sql = "select id from admins where ";
		for (long id : ids) {
			sql += "user_id = " + id + " or ";
		}
		sql = sql.substring(0, sql.lastIndexOf(" or "));

		// get ids
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			if (resultSet.next())
				throw new Exception("Cannot delete administrator with 'Delete User' operation. Operation aborted.");
		}
	}

	/**
	 * Returns user ids and image urls.
	 *
	 * @param statement
	 *            Database statement.
	 * @param aliases
	 *            User aliases.
	 * @param ids
	 *            User ids.
	 * @param urls
	 *            User profile image urls.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void getUserInfo(Statement statement, String[] aliases, ArrayList<Long> ids, ArrayList<String> urls) throws Exception {

		// build sql
		String sql = "select id, image_url from users where ";
		for (String a : aliases) {
			sql += "alias = '" + a.trim() + "' or ";
		}
		sql = sql.substring(0, sql.lastIndexOf(" or "));

		// get ids
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				ids.add(resultSet.getLong("id"));
				String imageUrl = resultSet.getString("image_url");
				if (imageUrl != null) {
					urls.add(imageUrl);
				}
			}
		}

		// no ids found
		if (ids.isEmpty())
			throw new Exception("No users found for given aliases. Operation 'Delete users' aborted.");
	}

	/**
	 * Checks user aliases.
	 *
	 * @param aliases
	 *            User aliases.
	 * @throws Exception
	 *             If check fails.
	 */
	private static void checkAliases(String[] aliases) throws Exception {

		// no alias supplied
		if (aliases == null || aliases.length == 0)
			throw new Exception("No user alias supplied for deleting users.");

		// loop over aliases
		for (String a : aliases) {
			if (a == null || a.trim().isEmpty())
				throw new Exception("Invalid user alias supplied for deleting user.");
		}
	}
}
