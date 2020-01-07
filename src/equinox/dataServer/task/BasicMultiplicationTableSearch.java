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
import equinox.dataServer.remote.data.MultiplicationTableInfo;
import equinox.dataServer.remote.data.MultiplicationTableInfo.MultiplicationTableInfoType;
import equinox.dataServer.remote.data.SearchInput;
import equinox.dataServer.remote.message.BasicMultiplicationTableSearchRequest;
import equinox.dataServer.remote.message.BasicMultiplicationTableSearchResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for basic multiplication table search task.
 *
 * @author Murat Artim
 * @date 24 Jan 2018
 * @time 14:39:04
 */
public class BasicMultiplicationTableSearch extends DatabaseQueryTask {

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
	public BasicMultiplicationTableSearch(DataServer server, DataClient client, BasicMultiplicationTableSearchRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for performing loadcase factor file search.");

		// create response message
		BasicMultiplicationTableSearchResponse response = new BasicMultiplicationTableSearchResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get search input
		BasicSearchInput input = ((BasicMultiplicationTableSearchRequest) request_).getInput();

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
	private static String buildSQL(BasicSearchInput input) throws Exception {

		// initialize parameters
		String sql = "select id, spectrum_name, pilot_point_name, name, ac_program, ac_section, fat_mission, description, delivery_ref_num, issue, data_url from mult_tables where ";

		// add search items
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.NAME, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.SPECTRUM_NAME, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.PILOT_POINT_NAME, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.AC_PROGRAM, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.AC_SECTION, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.FAT_MISSION, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.DELIVERY_REF, input);
		sql += buildQueryForStringBasedItem(MultiplicationTableInfoType.DESCRIPTION, input);

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
	private static String buildQueryForStringBasedItem(MultiplicationTableInfoType type, BasicSearchInput input) throws Exception {

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
