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
import equinox.dataServer.remote.message.DataServerStatisticsRequestFailed;
import equinox.dataServer.remote.message.GetSearchHitsRequest;
import equinox.dataServer.remote.message.GetSearchHitsResponse;

/**
 * Class for get search hits task.
 *
 * @author Murat Artim
 * @date 28 Jul 2018
 * @time 00:34:36
 */
public class GetSearchHits extends ServerTask {

	/** Requesting client. */
	private final DataClient client;

	/** Request message. */
	private final GetSearchHitsRequest request;

	/**
	 * Creates get diagnostics task.
	 *
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetSearchHits(DataClient client, GetSearchHitsRequest request) {
		super(client.getLobby().getServer());
		this.client = client;
		this.request = request;
	}

	@Override
	protected void runTask() throws Exception {

		// create response
		GetSearchHitsResponse response = new GetSearchHitsResponse();
		response.setListenerHashCode(request.getListenerHashCode());

		// get top 4 search hits
		response.setSearchHits(server_.getSearchHits(4));

		// send it
		client.sendMessage(response);
	}

	@Override
	protected void failed(Exception e) {

		// log exception
		server_.getLogger().log(Level.WARNING, "Get search hits failed.", e);

		// no client
		if (client == null)
			return;

		// send query failed message to client
		DataServerStatisticsRequestFailed message = new DataServerStatisticsRequestFailed();
		message.setException(e);
		client.sendMessage(message);
	}
}
