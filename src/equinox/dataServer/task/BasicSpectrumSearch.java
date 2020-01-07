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
import java.util.HashSet;
import java.util.Set;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.BasicSearchInput;
import equinox.dataServer.remote.data.SearchInput;
import equinox.dataServer.remote.data.SpectrumInfo;
import equinox.dataServer.remote.data.SpectrumInfo.SpectrumInfoType;
import equinox.dataServer.remote.message.BasicSpectrumSearchRequest;
import equinox.dataServer.remote.message.BasicSpectrumSearchResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for basic spectrum search task.
 *
 * @author Murat Artim
 * @date 24 Jan 2018
 * @time 15:23:23
 */
public class BasicSpectrumSearch extends DatabaseQueryTask {

	/**
	 * Creates advanced spectrum search task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public BasicSpectrumSearch(DataServer server, DataClient client, BasicSpectrumSearchRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for performing pilot point search.");

		// create response message
		BasicSpectrumSearchResponse response = new BasicSpectrumSearchResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get search input
		BasicSearchInput input = ((BasicSpectrumSearchRequest) request_).getInput();

		// build SQL query
		String sql = buildSQL(input);

		// create unique programs set
		Set<String> uniquePrograms = new HashSet<>();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			// create statement
			try (Statement statement = connection.createStatement()) {

				// set max hits
				statement.setMaxRows(input.getMaxHits());

				// execute query
				try (ResultSet resultSet = statement.executeQuery(sql)) {

					// prepare statement to count connected pilot points
					sql = "select count(id) as numpps from pilot_points where spectrum_name = ?";
					try (PreparedStatement countPPs = connection.prepareStatement(sql)) {

						// prepare statement to count connected multiplication tables
						sql = "select count(id) as nummults from mult_tables where spectrum_name = ?";
						try (PreparedStatement countMultTables = connection.prepareStatement(sql)) {

							// loop over spectra
							while (resultSet.next()) {

								// get A/C program
								String program = resultSet.getString("ac_program");

								// get spectrum info
								SpectrumInfo info = new SpectrumInfo();
								info.setInfo(SpectrumInfoType.ID, resultSet.getLong("id"));
								info.setInfo(SpectrumInfoType.NAME, resultSet.getString("name"));
								info.setInfo(SpectrumInfoType.DATA_SIZE, resultSet.getLong("data_size"));
								info.setInfo(SpectrumInfoType.DATA_URL, resultSet.getString("data_url"));
								info.setInfo(SpectrumInfoType.AC_PROGRAM, program);
								info.setInfo(SpectrumInfoType.AC_SECTION, resultSet.getString("ac_section"));
								info.setInfo(SpectrumInfoType.FAT_MISSION, resultSet.getString("fat_mission"));
								info.setInfo(SpectrumInfoType.FAT_MISSION_ISSUE, resultSet.getString("fat_mission_issue"));
								info.setInfo(SpectrumInfoType.FLP_ISSUE, resultSet.getString("flp_issue"));
								info.setInfo(SpectrumInfoType.IFLP_ISSUE, resultSet.getString("iflp_issue"));
								info.setInfo(SpectrumInfoType.CDF_ISSUE, resultSet.getString("cdf_issue"));
								info.setInfo(SpectrumInfoType.DELIVERY_REF, resultSet.getString("delivery_ref"));
								info.setInfo(SpectrumInfoType.DESCRIPTION, resultSet.getString("description"));

								// get number of connected pilot points
								int numPPs = 0;
								countPPs.setString(1, resultSet.getString("name"));
								try (ResultSet resultSet2 = countPPs.executeQuery()) {
									if (resultSet2.next()) {
										numPPs = resultSet2.getInt("numpps");
									}
								}
								info.setInfo(SpectrumInfoType.PILOT_POINTS, numPPs);

								// get number of connected multiplication tables
								int numMult = 0;
								countMultTables.setString(1, resultSet.getString("name"));
								try (ResultSet resultSet2 = countMultTables.executeQuery()) {
									if (resultSet2.next()) {
										numMult = resultSet2.getInt("nummults");
									}
								}
								info.setInfo(SpectrumInfoType.MULT_TABLES, numMult);

								// add info to response
								response.add(info);

								// add programs
								uniquePrograms.add(program);
							}
						}
					}
				}

				// reset statement
				statement.setMaxRows(0);
			}
		}

		// increment search trends
		server_.incrementSearchHits(uniquePrograms);

		// respond to client
		client_.sendMessage(response);
	}

	/**
	 * Builds and returns the SQL search query.
	 *
	 * @param input
	 *            Search input.
	 * @return The SQL search query.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static String buildSQL(BasicSearchInput input) throws Exception {

		// create SQL query
		String sql = "select * from spectra where ";

		// add search items
		sql += buildQueryForStringBasedItem(SpectrumInfoType.NAME, input);
		sql += buildQueryForStringBasedItem(SpectrumInfoType.AC_PROGRAM, input);
		sql += buildQueryForStringBasedItem(SpectrumInfoType.AC_SECTION, input);
		sql += buildQueryForStringBasedItem(SpectrumInfoType.FAT_MISSION, input);
		sql += buildQueryForStringBasedItem(SpectrumInfoType.DELIVERY_REF, input);
		sql += buildQueryForStringBasedItem(SpectrumInfoType.DESCRIPTION, input);

		// remove trailing operator
		sql = sql.substring(0, sql.lastIndexOf(" or "));

		// add order by criteria
		String orderBy = input.getOrderByCriteria();
		if (orderBy.equals(SearchInput.NAME)) {
			sql += " order by name";
		}
		else if (orderBy.equals(SearchInput.PROGRAM)) {
			sql += " order by ac_program";
		}
		else if (orderBy.equals(SearchInput.SECTION)) {
			sql += " order by ac_section";
		}
		else if (orderBy.equals(SearchInput.MISSION)) {
			sql += " order by fat_mission";
		}
		else if (orderBy.equals(SearchInput.DELIVERY)) {
			sql += " order by delivery_ref";
		}

		// add order
		sql += input.getOrder() ? " asc" : " desc";

		// return query
		return sql;
	}

	/**
	 * Builds and returns SQL query for the given type of string based search item.
	 *
	 * @param type
	 *            Type of search item.
	 * @param input
	 *            Search input.
	 * @return SQL query.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static String buildQueryForStringBasedItem(SpectrumInfoType type, BasicSearchInput input) throws Exception {

		// get database column name
		String columnName = type.getColumnName();

		// create query
		String sql = "(";
		for (String keyword : input.getKeywords()) {
			sql += input.getCase() ? "upper(" + columnName + ") like upper('%" + keyword + "%')" : columnName + " like '%" + keyword + "%'";
			sql += input.getOperator() ? " and " : " or ";
		}

		// remove trailing operator
		sql = input.getOperator() ? sql.substring(0, sql.lastIndexOf(" and ")) : sql.substring(0, sql.lastIndexOf(" or "));
		sql += ")";

		// add or operator
		sql += " or ";

		// return query
		return sql;
	}
}
