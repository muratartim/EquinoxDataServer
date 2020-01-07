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

import java.util.logging.Level;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.DatabaseQueryFailed;
import equinox.dataServer.remote.message.DatabaseQueryProgress;
import equinox.dataServer.remote.message.DatabaseQueryRequest;
import equinox.dataServer.server.DataServer;

/**
 * Abstract class for database query task.
 *
 * @author Murat Artim
 * @date 12 Dec 2017
 * @time 00:45:31
 */
public abstract class DatabaseQueryTask extends ServerTask {

	/** Client who sends the message. */
	protected final DataClient client_;

	/** Query message. */
	protected final DatabaseQueryRequest request_;

	/**
	 * Creates database query task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Query request message.
	 */
	public DatabaseQueryTask(DataServer server, DataClient client, DatabaseQueryRequest request) {

		// create server task
		super(server);

		// set attributes
		request_ = request;
		client_ = client;
	}

	@Override
	protected void failed(Exception e) {

		try {

			// increment failed query count for server statistics
			server_.incrementFailedQueries();

			// log exception
			server_.getLogger().log(Level.WARNING, "Database query failed for client '" + client_.getUsername() + "'.", e);

			// no client
			if (client_ == null)
				return;

			// send query failed message to client
			DatabaseQueryFailed message = new DatabaseQueryFailed();
			message.setListenerHashCode(request_.getListenerHashCode());
			message.setException(e);
			client_.sendMessage(message);
		}

		// exception occurred during process
		catch (Exception e1) {

			// log exception
			server_.getLogger().log(Level.WARNING, "Exception occurred during processing failed database query for client '" + client_.getUsername() + "'.", e1);

			// send query failed message to client
			DatabaseQueryFailed message = new DatabaseQueryFailed();
			message.setListenerHashCode(request_.getListenerHashCode());
			message.setException(e1);
			client_.sendMessage(message);
		}
	}

	/**
	 * Sends progress message to client.
	 *
	 * @param progressMessage
	 *            Message text.
	 */
	protected void sendProgressMessage(String progressMessage) {
		DatabaseQueryProgress message = new DatabaseQueryProgress();
		message.setListenerHashCode(request_.getListenerHashCode());
		message.setProgressMessage(progressMessage);
		client_.sendMessage(message);
	}
}
