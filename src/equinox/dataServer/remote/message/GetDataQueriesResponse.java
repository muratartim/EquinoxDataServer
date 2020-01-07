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
package equinox.dataServer.remote.message;

import equinox.dataServer.remote.data.PeriodicDataServerStatistic;
import equinox.serverUtilities.BigMessage;

/**
 * Class for get data queries response network message.
 *
 * @author Murat Artim
 * @date 27 Jul 2018
 * @time 20:47:20
 */
public class GetDataQueriesResponse extends DataMessage implements BigMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Periodic statistics. */
	private PeriodicDataServerStatistic[] periodicStats;

	/**
	 * No argument constructor for serialization.
	 */
	public GetDataQueriesResponse() {
	}

	/**
	 * Returns periodically collected statistics.
	 *
	 * @return Periodically collected statistics.
	 */
	public PeriodicDataServerStatistic[] getPeriodicStatistics() {
		return periodicStats;
	}

	/**
	 * Sets periodically collected statistics.
	 *
	 * @param periodicStats
	 *            Periodically collected statistics.
	 */
	public void setPeriodicStatistics(PeriodicDataServerStatistic[] periodicStats) {
		this.periodicStats = periodicStats;
	}

	@Override
	public boolean isReallyBig() {
		return periodicStats == null ? false : periodicStats.length > 30;
	}
}
