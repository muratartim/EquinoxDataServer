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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import equinox.dataServer.client.ClientConnection;
import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.HandshakeWithDataServer;
import equinox.dataServer.server.DataServer;
import equinox.serverUtilities.Permission;

/**
 * Class for process handshake task.
 *
 * @author Murat Artim
 * @date 20 Dec 2017
 * @time 13:34:52
 */
public class ProcessHandshake extends ServerTask {

	/** Client connection. */
	private final ClientConnection clientConnection_;

	/** Handshake message. */
	private final HandshakeWithDataServer message_;

	/**
	 * Creates process handshake task.
	 *
	 * @param clientConnection
	 *            Client connection.
	 * @param message
	 *            Handshake message.
	 * @param server
	 *            Server instance.
	 */
	public ProcessHandshake(ClientConnection clientConnection, HandshakeWithDataServer message, DataServer server) {
		super(server);
		clientConnection_ = clientConnection;
		message_ = message;
	}

	@Override
	protected void runTask() throws Exception {

		// initialize client
		DataClient client = null;

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// get client id and username
				client = getClient(statement);

				// no client found (create anonymous client)
				if (client == null) {
					client = createAnonymousClient();
				}

				// client found
				else {
					setPermissions(client, statement);
				}
			}
		}

		// add client to server
		server_.addClient(client);

		// set timeout for connection.
		clientConnection_.setTimeout(Integer.parseInt(server_.getProperties().getProperty("ns.connectionTimeout")));

		// set message parameters
		message_.setUsername(client.getUsername());
		message_.setReply(true);

		// respond to client
		client.sendMessage(message_);
	}

	private void setPermissions(DataClient client, Statement statement) throws SQLException {

		// check if user is administrator
		String sql = "select id from admins where user_id = " + client.getID();
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			message_.setAsAdministrator(resultSet.next());
		}

		// extract non-administrative permissions from database and add to client
		sql = "select name from permissions where user_id = " + client.getID() + " and is_admin = 0";
		try (ResultSet resultSet = statement.executeQuery(sql)) {

			// loop over permission names
			while (resultSet.next()) {

				// get permission name
				String permissionName = resultSet.getString("name");

				// add permission to client and response message
				try {

					// get permission
					Permission permission = Permission.valueOf(permissionName);

					// doesn't exist
					if (permission == null) {
						continue;
					}

					// add permission to client and response message
					client.addPermission(permission);
					message_.addPermissionName(permissionName);
				}

				// permission not recognized (ignore)
				catch (Exception e) {
					continue;
				}
			}
		}
	}

	private DataClient createAnonymousClient() {
		DataClient client = null;
		long id = System.currentTimeMillis();
		String username = "anonymous_" + id;
		client = new DataClient(clientConnection_, id, message_.getAlias(), username, server_.getLobby());
		for (Permission permission : Permission.values()) {
			if (permission.isAdminPermission()) {
				continue;
			}
			message_.setAsAdministrator(false);
			client.addPermission(permission);
			message_.addPermissionName(permission.name());
		}
		return client;
	}

	/**
	 * Queries and returns client.
	 *
	 * @param statement
	 *            Database statement.
	 * @return The client or null.
	 * @throws SQLException
	 *             If exception occurs during process.
	 */
	private DataClient getClient(Statement statement) throws SQLException {
		DataClient client = null;
		String sql = "select id, username from users where alias = '" + message_.getAlias() + "'";
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			if (resultSet.next()) {

				// create client
				long id = resultSet.getLong("id");
				String username = resultSet.getString("username");
				client = new DataClient(clientConnection_, id, message_.getAlias(), username, server_.getLobby());
			}
		}
		return client;
	}

	@Override
	protected void failed(Exception e) {
		server_.getLogger().log(Level.WARNING, "Exception occurred during responding to client handshake.", e);
	}
}
