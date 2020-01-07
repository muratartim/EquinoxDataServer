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

import java.util.Arrays;
import java.util.logging.Level;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.PeriodicDataServerStatistic;
import equinox.dataServer.remote.message.DataServerStatisticsRequestFailed;
import equinox.dataServer.remote.message.GetDataQueriesRequest;
import equinox.dataServer.remote.message.GetDataQueriesResponse;

/**
 * Class for get data query count task.
 *
 * @author Murat Artim
 * @date 28 Jul 2018
 * @time 00:23:08
 */
public class GetDataQueries extends ServerTask {

	/** Requesting client. */
	private final DataClient client;

	/** Request message. */
	private final GetDataQueriesRequest request;

	/**
	 * Creates get diagnostics task.
	 *
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public GetDataQueries(DataClient client, GetDataQueriesRequest request) {
		super(client.getLobby().getServer());
		this.client = client;
		this.request = request;
	}

	@Override
	protected void runTask() throws Exception {

		// create response
		GetDataQueriesResponse response = new GetDataQueriesResponse();
		response.setListenerHashCode(request.getListenerHashCode());

		// get requested periodic statistics
		PeriodicDataServerStatistic[] stats = server_.getStatistics().stream().filter(x -> x.getRecorded().after(request.getFrom()) && x.getRecorded().before(request.getTo())).toArray(PeriodicDataServerStatistic[]::new);

		// sort periodic statistics
		if (stats != null) {
			Arrays.sort(stats, (o1, o2) -> o1.getRecorded().compareTo(o2.getRecorded()));
		}

		// set periodic statistics
		response.setPeriodicStatistics(stats);

		// send it
		client.sendMessage(response);
	}

	@Override
	protected void failed(Exception e) {

		// log exception
		server_.getLogger().log(Level.WARNING, "Get data query count failed.", e);

		// no client
		if (client == null)
			return;

		// send query failed message to client
		DataServerStatisticsRequestFailed message = new DataServerStatisticsRequestFailed();
		message.setException(e);
		client.sendMessage(message);
	}
}
