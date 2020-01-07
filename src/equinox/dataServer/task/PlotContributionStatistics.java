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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.jfree.data.category.DefaultCategoryDataset;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.ContributionType;
import equinox.dataServer.remote.message.GetAircraftSectionsForPilotPointsRequest;
import equinox.dataServer.remote.message.GetFatigueMissionsForPilotPointsRequest;
import equinox.dataServer.remote.message.PlotContributionStatisticsRequest;
import equinox.dataServer.remote.message.PlotContributionStatisticsResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for plot contribution statistics task.
 *
 * @author Murat Artim
 * @date 18 Feb 2018
 * @time 21:59:40
 */
public class PlotContributionStatistics extends DatabaseQueryTask {

	/**
	 * Creates plot contribution statistics task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public PlotContributionStatistics(DataServer server, DataClient client, PlotContributionStatisticsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for plotting contribution statistics.");

		// create response message
		PlotContributionStatisticsResponse response = new PlotContributionStatisticsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		PlotContributionStatisticsRequest request = (PlotContributionStatisticsRequest) request_;

		// create data set
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// all sections
				if (request.getSection().equals(GetAircraftSectionsForPilotPointsRequest.ALL_SECTIONS)) {
					plotForAllSections(request, connection, statement, dataset);
				}

				// single section
				else {
					plotForSingleSection(request, statement, dataset);
				}
			}
		}

		// set dataset to response
		response.setDataset(dataset);

		// respond to client
		client_.sendMessage(response);
	}

	/**
	 * Plots contribution statistics for selected aircraft sections.
	 *
	 * @param request
	 *            Request message.
	 * @param statement
	 *            Database statement.
	 * @param dataset
	 *            Plot dataset.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void plotForSingleSection(PlotContributionStatisticsRequest request, Statement statement, DefaultCategoryDataset dataset) throws Exception {

		// get inputs
		String program = request.getProgram();
		String section = request.getSection();
		String mission = request.getMission();

		// build query
		String sql = null;

		// all missions
		if (mission.equals(GetFatigueMissionsForPilotPointsRequest.ALL_MISSIONS)) {
			sql = "select increment_contributions.event, avg(increment_contributions.contribution) as avgvalue from damage_contributions ";
			sql += "inner join increment_contributions on increment_contributions.damcont_id = damage_contributions.id where ";
			sql += "damage_contributions.ac_program = '" + program + "' and damage_contributions.ac_section = '" + section + "' group by ";
			sql += "increment_contributions.event order by avgvalue desc";
		}

		// specific mission
		else {
			sql = "select increment_contributions.event, avg(increment_contributions.contribution) as avgvalue from damage_contributions inner ";
			sql += "join increment_contributions on increment_contributions.damcont_id = damage_contributions.id where ";
			sql += "damage_contributions.ac_program = '" + program + "' and damage_contributions.ac_section = '" + section + "' and ";
			sql += "damage_contributions.fat_mission = '" + mission + "' group by increment_contributions.event order by avgvalue desc";
		}

		// execute
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				String event = resultSet.getString("event");
				double avgval = resultSet.getDouble("avgvalue");
				dataset.addValue(avgval, "Increment Contributions", event);
			}
		}
	}

	/**
	 * Plots contribution statistics for all aircraft sections.
	 *
	 * @param request
	 *            Request message.
	 * @param connection
	 *            Database statement.
	 * @param statement
	 *            Database statement.
	 * @param dataset
	 *            Plot dataset.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void plotForAllSections(PlotContributionStatisticsRequest request, Connection connection, Statement statement, DefaultCategoryDataset dataset) throws Exception {

		// get inputs
		String program = request.getProgram();
		String mission = request.getMission();
		ContributionType contType = request.getContributionType();

		// incremental contributions
		if (contType.equals(ContributionType.INCREMENT)) {
			plotIncrementsForAllSections(request, connection, statement, dataset);
		}

		// steady contributions
		else {

			// build query
			String sql = null;

			// all missions
			if (mission.equals(GetFatigueMissionsForPilotPointsRequest.ALL_MISSIONS)) {
				sql = "select damage_contributions.ac_section, avg(steady_contributions." + contType.getColumnName() + ") as avgvalue from damage_contributions inner join ";
				sql += "steady_contributions on steady_contributions.damcont_id = damage_contributions.id where damage_contributions.ac_program = '" + program + "' ";
				sql += "group by damage_contributions.ac_section";
			}

			// specific mission
			else {
				sql = "select damage_contributions.ac_section, avg(steady_contributions." + contType.getColumnName() + ") as avgvalue from damage_contributions inner join ";
				sql += "steady_contributions on steady_contributions.damcont_id = damage_contributions.id where damage_contributions.ac_program = '" + program + "' ";
				sql += "and damage_contributions.fat_mission = '" + mission + "' group by damage_contributions.ac_section";
			}

			// execute
			try (ResultSet resultSet = statement.executeQuery(sql)) {
				while (resultSet.next()) {
					String section = resultSet.getString("ac_section");
					double avgval = resultSet.getDouble("avgvalue");
					dataset.addValue(avgval, "Steady Contributions", section);
				}
			}
		}
	}

	/**
	 * Plots incremental contributions for all aircraft sections.
	 *
	 * @param request
	 *            Request message.
	 * @param connection
	 *            Database statement.
	 * @param statement
	 *            Database statement.
	 * @param dataset
	 *            Plot dataset.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void plotIncrementsForAllSections(PlotContributionStatisticsRequest request, Connection connection, Statement statement, DefaultCategoryDataset dataset) throws Exception {

		// create a view containing section, event and contribution value
		String viewName = "tempView_" + client_.getAlias();

		// get inputs
		String program = request.getProgram();
		String mission = request.getMission();
		int limit = request.getLimit();

		// build query
		String sql = null;

		// all missions
		if (mission.equals(GetFatigueMissionsForPilotPointsRequest.ALL_MISSIONS)) {
			sql = "create view " + viewName + "(section, event, val) as select damage_contributions.ac_section, increment_contributions.event, ";
			sql += "avg(increment_contributions.contribution) as avgvalue from damage_contributions inner join increment_contributions on ";
			sql += "increment_contributions.damcont_id = damage_contributions.id where damage_contributions.ac_program = '" + program + "' ";
			sql += "group by damage_contributions.ac_section, increment_contributions.event";
		}

		// specific mission
		else {
			sql = "create view " + viewName + "(section, event, val) as select damage_contributions.ac_section, increment_contributions.event, ";
			sql += "avg(increment_contributions.contribution) as avgvalue from damage_contributions inner join increment_contributions on ";
			sql += "increment_contributions.damcont_id = damage_contributions.id where damage_contributions.ac_program = '" + program + "' ";
			sql += "and damage_contributions.fat_mission = '" + mission + "' group by damage_contributions.ac_section, increment_contributions.event";
		}

		// execute statement
		statement.executeUpdate(sql);

		try {

			// prepare statement to select event and contribution value for given section
			sql = "select event, val from " + viewName + " where section = ? order by val desc limit " + limit;
			try (PreparedStatement selectEventVal = connection.prepareStatement(sql)) {

				// select distinct sections
				sql = "select distinct section from " + viewName;
				try (ResultSet sections = statement.executeQuery(sql)) {

					// loop over sections
					while (sections.next()) {

						// set section
						String section = sections.getString("section");

						// get event and contribution value for section
						selectEventVal.setString(1, section);
						try (ResultSet eventVal = selectEventVal.executeQuery()) {

							// loop over event and contribution values
							while (eventVal.next()) {

								// get event
								String event = eventVal.getString("event");
								double val = eventVal.getDouble("val");
								dataset.addValue(val, event, section);
							}
						}
					}
				}
			}
		}

		// drop view
		finally {
			statement.executeUpdate("drop view " + viewName);
		}
	}
}
