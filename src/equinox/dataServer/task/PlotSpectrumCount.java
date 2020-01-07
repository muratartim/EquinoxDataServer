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

import org.jfree.data.category.DefaultCategoryDataset;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.PlotSpectrumCountRequest;
import equinox.dataServer.remote.message.PlotSpectrumCountResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for plot spectrum count task.
 *
 * @author Murat Artim
 * @date 20 Feb 2018
 * @time 11:43:13
 */
public class PlotSpectrumCount extends DatabaseQueryTask {

	/**
	 * Creates plot spectrum count task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public PlotSpectrumCount(DataServer server, DataClient client, PlotSpectrumCountRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for plotting spectrum count.");

		// create response message
		PlotSpectrumCountResponse response = new PlotSpectrumCountResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		PlotSpectrumCountRequest request = (PlotSpectrumCountRequest) request_;
		String program = request.getProgram();
		String section = request.getSection();
		String mission = request.getMission();

		// create data set
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// all programs
				if (program == null) {
					plotAllPrograms(dataset, statement);
				}

				// all sections
				else if (program != null && section == null) {
					plotAllSections(dataset, statement, program);
				}

				// all missions
				else if (program != null && section != null && mission == null) {
					plotAllMissions(dataset, statement, program, section);
				}

				// single mission
				else if (program != null && section != null && mission != null) {
					plotSingleMission(dataset, statement, program, section, mission);
				}
			}
		}

		// set dataset to response
		response.setDataset(dataset);

		// respond to client
		client_.sendMessage(response);
	}

	/**
	 * Plots spectrum count for a single mission.
	 *
	 * @param dataset
	 *            Chart dataset.
	 * @param statement
	 *            Database statement.
	 * @param program
	 *            Aircraft program.
	 * @param section
	 *            Aircraft section.
	 * @param mission
	 *            Fatigue mission.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void plotSingleMission(DefaultCategoryDataset dataset, Statement statement, String program, String section, String mission) throws Exception {
		String sql = "select fat_mission, count(id) as spectrumCount from spectra where ac_program = '" + program + "' and ac_section = '" + section + "' and fat_mission = '" + mission + "' group by fat_mission order by spectrumCount desc";
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				int spectrumCount = resultSet.getInt("spectrumCount");
				dataset.addValue(spectrumCount, "Spectrum Summary", section);
			}
		}
	}

	/**
	 * Plots spectrum count for all missions.
	 *
	 * @param dataset
	 *            Chart dataset.
	 * @param statement
	 *            Database statement.
	 * @param program
	 *            Aircraft program.
	 * @param section
	 *            Aircraft section.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void plotAllMissions(DefaultCategoryDataset dataset, Statement statement, String program, String section) throws Exception {
		String sql = "select fat_mission, count(id) as spectrumCount from spectra where ac_program = '" + program + "' and ac_section = '" + section + "' group by fat_mission order by spectrumCount desc";
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				String mission = resultSet.getString("fat_mission");
				int spectrumCount = resultSet.getInt("spectrumCount");
				dataset.addValue(spectrumCount, "Spectrum Summary", mission);
			}
		}
	}

	/**
	 * Plots spectrum count for all sections.
	 *
	 * @param dataset
	 *            Chart dataset.
	 * @param statement
	 *            Database statement.
	 * @param program
	 *            Aircraft program.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void plotAllSections(DefaultCategoryDataset dataset, Statement statement, String program) throws Exception {
		String sql = "select ac_section, count(id) as spectrumCount from spectra where ac_program = '" + program + "' group by ac_section order by spectrumCount desc";
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				String section = resultSet.getString("ac_section");
				int spectrumCount = resultSet.getInt("spectrumCount");
				dataset.addValue(spectrumCount, "Spectrum Summary", section);
			}
		}
	}

	/**
	 * Plots spectrum count for all programs.
	 *
	 * @param dataset
	 *            Chart dataset.
	 * @param statement
	 *            Database statement.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void plotAllPrograms(DefaultCategoryDataset dataset, Statement statement) throws Exception {
		String sql = "select ac_program, count(id) as spectrumCount from spectra group by ac_program order by spectrumCount desc";
		try (ResultSet resultSet = statement.executeQuery(sql)) {
			while (resultSet.next()) {
				String program = resultSet.getString("ac_program");
				int spectrumCount = resultSet.getInt("spectrumCount");
				dataset.addValue(spectrumCount, "Spectrum Summary", program);
			}
		}
	}
}
