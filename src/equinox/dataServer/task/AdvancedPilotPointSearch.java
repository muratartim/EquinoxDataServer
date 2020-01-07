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
import java.util.HashSet;
import java.util.Set;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.PilotPointInfo;
import equinox.dataServer.remote.data.PilotPointInfo.PilotPointInfoType;
import equinox.dataServer.remote.data.PilotPointSearchInput;
import equinox.dataServer.remote.data.SearchInput;
import equinox.dataServer.remote.data.SearchItem;
import equinox.dataServer.remote.message.AdvancedPilotPointSearchRequest;
import equinox.dataServer.remote.message.AdvancedPilotPointSearchResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for advanced pilot point search task.
 *
 * @author Murat Artim
 * @date 24 Jan 2018
 * @time 10:04:37
 */
public class AdvancedPilotPointSearch extends DatabaseQueryTask {

	/**
	 * Creates advanced pilot point search task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public AdvancedPilotPointSearch(DataServer server, DataClient client, AdvancedPilotPointSearchRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for pilot point search.");

		// create response message
		AdvancedPilotPointSearchResponse response = new AdvancedPilotPointSearchResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get search input
		PilotPointSearchInput input = ((AdvancedPilotPointSearchRequest) request_).getInput();

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

					// loop over spectra
					while (resultSet.next()) {

						// get program
						String program = resultSet.getString("ac_program");

						// get pilot point info
						PilotPointInfo info = new PilotPointInfo();
						info.setInfo(PilotPointInfoType.ID, resultSet.getLong("id"));
						info.setInfo(PilotPointInfoType.SPECTRUM_NAME, resultSet.getString("spectrum_name"));
						info.setInfo(PilotPointInfoType.NAME, resultSet.getString("name"));
						info.setInfo(PilotPointInfoType.AC_PROGRAM, program);
						info.setInfo(PilotPointInfoType.AC_SECTION, resultSet.getString("ac_section"));
						info.setInfo(PilotPointInfoType.FAT_MISSION, resultSet.getString("fat_mission"));
						info.setInfo(PilotPointInfoType.DESCRIPTION, resultSet.getString("description"));
						info.setInfo(PilotPointInfoType.ELEMENT_TYPE, resultSet.getString("element_type"));
						info.setInfo(PilotPointInfoType.FRAME_RIB_POSITION, resultSet.getString("frame_rib_position"));
						info.setInfo(PilotPointInfoType.STRINGER_POSITION, resultSet.getString("stringer_position"));
						info.setInfo(PilotPointInfoType.DATA_SOURCE, resultSet.getString("data_source"));
						info.setInfo(PilotPointInfoType.GENERATION_SOURCE, resultSet.getString("generation_source"));
						info.setInfo(PilotPointInfoType.DELIVERY_REF_NUM, resultSet.getString("delivery_ref_num"));
						info.setInfo(PilotPointInfoType.ISSUE, resultSet.getString("issue"));
						info.setInfo(PilotPointInfoType.FATIGUE_MATERIAL, resultSet.getString("fatigue_material"));
						info.setInfo(PilotPointInfoType.PREFFAS_MATERIAL, resultSet.getString("preffas_material"));
						info.setInfo(PilotPointInfoType.LINEAR_MATERIAL, resultSet.getString("linear_material"));
						info.setInfo(PilotPointInfoType.EID, resultSet.getString("eid"));

						// add info to response
						response.add(info);

						// add programs
						uniquePrograms.add(program);
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
	private static String buildSQL(PilotPointSearchInput input) throws Exception {

		// initialize parameters
		String sql = "select id, spectrum_name, name, ac_program, ac_section, fat_mission, description, element_type, ";
		sql += "frame_rib_position, stringer_position, data_source, generation_source, delivery_ref_num, issue, ";
		sql += "fatigue_material, preffas_material, linear_material, eid from pilot_points where ";

		// add search items
		sql += buildQueryForStringBasedItem(PilotPointInfoType.SPECTRUM_NAME, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.NAME, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.AC_PROGRAM, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.AC_SECTION, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.FAT_MISSION, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.DESCRIPTION, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.ELEMENT_TYPE, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.FRAME_RIB_POSITION, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.STRINGER_POSITION, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.DATA_SOURCE, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.GENERATION_SOURCE, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.DELIVERY_REF_NUM, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.FATIGUE_MATERIAL, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.PREFFAS_MATERIAL, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.LINEAR_MATERIAL, input);
		sql += buildQueryForStringBasedItem(PilotPointInfoType.EID, input);

		// remove trailing operator
		if (sql.endsWith(" and ")) {
			sql = sql.substring(0, sql.lastIndexOf(" and "));
		}
		else if (sql.endsWith(" or ")) {
			sql = sql.substring(0, sql.lastIndexOf(" or "));
		}

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
			sql += " order by delivery_ref_num";
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
	private static String buildQueryForStringBasedItem(PilotPointInfoType type, PilotPointSearchInput input) throws Exception {

		// get search item
		SearchItem item = input.getInput(type);

		// search item exists
		String sql = "";
		if (item != null) {

			// get database column name
			String columnName = type.getColumnName();

			// create query
			sql += input.getCase() ? "upper(" + columnName + ") like " : columnName + " like ";
			int criteria = item.getCriteria();
			String value = item.getValue().toString();
			if (criteria == 0) {
				sql += input.getCase() ? "upper('%" + value + "%')" : "'%" + value + "%'"; // contains
			}
			else if (criteria == 1) {
				sql += input.getCase() ? "upper('" + value + "')" : "'" + value + "'"; // equals
			}
			else if (criteria == 2) {
				sql += input.getCase() ? "upper('" + value + "%')" : "'" + value + "%'"; // starts with
			}
			else if (criteria == 3) {
				sql += input.getCase() ? "upper('%" + value + "')" : "'%" + value + "'"; // ends with
			}
			sql += input.getOperator() ? " and " : " or ";
		}

		// return query
		return sql;
	}
}
