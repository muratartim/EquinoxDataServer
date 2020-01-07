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
import equinox.dataServer.remote.data.BasicSearchInput;
import equinox.dataServer.remote.data.PilotPointInfo;
import equinox.dataServer.remote.data.PilotPointInfo.PilotPointInfoType;
import equinox.dataServer.remote.data.SearchInput;
import equinox.dataServer.remote.message.BasicPilotPointSearchRequest;
import equinox.dataServer.remote.message.BasicPilotPointSearchResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for basic pilot point search task.
 *
 * @author Murat Artim
 * @date 24 Jan 2018
 * @time 15:07:37
 */
public class BasicPilotPointSearch extends DatabaseQueryTask {

	/**
	 * Creates advanced multiplication table search task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public BasicPilotPointSearch(DataServer server, DataClient client, BasicPilotPointSearchRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for performing pilot point search.");

		// create response message
		BasicPilotPointSearchResponse response = new BasicPilotPointSearchResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get search input
		BasicSearchInput input = ((BasicPilotPointSearchRequest) request_).getInput();

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
	private static String buildSQL(BasicSearchInput input) throws Exception {

		// create SQL query
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
	private static String buildQueryForStringBasedItem(PilotPointInfoType type, BasicSearchInput input) throws Exception {

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
