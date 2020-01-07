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
import java.sql.ResultSet;
import java.sql.Statement;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.UploadDamageContributionsRequest;
import equinox.dataServer.remote.message.UploadDamageContributionsResponse;
import equinox.dataServer.server.DataServer;
import equinox.dataServer.utility.Utility;
import equinox.serverUtilities.FilerConnection;
import equinox.serverUtilities.SharedFileInfo;
import equinox.serverUtilities.SharedFileInfo.SharedFileInfoType;
import jxl.Sheet;
import jxl.Workbook;

/**
 * Class for upload damage contributions task.
 *
 * @author Murat Artim
 * @date 1 Mar 2018
 * @time 18:24:35
 */
public class UploadDamageContributions extends DatabaseQueryTask {

	/**
	 * Creates upload damage contributions task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public UploadDamageContributions(DataServer server, DataClient client, UploadDamageContributionsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for uploading damage contributions.");

		// create response message
		UploadDamageContributionsResponse response = new UploadDamageContributionsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		UploadDamageContributionsRequest request = (UploadDamageContributionsRequest) request_;
		SharedFileInfo info = request.getSharedFileInfo();

		// download input file
		Path inputFile = downloadInputFile(info);

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// update spectrum info
				uploadData(connection, inputFile);

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
	 * Uploads damage contribution data to global database.
	 *
	 * @param connection
	 *            Database connection.
	 * @param inputFile
	 *            Input Excel file.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private static void uploadData(Connection connection, Path inputFile) throws Exception {

		// initialize variables
		Workbook workbook = null;

		try {

			// get workbook
			workbook = Workbook.getWorkbook(inputFile.toFile());

			// get worksheet
			Sheet worksheet = workbook.getSheet("Damage Contributions");

			// cannot find worksheet
			if (worksheet == null)
				throw new Exception("Cannot find worksheet 'Damage Contributions' in input XLS file '" + inputFile.getFileName().toString() + "'. Aborting damage contribution upload.");

			// prepare statement to insert damage contributions
			String sql = "insert into damage_contributions(spectrum_name, pp_name, ac_program, ac_section, fat_mission) values(?, ?, ?, ?, ?)";
			try (PreparedStatement insertDamageContributions = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

				// prepare statement to insert steady contributions
				sql = "insert into steady_contributions(damcont_id, gag, dp, dt, oneg) values(?, ?, ?, ?, ?)";
				try (PreparedStatement insertSteadyContributions = connection.prepareStatement(sql)) {

					// prepare statement to insert increment contributions
					sql = "insert into increment_contributions(damcont_id, event, contribution) values(?, ?, ?)";
					try (PreparedStatement insertIncrementContributions = connection.prepareStatement(sql)) {

						// loop over rows
						for (int i = 1; i < worksheet.getRows(); i++) {

							// get contribution info
							String program = worksheet.getCell(0, i).getContents().trim();
							String section = worksheet.getCell(1, i).getContents().trim();
							String mission = worksheet.getCell(2, i).getContents().trim();
							String spectrumName = worksheet.getCell(3, i).getContents().trim();
							String ppName = worksheet.getCell(4, i).getContents().trim();

							// invalid value given
							if (spectrumName == null || spectrumName.isEmpty())
								throw new Exception("Invalid spectrum name encountered at row " + i + " in input XLS file '" + inputFile.getFileName().toString() + "'. Aborting damage contribution upload.");
							if (ppName == null || ppName.isEmpty())
								throw new Exception("Invalid pilot point name encountered at row " + i + " in input XLS file '" + inputFile.getFileName().toString() + "'. Aborting damage contribution upload.");
							if (program == null || program.isEmpty())
								throw new Exception("Invalid aircraft program encountered at row " + i + " in input XLS file '" + inputFile.getFileName().toString() + "'. Aborting damage contribution upload.");
							if (section == null || section.isEmpty())
								throw new Exception("Invalid aircraft section encountered at row " + i + " in input XLS file '" + inputFile.getFileName().toString() + "'. Aborting damage contribution upload.");
							if (mission == null || mission.isEmpty())
								throw new Exception("Invalid fatigue mission encountered at row " + i + " in input XLS file '" + inputFile.getFileName().toString() + "'. Aborting damage contribution upload.");

							// insert damage contributions
							insertDamageContributions.setString(1, spectrumName);
							insertDamageContributions.setString(2, ppName);
							insertDamageContributions.setString(3, program);
							insertDamageContributions.setString(4, section);
							insertDamageContributions.setString(5, mission);
							insertDamageContributions.executeUpdate();

							// get output file ID
							int damContID = -1;
							try (ResultSet resultSet = insertDamageContributions.getGeneratedKeys()) {
								if (resultSet.next()) {
									damContID = resultSet.getBigDecimal(1).intValue();
								}
							}

							// get steady contributions
							String oneg = worksheet.getCell(5, i).getContents().trim();
							String gag = worksheet.getCell(6, i).getContents().trim();
							String dp = worksheet.getCell(7, i).getContents().trim();
							String dt = worksheet.getCell(8, i).getContents().trim();

							// insert steady contributions
							insertSteadyContributions.setInt(1, damContID);
							if (gag == null || gag.isEmpty()) {
								insertSteadyContributions.setNull(2, java.sql.Types.DOUBLE);
							}
							else {
								insertSteadyContributions.setDouble(2, Double.parseDouble(gag));
							}
							if (dp == null || dp.isEmpty()) {
								insertSteadyContributions.setNull(3, java.sql.Types.DOUBLE);
							}
							else {
								insertSteadyContributions.setDouble(3, Double.parseDouble(dp));
							}
							if (dt == null || dt.isEmpty()) {
								insertSteadyContributions.setNull(4, java.sql.Types.DOUBLE);
							}
							else {
								insertSteadyContributions.setDouble(4, Double.parseDouble(dt));
							}
							if (oneg == null || oneg.isEmpty()) {
								insertSteadyContributions.setNull(5, java.sql.Types.DOUBLE);
							}
							else {
								insertSteadyContributions.setDouble(5, Double.parseDouble(oneg));
							}
							insertSteadyContributions.executeUpdate();

							// loop over increment contributions
							insertIncrementContributions.setInt(1, damContID);
							for (int j = 9; j < worksheet.getColumns(); j++) {

								// get event and value
								String event = worksheet.getCell(j, 0).getContents().trim();
								String value = worksheet.getCell(j, i).getContents().trim();

								// invalid value given
								if (event == null || event.isEmpty())
									throw new Exception("Invalid increment event name encountered at row " + i + ", column " + j + " in input XLS file '" + inputFile.getFileName().toString() + "'. Aborting damage contribution upload.");
								if (value == null || value.isEmpty())
									throw new Exception("Invalid increment contribution value encountered at row " + i + ", column " + j + " in input XLS file '" + inputFile.getFileName().toString() + "'. Aborting damage contribution upload.");

								// insert increment contribution
								insertIncrementContributions.setString(2, event);
								insertIncrementContributions.setDouble(3, Double.parseDouble(value));
								insertIncrementContributions.executeUpdate();
							}
						}
					}
				}
			}
		}

		// close workbook
		finally {
			if (workbook != null) {
				workbook.close();
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
			throw new Exception("Cannot find input Excel file for uploading damage contributions.");

		// return input file
		return inputFile;
	}
}
