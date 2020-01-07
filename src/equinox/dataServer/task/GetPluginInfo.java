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
import equinox.dataServer.remote.data.ServerPluginInfo;
import equinox.dataServer.remote.data.ServerPluginInfo.PluginInfoType;
import equinox.dataServer.remote.message.GetPluginInfoRequest;
import equinox.dataServer.remote.message.GetPluginInfoResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for get plugin info task.
 *
 * @author Murat Artim
 * @date 12 Dec 2017
 * @time 00:29:51
 */
public class GetPluginInfo extends DatabaseQueryTask {

	/**
	 * Creates get plugin info task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Database query request.
	 */
	public GetPluginInfo(DataServer server, DataClient client, GetPluginInfoRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for getting plugin info.");

		// reset request message
		GetPluginInfoResponse message = new GetPluginInfoResponse();
		message.setListenerHashCode(request_.getListenerHashCode());

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// execute query
				String sql = "select * from plugins order by name";
				try (ResultSet resultSet = statement.executeQuery(sql)) {

					// loop over plugins
					while (resultSet.next()) {

						// get plugin info
						ServerPluginInfo plugin = new ServerPluginInfo();
						for (PluginInfoType type : PluginInfoType.values()) {
							plugin.setInfo(type, resultSet.getObject(type.getColumnName()));
						}

						// add plugin to message
						message.add(plugin);
					}
				}
			}
		}

		// respond to client
		client_.sendMessage(message);
	}
}
