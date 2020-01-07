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
package equinox.dataServer.remote.data;

import java.io.Serializable;
import java.util.Date;

/**
 * Class for periodically collected data server statistic.
 *
 * @author Murat Artim
 * @date 22 Jun 2018
 * @time 20:22:05
 */
public class PeriodicDataServerStatistic implements Serializable {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Record time. */
	private final Date recorded;

	/** Statistics. */
	private int clients, queries, failedQueries, threadPoolSize, activeThreads;

	/**
	 * No argument constructor for serialization.
	 */
	public PeriodicDataServerStatistic() {
		recorded = new Date();
	}

	/**
	 * Sets number of connected clients.
	 *
	 * @param clients
	 *            Number of connected clients.
	 */
	public void setClients(int clients) {
		this.clients = clients;
	}

	/**
	 * Sets number of database queries.
	 *
	 * @param queries
	 *            Number of database queries.
	 */
	public void setQueries(int queries) {
		this.queries = queries;
	}

	/**
	 * Sets number of failed queries.
	 *
	 * @param failedQueries
	 *            Number of failed queries.
	 */
	public void setFailedQueries(int failedQueries) {
		this.failedQueries = failedQueries;
	}

	/**
	 * Sets the thread pool size.
	 *
	 * @param threadPoolSize
	 *            Thread pool size.
	 */
	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	/**
	 * Sets number of active threads.
	 *
	 * @param activeThreads
	 *            Number of active threads.
	 */
	public void setActiveThreads(int activeThreads) {
		this.activeThreads = activeThreads;
	}

	/**
	 * Returns the record time.
	 *
	 * @return The record time.
	 */
	public Date getRecorded() {
		return recorded;
	}

	/**
	 * Returns number of connected clients.
	 *
	 * @return Number of connected clients.
	 */
	public int getClients() {
		return clients;
	}

	/**
	 * Returns number of database queries.
	 *
	 * @return Number of database queries.
	 */
	public int getQueries() {
		return queries;
	}

	/**
	 * Returns number of failed queries.
	 *
	 * @return Number of failed queries.
	 */
	public int getFailedQueries() {
		return failedQueries;
	}

	/**
	 * Returns the thread pool size.
	 *
	 * @return The thread pool size.
	 */
	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	/**
	 * Returns the number of active threads.
	 *
	 * @return Number of active threads.
	 */
	public int getActiveThreads() {
		return activeThreads;
	}

	@Override
	public String toString() {
		String text = "Recorded: " + recorded.toString();
		text += ", Clients: " + clients;
		text += ", Queries: " + queries;
		text += ", Failed Queries: " + failedQueries;
		text += ", Thread Pool Size: " + threadPoolSize;
		text += ", Active Threads: " + activeThreads;
		return text;
	}
}