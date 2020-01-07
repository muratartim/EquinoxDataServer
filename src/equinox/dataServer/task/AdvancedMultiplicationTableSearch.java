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
import equinox.dataServer.remote.data.MultiplicationTableInfo;
import equinox.dataServer.remote.data.MultiplicationTableInfo.MultiplicationTableInfoType;
import equinox.dataServer.remote.data.MultiplicationTableSearchInput;
import equinox.dataServer.remote.data.SearchInput;
import equinox.dataServer.remote.data.SearchItem;
import equinox.dataServer.remote.message.AdvancedMultiplicationTableSearchRequest;
import equinox.dataServer.remote.message.AdvancedMultiplicationTableSearchResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for advanced multiplication table search task.
 *
 * @author Murat Artim
 * @date 23 Jan 2018
 * @time 00:42:35
 */
public class AdvancedMultiplicationTableSearch extends DatabaseQueryTask {

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
	public AdvancedMultiplicationTableSearch(DataServer server, DataClient client, AdvancedMultiplicationTableSearchRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for performing loadcase factor file search.");

		// create response message
		AdvancedMultiplicationTableSearchResponse response = new AdvancedMultiplicationTableSearchResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get search input
		MultiplicationTableSearchInput input = ((AdvancedMultiplicationTableSearchRequest) request_).getInput();

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

						// get spectrum info
						MultiplicationTableInfo info = new MultiplicationTableInfo();
						for (MultiplicationTableInfoType infoType : MultiplicationTableInfoType.values()) {
							info.setInfo(infoType, resultSet.getObject(infoType.getColumnName()));
						}

						// add info to response
						response.add(info);

						// add programs
						uniquePrograms.add(resultSet.getString(MultiplicationTableInfoType.AC_PROGRAM.getColumnName()));
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
	private static String buildSQL(MultiplicationTableSearchInput input) throws Exception {

		// initialize parameters
		String sql = "select * from mult_tables where ";

		// add search items
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.NAME, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.SPECTRUM_NAME, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.PILOT_POINT_NAME, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.AC_PROGRAM, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.AC_SECTION, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.FAT_MISSION, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.ISSUE, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.DELIVERY_REF, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.DESCRIPTION, input);

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
	private static String buildQueryForStringBasedItem(MultiplicationTableInfoType type, MultiplicationTableSearchInput input) throws Exception {

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
