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
package equinox.dataServer.client;

import java.util.logging.Level;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import equinox.dataServer.remote.message.HandshakeWithDataServer;
import equinox.dataServer.server.DataServer;
import equinox.dataServer.task.ProcessHandshake;
import equinox.serverUtilities.NetworkMessage;
import equinox.serverUtilities.PartialMessage;

/**
 * Class for client handler. Client handler manages the incoming client connections.
 *
 * @version 1.0
 * @author Murat Artim
 * @time 5:01:21 PM
 * @date Jul 10, 2011
 *
 */
public class ClientHandler extends Listener {

	/** The central server. */
	private final DataServer server_;

	/**
	 * Creates client handler. Client handler manages the incoming client connections.
	 *
	 * @param server
	 *            The central server.
	 */
	public ClientHandler(DataServer server) {
		server_ = server;
		server_.getLogger().info("Client handler created.");
	}

	@Override
	public void received(final Connection connection, final Object object) {

		// null connection/message or unsupported protocol
		if (connection == null || object == null || connection instanceof ClientConnection == false || object instanceof NetworkMessage == false)
			return;

		// respond
		server_.getThreadPool().submit(() -> {

			// cast to client connection
			ClientConnection cc = (ClientConnection) connection;

			// cast to network message
			NetworkMessage networkMessage = (NetworkMessage) object;

			// unknown client (hand shake required)
			if (cc.getClient() == null) {
				handShake(cc, networkMessage);
			}

			// known client
			else {
				processClient(cc, networkMessage);
			}
		});
	}

	@Override
	public void disconnected(Connection connection) {

		// null connection
		if (connection == null)
			return;

		// get client
		DataClient client = ((ClientConnection) connection).getClient();

		// remove client
		if (client != null) {
			server_.removeClient(client);
		}
	}

	/**
	 * Performs server handshake with client.
	 *
	 * @param cc
	 *            Client connection.
	 * @param message
	 *            Network message.
	 */
	private void handShake(ClientConnection cc, NetworkMessage message) {

		// respond to client message with its room protocol
		try {

			// not handshake message
			if (message instanceof HandshakeWithDataServer == false)
				return;

			// cast
			HandshakeWithDataServer handshake = (HandshakeWithDataServer) message;

			// get alias
			String alias = handshake.getAlias();

			// alias exists
			if (server_.getClient(alias) != null) {
				handshake.setReply(false);
				cc.sendTCP(message);
				return;
			}

			// process handshake
			server_.getThreadPool().submit(new ProcessHandshake(cc, handshake, server_));
		}

		// exception occurred during responding to client request
		catch (Exception e) {
			server_.getLogger().log(Level.WARNING, "Exception occurred during responding to client handshake.", e);
		}
	}

	/**
	 * Responds to messages from clients who are logged-in.
	 *
	 * @param connection
	 *            Client connection to respond.
	 * @param message
	 *            Client's message.
	 */
	private void processClient(ClientConnection connection, NetworkMessage message) {

		// get client
		DataClient client = connection.getClient();

		// respond to client message
		try {

			// partial message
			if (message instanceof PartialMessage) {
				client.receivePartialMessage((PartialMessage) message);
			}

			// normal message
			else {
				server_.getLobby().respond(client, message);
			}
		}

		// exception occurred during responding to client request
		catch (Exception e) {
			server_.getLogger().log(Level.WARNING, "Exception occurred during responding to client request.", e);
		}
	}
}
