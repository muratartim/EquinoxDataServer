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

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.UploadMaterialsRequest;
import equinox.dataServer.remote.message.UploadMaterialsResponse;
import equinox.dataServer.server.DataServer;
import equinox.dataServer.utility.Utility;
import equinox.serverUtilities.FilerConnection;
import equinox.serverUtilities.SharedFileInfo;
import equinox.serverUtilities.SharedFileInfo.SharedFileInfoType;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Class for upload materials task.
 *
 * @author Murat Artim
 * @date 13 Mar 2018
 * @time 10:43:32
 */
public class UploadMaterials extends DatabaseQueryTask {

	/**
	 * Creates upload materials task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public UploadMaterials(DataServer server, DataClient client, UploadMaterialsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for uploading materials.");

		// create response message
		UploadMaterialsResponse response = new UploadMaterialsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		UploadMaterialsRequest request = (UploadMaterialsRequest) request_;
		SharedFileInfo info = request.getSharedFileInfo();

		// download input file
		Path inputFile = downloadInputFile(info);

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// update materials
				uploadMaterialData(connection, inputFile);

				// commit updates
				connection.commit();
				connection.setAutoCommit(true);
			}

			// exception occurred during process
			catch (Exception e) {

				// roll back updates
				if (connection != null) {
					connection.rollback();
					connection.setAutoCommit(true);
				}

				// propagate exception
				throw e;
			}
		}

		// set updated
		response.setUploaded(true);

		// respond to client
		client_.sendMessage(response);
	}

	/**
	 * Starts upload process.
	 *
	 * @param connection
	 *            Database connection.
	 * @param inputFile
	 *            Input Excel file.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void uploadMaterialData(Connection connection, Path inputFile) throws Exception {

		// initialize variables
		Workbook workbook = null;

		try {

			// get workbook
			workbook = Workbook.getWorkbook(inputFile.toFile());

			// load fatigue data
			sendProgressMessage("Uploading fatigue material data to database...");
			loadFatigueData(workbook.getSheet("Fatigue"), connection);

			// load Linear data
			sendProgressMessage("Uploading linear propagation material data to database...");
			loadPropagationData("linear_materials", workbook.getSheet("Linear"), connection);

			// load Preffas data
			sendProgressMessage("Uploading preffas propagation material data to database...");
			loadPropagationData("preffas_materials", workbook.getSheet("Preffas"), connection);

			// load other propagation data
			sendProgressMessage("Uploading other propagation material data to database...");
			loadPropagationData("linear_materials", workbook.getSheet("Other"), connection);
			loadPropagationData("preffas_materials", workbook.getSheet("Other"), connection);
		}

		// close workbook
		finally {
			if (workbook != null) {
				workbook.close();
			}
		}
	}

	/**
	 * Loads linear material data.
	 *
	 * @param tableName
	 *            Database table name.
	 * @param dataSheet
	 *            Linear sheet.
	 * @param connection
	 *            Database connection.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void loadPropagationData(String tableName, Sheet dataSheet, Connection connection) throws Exception {

		// create statement
		String sql = "insert into tableName(name, specification, library_version, family, orientation, configuration, par_ceff, par_m, par_a, par_b, par_c, par_ftu, par_fty, isami_version) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(sql.replaceFirst("tableName", tableName))) {

			// set location
			int startRow = 1, endRow = dataSheet.getRows() - 1;

			// loop over rows
			for (int i = startRow; i <= endRow; i++) {

				// get material data
				String name = dataSheet.getCell(0, i).getContents().trim();
				String specification = dataSheet.getCell(1, i).getContents().trim();
				String libraryVersion = dataSheet.getCell(2, i).getContents().trim();
				String family = dataSheet.getCell(3, i).getContents().trim();
				String orientation = dataSheet.getCell(4, i).getContents().trim();
				String configuration = dataSheet.getCell(5, i).getContents().trim();
				double parCeff = Double.parseDouble(dataSheet.getCell(6, i).getContents().trim());
				double parM = Double.parseDouble(dataSheet.getCell(7, i).getContents().trim());
				double parA = Double.parseDouble(dataSheet.getCell(8, i).getContents().trim());
				double parB = Double.parseDouble(dataSheet.getCell(9, i).getContents().trim());
				double parC = Double.parseDouble(dataSheet.getCell(10, i).getContents().trim());
				double parFtu = Double.parseDouble(dataSheet.getCell(11, i).getContents().trim());
				double parFty = Double.parseDouble(dataSheet.getCell(12, i).getContents().trim());
				String isamiVersion = dataSheet.getCell(13, i).getContents().trim();

				// set parameters
				statement.setString(1, name);
				if (specification == null || specification.isEmpty()) {
					statement.setNull(2, java.sql.Types.VARCHAR);
				}
				else {
					statement.setString(2, specification);
				}
				if (libraryVersion == null || libraryVersion.isEmpty()) {
					statement.setNull(3, java.sql.Types.VARCHAR);
				}
				else {
					statement.setString(3, libraryVersion);
				}
				if (family == null || family.isEmpty()) {
					statement.setNull(4, java.sql.Types.VARCHAR);
				}
				else {
					statement.setString(4, family);
				}
				if (orientation == null || orientation.isEmpty()) {
					statement.setNull(5, java.sql.Types.VARCHAR);
				}
				else {
					statement.setString(5, orientation);
				}
				if (configuration == null || configuration.isEmpty()) {
					statement.setNull(6, java.sql.Types.VARCHAR);
				}
				else {
					statement.setString(6, configuration);
				}
				statement.setDouble(7, parCeff);
				statement.setDouble(8, parM);
				statement.setDouble(9, parA);
				statement.setDouble(10, parB);
				statement.setDouble(11, parC);
				statement.setDouble(12, parFtu);
				statement.setDouble(13, parFty);
				statement.setString(14, isamiVersion);

				// execute update
				statement.executeUpdate();
			}
		}
	}

	/**
	 * Loads fatigue material data.
	 *
	 * @param fatigueSheet
	 *            Fatigue sheet.
	 * @param connection
	 *            Database connection.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void loadFatigueData(Sheet fatigueSheet, Connection connection) throws Exception {

		// create statement
		String sql = "insert into fatigue_materials(name, specification, library_version, family, orientation, configuration, par_p, par_q, par_m, isami_version) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			// set location
			int startRow = 1, endRow = fatigueSheet.getRows() - 1;

			// loop over rows
			for (int i = startRow; i <= endRow; i++) {

				// get material data
				String name = fatigueSheet.getCell(0, i).getContents().trim();
				String specification = fatigueSheet.getCell(1, i).getContents().trim();
				String libraryVersion = fatigueSheet.getCell(2, i).getContents().trim();
				String family = fatigueSheet.getCell(3, i).getContents().trim();
				String orientation = fatigueSheet.getCell(4, i).getContents().trim();
				String configuration = fatigueSheet.getCell(5, i).getContents().trim();
				double parP = Double.parseDouble(fatigueSheet.getCell(6, i).getContents().trim());
				double parQ = Double.parseDouble(fatigueSheet.getCell(7, i).getContents().trim());
				double parM = Double.parseDouble(fatigueSheet.getCell(8, i).getContents().trim());
				String isamiVersion = fatigueSheet.getCell(9, i).getContents().trim();

				// set parameters
				statement.setString(1, name);
				if (specification == null || specification.isEmpty()) {
					statement.setNull(2, java.sql.Types.VARCHAR);
				}
				else {
					statement.setString(2, specification);
				}
				if (libraryVersion == null || libraryVersion.isEmpty()) {
					statement.setNull(3, java.sql.Types.VARCHAR);
				}
				else {
					statement.setString(3, libraryVersion);
				}
				if (family == null || family.isEmpty()) {
					statement.setNull(4, java.sql.Types.VARCHAR);
				}
				else {
					statement.setString(4, family);
				}
				if (orientation == null || orientation.isEmpty()) {
					statement.setNull(5, java.sql.Types.VARCHAR);
				}
				else {
					statement.setString(5, orientation);
				}
				if (configuration == null || configuration.isEmpty()) {
					statement.setNull(6, java.sql.Types.VARCHAR);
				}
				else {
					statement.setString(6, configuration);
				}
				statement.setDouble(7, parP);
				statement.setDouble(8, parQ);
				statement.setDouble(9, parM);
				statement.setString(10, isamiVersion);

				// execute update
				statement.executeUpdate();
			}
		}
	}

	/**
	 * Downloads input file from filer.
	 *
	 * @param info
	 *            Input file info.
	 * @return Input data file.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private Path downloadInputFile(SharedFileInfo info) throws Exception {

		// create path to input archive
		Path workingDirectory = getWorkingDirectory();
		Path inputArchive = workingDirectory.resolve("input.zip");

		// get URL to data
		String url = (String) info.getInfo(SharedFileInfoType.DATA_URL);

		// download from server
		try (FilerConnection filer = getFilerConnection()) {
			filer.getSftpChannel().get(url, inputArchive.toString());
		}

		// extract input files from archive
		sendProgressMessage("Extracting input SIGMA file...");
		Utility.extractAllFilesFromZIP(inputArchive, workingDirectory);

		// get input excel file
		String filename = (String) info.getInfo(SharedFileInfoType.FILE_NAME);
		Path inputFile = workingDirectory.resolve(filename);
		if (!Files.exists(inputFile))
			throw new Exception("Cannot find input Excel file for uploading materials.");

		// return input file
		return inputFile;
	}
}
