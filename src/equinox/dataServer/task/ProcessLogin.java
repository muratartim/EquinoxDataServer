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
import java.util.logging.Level;

import decoder.Base64Decoder;
import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.Login;
import equinox.dataServer.remote.message.LoginFailed;
import equinox.dataServer.remote.message.LoginSuccessful;
import equinox.serverUtilities.Permission;
import equinox.serverUtilities.PermissionDenied;

/**
 * Class for process login task.
 *
 * @author Murat Artim
 * @date 20 Dec 2017
 * @time 15:51:10
 */
public class ProcessLogin extends ServerTask {

	/** Requesting client. */
	private final DataClient client_;

	/** Request message. */
	private final Login message_;

	/**
	 * Creates process login task.
	 *
	 * @param client
	 *            Requesting client.
	 * @param message
	 *            Login message.
	 */
	public ProcessLogin(DataClient client, Login message) {
		super(client.getLobby().getServer());
		client_ = client;
		message_ = message;
	}

	@Override
	protected void runTask() throws Exception {

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// check password
				String password = null;
				String sql = "select password from admins where user_id = " + client_.getID();
				try (ResultSet resultSet = statement.executeQuery(sql)) {
					if (resultSet.next()) {
						password = Base64Decoder.decodeString(resultSet.getString("password"));
					}
				}

				// not administrator
				if (password == null) {
					client_.sendMessage(new PermissionDenied(Permission.LOGIN_AS_ADMINISTRATOR));
					return;
				}

				// decode password
				String suppliedPassword = Base64Decoder.decodeString(message_.getPassword());

				// login failed
				if (suppliedPassword == null || suppliedPassword.trim().isEmpty()) {
					client_.sendMessage(new LoginFailed());
					return;
				}

				// login failed
				if (!suppliedPassword.equals(password)) {
					client_.sendMessage(new LoginFailed());
					return;
				}

				// login successful
				LoginSuccessful response = new LoginSuccessful();

				// extract administrative permissions from database and add to client
				sql = "select name from permissions where user_id = " + client_.getID() + " and is_admin = 1";
				try (ResultSet resultSet = statement.executeQuery(sql)) {

					// loop over permissions
					while (resultSet.next()) {

						// get permission name
						String permissionName = resultSet.getString("name");

						// add permission to client and response message
						try {

							// get permission
							Permission permission = Permission.valueOf(permissionName);

							// not found
							if (permission == null) {
								continue;
							}

							// add permission to client and response message
							client_.addPermission(permission);
							response.addAdminPermissionName(permissionName);
						}

						// permission not recognized
						catch (Exception e) {
							continue;
						}
					}
				}

				// respond to client
				client_.sendMessage(response);
			}
		}
	}

	@Override
	protected void failed(Exception e) {
		server_.getLogger().log(Level.WARNING, "Exception occurred during responding to administrator login request.", e);
		client_.sendMessage(new LoginFailed());
		server_.incrementFailedQueries();
	}
}
