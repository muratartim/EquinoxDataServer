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
package equinox.dataServer.server;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.esotericsoftware.kryonet.Server;
import com.zaxxer.hikari.HikariDataSource;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.PeriodicDataServerStatistic;
import equinox.dataServer.task.CollectServerStatistics;
import equinox.dataServer.utility.Utility;

/**
 * Equinox data server application entry class.
 *
 * @author Murat Artim
 * @date Sep 16, 2014
 * @time 5:50:06 PM
 */
public class DataServer extends Thread {

	/** Server properties. */
	private final Properties properties_;

	/** Server logger. */
	private final Logger logger_;

	/** Database connection pool. */
	private final HikariDataSource dcpEngine_;

	/** Cached thread pool. */
	private final ExecutorService threadPool_;

	/** Scheduled thread pool. */
	private final ScheduledExecutorService scheduledThreadPool_;

	/** Client lobby. */
	private final Lobby lobby_;

	/** List containing the connected clients. */
	private final List<DataClient> clients_;

	/** The network server. */
	private final Server networkServer_;

	/** Server health monitor parameters. */
	private final AtomicInteger queryRequests_, failedQueries_;

	/** Data server statistics. */
	private final ArrayList<PeriodicDataServerStatistic> statistics_;

	/** Search hits. */
	private final Map<String, Integer> searchHits_;

	/** True if the server is shut down. */
	private volatile boolean isShutDown_ = false;

	/**
	 * Creates equinox server.
	 *
	 * @throws Exception
	 *             If server cannot be created.
	 */
	public DataServer() throws Exception {

		// create thread
		super("Equinox Data Server");

		// read server properties
		properties_ = Utility.loadProperties(Paths.get("resources/config.properties"));

		// setup server logger
		logger_ = Utility.setupLogger(properties_.getProperty("log.filename"), properties_.getProperty("log.level"));

		// setup database connection pool
		dcpEngine_ = Utility.setupDCPEngine(properties_, logger_);

		// setup network server
		networkServer_ = Utility.setupNetworkServer(this);

		// create thread pools
		threadPool_ = Executors.newCachedThreadPool();
		scheduledThreadPool_ = Executors.newSingleThreadScheduledExecutor();
		logger_.info("Thread pools created.");

		// initialize server statistic counters
		queryRequests_ = new AtomicInteger();
		failedQueries_ = new AtomicInteger();

		// create client list
		clients_ = Collections.synchronizedList(new ArrayList<DataClient>());

		// create client lobby
		lobby_ = new Lobby(this);

		// create server statistics
		statistics_ = new ArrayList<>();
		searchHits_ = Collections.synchronizedMap(new HashMap<String, Integer>());

		// log server creation info
		logger_.info("Server initialized.");
	}

	@Override
	public void run() {

		// log start message
		logger_.info("Starting server...");

		try {

			// bind server to its port
			networkServer_.bind(Integer.parseInt(properties_.getProperty("ns.port")));

			// start the network server
			networkServer_.start();

			// schedule statistics collection
			if (properties_.getProperty("stat.collect").equals("yes")) {
				long period = Long.parseLong(properties_.getProperty("stat.period"));
				scheduledThreadPool_.scheduleAtFixedRate(new CollectServerStatistics(this), 30, period, TimeUnit.SECONDS);
			}
		}

		// exception occurred during starting server
		catch (IOException e) {
			logger_.log(Level.SEVERE, "Exception occurred during starting server.", e);
			stopServer(true);
		}
	}

	/**
	 * Stops the server.
	 *
	 * @param exit
	 *            True to exit JVM.
	 */
	public void stopServer(boolean exit) {

		// already shut down
		if (isShutDown_)
			return;

		// set shut down
		isShutDown_ = true;

		// log stopping message
		logger_.info("Stopping server...");

		// stop lobby
		lobby_.stop();

		// shutdown thread pool
		Utility.shutdownThreadPool(threadPool_, logger_);
		Utility.shutdownThreadPool(scheduledThreadPool_, logger_);
		logger_.info("Thread pools shutdown.");

		// shutdown database connection pool
		dcpEngine_.close();
		logger_.info("Database connection pool shutdown.");

		// stop network server
		networkServer_.stop();
		logger_.info("Network server shutdown.");

		// close logger
		Arrays.stream(logger_.getHandlers()).forEach(h -> h.close());

		// exit
		if (exit) {
			System.exit(0);
		}
	}

	/**
	 * Returns server statistics.
	 *
	 * @return Server statistics.
	 */
	public ArrayList<PeriodicDataServerStatistic> getStatistics() {
		return statistics_;
	}

	/**
	 * Returns server properties.
	 *
	 * @return Server properties.
	 */
	public Properties getProperties() {
		return properties_;
	}

	/**
	 * Returns server logger.
	 *
	 * @return Server logger.
	 */
	public Logger getLogger() {
		return logger_;
	}

	/**
	 * Returns database connection pool.
	 *
	 * @return Database connection pool.
	 */
	public HikariDataSource getDCP() {
		return dcpEngine_;
	}

	/**
	 * Returns thread pool.
	 *
	 * @return Thread pool.
	 */
	public ExecutorService getThreadPool() {
		return threadPool_;
	}

	/**
	 * Returns network server.
	 *
	 * @return Network server.
	 */
	public Server getNetworkServer() {
		return networkServer_;
	}

	/**
	 * Returns client lobby.
	 *
	 * @return Client lobby.
	 */
	public Lobby getLobby() {
		return lobby_;
	}

	/**
	 * Returns the connected clients of the server.
	 *
	 * @return List containing the connected clients of the server.
	 */
	public List<DataClient> getClients() {
		return clients_;
	}

	/**
	 * Checks whether the given alias matches with the alias of a connected client. If so, returns the client or <code>null</code>.
	 *
	 * @param alias
	 *            Client alias to check.
	 * @return The client with the given alias or <code>null</code> if no client is connected with the given alias.
	 */
	public DataClient getClient(String alias) {

		// sync over clients
		synchronized (clients_) {

			// get clients
			Iterator<DataClient> i = clients_.iterator();

			// loop over clients
			while (i.hasNext()) {

				// get client
				DataClient c = i.next();

				// client alias matches
				if (c.getAlias().equals(alias))
					return c;
			}
		}

		// client doesn't exist
		return null;
	}

	/**
	 * Creates and adds the given client.
	 *
	 * @param client
	 *            Client to add.
	 */
	public void addClient(DataClient client) {

		// add client
		synchronized (clients_) {
			clients_.add(client);
		}

		// log added info
		logger_.info("Client '" + client.getAlias() + "' added to connected clients.");
	}

	/**
	 * Removes the given client.
	 *
	 * @param client
	 *            Client to remove.
	 */
	public void removeClient(DataClient client) {

		// remove client
		synchronized (clients_) {
			clients_.remove(client);
		}

		// log removal
		logger_.info("Client '" + client.getAlias() + "' removed from connected clients.");
	}

	/**
	 * Increments search hits for the given aircraft programs.
	 *
	 * @param programs
	 *            Aircraft programs.
	 */
	public void incrementSearchHits(Set<String> programs) {
		synchronized (searchHits_) {
			programs.forEach(program -> {
				Integer count = searchHits_.get(program);
				searchHits_.put(program, count == null ? 1 : count + 1);
			});
		}
	}

	/**
	 * Increments query requests.
	 *
	 * @return The updated value.
	 */
	public int incrementQueryRequests() {
		return queryRequests_.incrementAndGet();
	}

	/**
	 * Increments failed queries.
	 *
	 * @return The updated value.
	 */
	public int incrementFailedQueries() {
		return failedQueries_.incrementAndGet();
	}

	/**
	 * Returns query requests and resets the value.
	 *
	 * @return Query requests.
	 */
	public int getQueryRequests() {
		return queryRequests_.getAndSet(0);
	}

	/**
	 * Returns failed queries and resets the value.
	 *
	 * @return Failed queries.
	 */
	public int getFailedQueries() {
		return failedQueries_.getAndSet(0);
	}

	/**
	 * Returns mapping containing search hits.
	 *
	 * @param limit
	 *            Limit.
	 * @return Mapping containing search hits.
	 */
	public Map<String, Integer> getSearchHits(int limit) {
		synchronized (searchHits_) {
			limit = Math.min(limit, searchHits_.size());
			return searchHits_.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(limit).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		}
	}
}