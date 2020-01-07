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

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.message.AddNewUserRequest;
import equinox.dataServer.remote.message.AdvancedMultiplicationTableSearchRequest;
import equinox.dataServer.remote.message.AdvancedPilotPointSearchRequest;
import equinox.dataServer.remote.message.AdvancedSpectrumSearchRequest;
import equinox.dataServer.remote.message.BasicMultiplicationTableSearchRequest;
import equinox.dataServer.remote.message.BasicPilotPointSearchRequest;
import equinox.dataServer.remote.message.BasicSpectrumSearchRequest;
import equinox.dataServer.remote.message.CheckForEquinoxUpdatesRequest;
import equinox.dataServer.remote.message.CheckForMaterialUpdatesRequest;
import equinox.dataServer.remote.message.CloseAccessRequestRequest;
import equinox.dataServer.remote.message.CloseBugReportRequest;
import equinox.dataServer.remote.message.CloseWishRequest;
import equinox.dataServer.remote.message.DatabaseQueryPermissionDenied;
import equinox.dataServer.remote.message.DatabaseQueryRequest;
import equinox.dataServer.remote.message.DeleteHelpVideoRequest;
import equinox.dataServer.remote.message.DeleteMultiplicationTableRequest;
import equinox.dataServer.remote.message.DeletePilotPointRequest;
import equinox.dataServer.remote.message.DeletePluginRequest;
import equinox.dataServer.remote.message.DeleteSpectrumRequest;
import equinox.dataServer.remote.message.DeleteUsersRequest;
import equinox.dataServer.remote.message.DownloadHelpVideoRequest;
import equinox.dataServer.remote.message.DownloadPilotPointAttributesRequest;
import equinox.dataServer.remote.message.DownloadPilotPointImagesRequest;
import equinox.dataServer.remote.message.DownloadPilotPointRequest;
import equinox.dataServer.remote.message.DownloadPilotPointsRequest;
import equinox.dataServer.remote.message.DownloadSampleInputRequest;
import equinox.dataServer.remote.message.EditUserPermissionsRequest;
import equinox.dataServer.remote.message.ExecuteSQLStatementRequest;
import equinox.dataServer.remote.message.GetAccessRequestCountRequest;
import equinox.dataServer.remote.message.GetAccessRequestsRequest;
import equinox.dataServer.remote.message.GetAircraftProgramsForPilotPointsRequest;
import equinox.dataServer.remote.message.GetAircraftProgramsForSpectraRequest;
import equinox.dataServer.remote.message.GetAircraftSectionsForPilotPointsRequest;
import equinox.dataServer.remote.message.GetAircraftSectionsForSpectraRequest;
import equinox.dataServer.remote.message.GetBugReportCountRequest;
import equinox.dataServer.remote.message.GetBugReportsRequest;
import equinox.dataServer.remote.message.GetDataQueriesRequest;
import equinox.dataServer.remote.message.GetFatigueMaterialsRequest;
import equinox.dataServer.remote.message.GetFatigueMissionsForPilotPointsRequest;
import equinox.dataServer.remote.message.GetFatigueMissionsForSpectraRequest;
import equinox.dataServer.remote.message.GetHelpVideosRequest;
import equinox.dataServer.remote.message.GetLinearMaterialsRequest;
import equinox.dataServer.remote.message.GetPilotPointCountsRequest;
import equinox.dataServer.remote.message.GetPilotPointImageRequest;
import equinox.dataServer.remote.message.GetPilotPointImagesRequest;
import equinox.dataServer.remote.message.GetPluginInfoRequest;
import equinox.dataServer.remote.message.GetPreffasMaterialsRequest;
import equinox.dataServer.remote.message.GetSearchHitsRequest;
import equinox.dataServer.remote.message.GetSpectrumCountsRequest;
import equinox.dataServer.remote.message.GetUserPermissionsRequest;
import equinox.dataServer.remote.message.GetWishCountRequest;
import equinox.dataServer.remote.message.GetWishesRequest;
import equinox.dataServer.remote.message.LikeWishRequest;
import equinox.dataServer.remote.message.Login;
import equinox.dataServer.remote.message.PlotContributionStatisticsRequest;
import equinox.dataServer.remote.message.PlotPilotPointCountRequest;
import equinox.dataServer.remote.message.PlotSpectrumCountRequest;
import equinox.dataServer.remote.message.PlotSpectrumSizeRequest;
import equinox.dataServer.remote.message.ResetExchangeTableRequest;
import equinox.dataServer.remote.message.SavePilotPointImageRequest;
import equinox.dataServer.remote.message.SubmitAccessRequestRequest;
import equinox.dataServer.remote.message.SubmitBugReportRequest;
import equinox.dataServer.remote.message.SubmitWishRequest;
import equinox.dataServer.remote.message.UpdateMultiplicationTableRequest;
import equinox.dataServer.remote.message.UpdatePilotPointRequest;
import equinox.dataServer.remote.message.UpdateSpectrumRequest;
import equinox.dataServer.remote.message.UploadContainerUpdateRequest;
import equinox.dataServer.remote.message.UploadDamageContributionsRequest;
import equinox.dataServer.remote.message.UploadHelpVideoRequest;
import equinox.dataServer.remote.message.UploadMaterialsRequest;
import equinox.dataServer.remote.message.UploadMultiplicationTablesRequest;
import equinox.dataServer.remote.message.UploadPilotPointsRequest;
import equinox.dataServer.remote.message.UploadPluginRequest;
import equinox.dataServer.remote.message.UploadSampleInputsRequest;
import equinox.dataServer.remote.message.UploadSpectraRequest;
import equinox.dataServer.task.AddNewUser;
import equinox.dataServer.task.AdvancedMultiplicationTableSearch;
import equinox.dataServer.task.AdvancedPilotPointSearch;
import equinox.dataServer.task.AdvancedSpectrumSearch;
import equinox.dataServer.task.BasicMultiplicationTableSearch;
import equinox.dataServer.task.BasicPilotPointSearch;
import equinox.dataServer.task.BasicSpectrumSearch;
import equinox.dataServer.task.CheckForEquinoxContainerUpdates;
import equinox.dataServer.task.CheckForMaterialUpdates;
import equinox.dataServer.task.CloseAccessRequest;
import equinox.dataServer.task.CloseBugReport;
import equinox.dataServer.task.CloseWish;
import equinox.dataServer.task.DatabaseQueryTask;
import equinox.dataServer.task.DeleteHelpVideo;
import equinox.dataServer.task.DeleteMultiplicationTable;
import equinox.dataServer.task.DeletePilotPoint;
import equinox.dataServer.task.DeletePlugin;
import equinox.dataServer.task.DeleteSpectrum;
import equinox.dataServer.task.DeleteUsers;
import equinox.dataServer.task.DownloadHelpVideo;
import equinox.dataServer.task.DownloadPilotPoint;
import equinox.dataServer.task.DownloadPilotPointAttributes;
import equinox.dataServer.task.DownloadPilotPointImages;
import equinox.dataServer.task.DownloadPilotPoints;
import equinox.dataServer.task.DownloadSampleInput;
import equinox.dataServer.task.EditUserPermissions;
import equinox.dataServer.task.ExecuteSQLStatement;
import equinox.dataServer.task.GetAccessRequestCount;
import equinox.dataServer.task.GetAccessRequests;
import equinox.dataServer.task.GetAircraftProgramsForPilotPoints;
import equinox.dataServer.task.GetAircraftProgramsForSpectra;
import equinox.dataServer.task.GetAircraftSectionsForPilotPoints;
import equinox.dataServer.task.GetAircraftSectionsForSpectra;
import equinox.dataServer.task.GetBugReportCount;
import equinox.dataServer.task.GetBugReports;
import equinox.dataServer.task.GetDataQueries;
import equinox.dataServer.task.GetFatigueMaterials;
import equinox.dataServer.task.GetFatigueMissionsForPilotPoints;
import equinox.dataServer.task.GetFatigueMissionsForSpectra;
import equinox.dataServer.task.GetHelpVideos;
import equinox.dataServer.task.GetLinearMaterials;
import equinox.dataServer.task.GetPilotPointCounts;
import equinox.dataServer.task.GetPilotPointImage;
import equinox.dataServer.task.GetPilotPointImages;
import equinox.dataServer.task.GetPluginInfo;
import equinox.dataServer.task.GetPreffasMaterials;
import equinox.dataServer.task.GetSearchHits;
import equinox.dataServer.task.GetSpectrumCounts;
import equinox.dataServer.task.GetUserPermissions;
import equinox.dataServer.task.GetWishCount;
import equinox.dataServer.task.GetWishes;
import equinox.dataServer.task.LikeWish;
import equinox.dataServer.task.PlotContributionStatistics;
import equinox.dataServer.task.PlotPilotPointCount;
import equinox.dataServer.task.PlotSpectrumCount;
import equinox.dataServer.task.PlotSpectrumSize;
import equinox.dataServer.task.ProcessLogin;
import equinox.dataServer.task.ResetExchangeTable;
import equinox.dataServer.task.SavePilotPointImage;
import equinox.dataServer.task.SubmitAccessRequest;
import equinox.dataServer.task.SubmitBugReport;
import equinox.dataServer.task.SubmitWish;
import equinox.dataServer.task.UpdateMultiplicationTable;
import equinox.dataServer.task.UpdatePilotPoint;
import equinox.dataServer.task.UpdateSpectrum;
import equinox.dataServer.task.UploadDamageContributions;
import equinox.dataServer.task.UploadEquinoxContainerUpdate;
import equinox.dataServer.task.UploadHelpVideo;
import equinox.dataServer.task.UploadMaterials;
import equinox.dataServer.task.UploadMultiplicationTables;
import equinox.dataServer.task.UploadPilotPoints;
import equinox.dataServer.task.UploadPlugin;
import equinox.dataServer.task.UploadSampleInputs;
import equinox.dataServer.task.UploadSpectra;
import equinox.serverUtilities.NetworkMessage;
import equinox.serverUtilities.Permission;
import equinox.serverUtilities.PermissionDenied;

/**
 * Class for lobby class. This class manages all communication between clients and server.
 *
 * @author Murat Artim
 * @date Sep 16, 2014
 * @time 6:23:43 PM
 */
public class Lobby {

	/** Server. */
	private final DataServer server_;

	/**
	 * Creates lobby.
	 *
	 * @param server
	 *            Central server of the lobby.
	 */
	public Lobby(DataServer server) {
		server_ = server;
		server_.getLogger().info("Client lobby created.");
	}

	/**
	 * Returns the server of the lobby.
	 *
	 * @return The server of the lobby.
	 */
	public DataServer getServer() {
		return server_;
	}

	/**
	 * Stops this lobby.
	 */
	public void stop() {
		server_.getLogger().info("Client lobby stopped.");
	}

	/**
	 * Responds to given message according to general room protocol.
	 *
	 * @param client
	 *            Client who sent the message.
	 * @param message
	 *            Message to respond.
	 * @throws Exception
	 *             If exception occurs during processing client message.
	 */
	public void respond(DataClient client, NetworkMessage message) throws Exception {

		// login request
		if (message instanceof Login && checkPermission(client, Permission.LOGIN_AS_ADMINISTRATOR, message)) {
			server_.incrementQueryRequests();
			server_.getThreadPool().submit(new ProcessLogin(client, (Login) message));
		}

		// data queries count request
		else if (message instanceof GetDataQueriesRequest) {
			server_.getThreadPool().submit(new GetDataQueries(client, (GetDataQueriesRequest) message));
		}

		// spectrum counts request
		else if (message instanceof GetSpectrumCountsRequest) {
			server_.getThreadPool().submit(new GetSpectrumCounts(client, (GetSpectrumCountsRequest) message));
		}

		// search hits request
		else if (message instanceof GetSearchHitsRequest) {
			server_.getThreadPool().submit(new GetSearchHits(client, (GetSearchHitsRequest) message));
		}

		// pilot point counts request
		else if (message instanceof GetPilotPointCountsRequest) {
			server_.getThreadPool().submit(new GetPilotPointCounts(client, (GetPilotPointCountsRequest) message));
		}

		// bug report count request
		else if (message instanceof GetBugReportCountRequest) {
			server_.getThreadPool().submit(new GetBugReportCount(client, (GetBugReportCountRequest) message));
		}

		// wish count request
		else if (message instanceof GetWishCountRequest) {
			server_.getThreadPool().submit(new GetWishCount(client, (GetWishCountRequest) message));
		}

		// access request count request
		else if (message instanceof GetAccessRequestCountRequest) {
			server_.getThreadPool().submit(new GetAccessRequestCount(client, (GetAccessRequestCountRequest) message));
		}

		// database query request
		else if (message instanceof DatabaseQueryRequest) {
			databaseQueryRequest(client, (DatabaseQueryRequest) message);
		}
	}

	/**
	 * Responds to database query request message form client.
	 *
	 * @param client
	 *            Requesting client.
	 * @param message
	 *            Client message.
	 * @throws Exception
	 *             If exception occurs during process.
	 */
	private void databaseQueryRequest(DataClient client, DatabaseQueryRequest message) throws Exception {

		// increment query request count statistic
		server_.incrementQueryRequests();

		// initialize analysis task
		DatabaseQueryTask task = null;

		// plugin info request
		if (message instanceof GetPluginInfoRequest && checkPermission(client, Permission.GET_EQUINOX_PLUGIN_INFO, message)) {
			task = new GetPluginInfo(server_, client, (GetPluginInfoRequest) message);
		}

		// advanced multiplication table search request
		else if (message instanceof AdvancedMultiplicationTableSearchRequest && checkPermission(client, Permission.SEARCH_MULTIPLICATION_TABLE, message)) {
			task = new AdvancedMultiplicationTableSearch(server_, client, (AdvancedMultiplicationTableSearchRequest) message);
		}

		// advanced pilot point search request
		else if (message instanceof AdvancedPilotPointSearchRequest && checkPermission(client, Permission.SEARCH_PILOT_POINT, message)) {
			task = new AdvancedPilotPointSearch(server_, client, (AdvancedPilotPointSearchRequest) message);
		}

		// advanced spectrum search request
		else if (message instanceof AdvancedSpectrumSearchRequest && checkPermission(client, Permission.SEARCH_SPECTRUM, message)) {
			task = new AdvancedSpectrumSearch(server_, client, (AdvancedSpectrumSearchRequest) message);
		}

		// basic multiplication table search request
		else if (message instanceof BasicMultiplicationTableSearchRequest && checkPermission(client, Permission.SEARCH_MULTIPLICATION_TABLE, message)) {
			task = new BasicMultiplicationTableSearch(server_, client, (BasicMultiplicationTableSearchRequest) message);
		}

		// basic pilot point search request
		else if (message instanceof BasicPilotPointSearchRequest && checkPermission(client, Permission.SEARCH_PILOT_POINT, message)) {
			task = new BasicPilotPointSearch(server_, client, (BasicPilotPointSearchRequest) message);
		}

		// basic spectrum search request
		else if (message instanceof BasicSpectrumSearchRequest && checkPermission(client, Permission.SEARCH_SPECTRUM, message)) {
			task = new BasicSpectrumSearch(server_, client, (BasicSpectrumSearchRequest) message);
		}

		// check for Equinox updates
		else if (message instanceof CheckForEquinoxUpdatesRequest && checkPermission(client, Permission.CHECK_FOR_EQUINOX_UPDATES, message)) {
			task = new CheckForEquinoxContainerUpdates(server_, client, (CheckForEquinoxUpdatesRequest) message);
		}

		// check for material updates
		else if (message instanceof CheckForMaterialUpdatesRequest && checkPermission(client, Permission.CHECK_FOR_MATERIAL_UPDATES, message)) {
			task = new CheckForMaterialUpdates(server_, client, (CheckForMaterialUpdatesRequest) message);
		}

		// close bug report
		else if (message instanceof CloseBugReportRequest && checkPermission(client, Permission.CLOSE_BUG_REPORT, message)) {
			task = new CloseBugReport(server_, client, (CloseBugReportRequest) message);
		}

		// close wish
		else if (message instanceof CloseWishRequest && checkPermission(client, Permission.CLOSE_WISH, message)) {
			task = new CloseWish(server_, client, (CloseWishRequest) message);
		}

		// delete help video
		else if (message instanceof DeleteHelpVideoRequest && checkPermission(client, Permission.DELETE_HELP_VIDEO, message)) {
			task = new DeleteHelpVideo(server_, client, (DeleteHelpVideoRequest) message);
		}

		// delete multiplication table
		else if (message instanceof DeleteMultiplicationTableRequest && checkPermission(client, Permission.DELETE_MULTIPLICATION_TABLE, message)) {
			task = new DeleteMultiplicationTable(server_, client, (DeleteMultiplicationTableRequest) message);
		}

		// delete pilot point
		else if (message instanceof DeletePilotPointRequest && checkPermission(client, Permission.DELETE_PILOT_POINT, message)) {
			task = new DeletePilotPoint(server_, client, (DeletePilotPointRequest) message);
		}

		// delete plugin
		else if (message instanceof DeletePluginRequest && checkPermission(client, Permission.DELETE_EQUINOX_PLUGIN, message)) {
			task = new DeletePlugin(server_, client, (DeletePluginRequest) message);
		}

		// delete spectrum
		else if (message instanceof DeleteSpectrumRequest && checkPermission(client, Permission.DELETE_SPECTRUM, message)) {
			task = new DeleteSpectrum(server_, client, (DeleteSpectrumRequest) message);
		}

		// download help video
		else if (message instanceof DownloadHelpVideoRequest && checkPermission(client, Permission.DOWNLOAD_HELP_VIDEO, message)) {
			task = new DownloadHelpVideo(server_, client, (DownloadHelpVideoRequest) message);
		}

		// download pilot point
		else if (message instanceof DownloadPilotPointRequest && checkPermission(client, Permission.DOWNLOAD_PILOT_POINT, message)) {
			task = new DownloadPilotPoint(server_, client, (DownloadPilotPointRequest) message);
		}

		// download pilot point images
		else if (message instanceof DownloadPilotPointImagesRequest && checkPermission(client, Permission.DOWNLOAD_PILOT_POINT, message)) {
			task = new DownloadPilotPointImages(server_, client, (DownloadPilotPointImagesRequest) message);
		}

		// download pilot point attributes
		else if (message instanceof DownloadPilotPointAttributesRequest && checkPermission(client, Permission.DOWNLOAD_PILOT_POINT, message)) {
			task = new DownloadPilotPointAttributes(server_, client, (DownloadPilotPointAttributesRequest) message);
		}

		// download pilot points
		else if (message instanceof DownloadPilotPointsRequest && checkPermission(client, Permission.DOWNLOAD_PILOT_POINT, message)) {
			task = new DownloadPilotPoints(server_, client, (DownloadPilotPointsRequest) message);
		}

		// download sample input
		else if (message instanceof DownloadSampleInputRequest && checkPermission(client, Permission.DOWNLOAD_SAMPLE_INPUT, message)) {
			task = new DownloadSampleInput(server_, client, (DownloadSampleInputRequest) message);
		}

		// execute SQL statement
		else if (message instanceof ExecuteSQLStatementRequest && checkPermission(client, Permission.EXECUTE_GENERIC_SQL_STATEMENT, message)) {
			task = new ExecuteSQLStatement(server_, client, (ExecuteSQLStatementRequest) message);
		}

		// get aircraft programs for pilot points
		else if (message instanceof GetAircraftProgramsForPilotPointsRequest) {
			task = new GetAircraftProgramsForPilotPoints(server_, client, (GetAircraftProgramsForPilotPointsRequest) message);
		}

		// get aircraft programs for spectra
		else if (message instanceof GetAircraftProgramsForSpectraRequest) {
			task = new GetAircraftProgramsForSpectra(server_, client, (GetAircraftProgramsForSpectraRequest) message);
		}

		// get aircraft sections for pilot points
		else if (message instanceof GetAircraftSectionsForPilotPointsRequest) {
			task = new GetAircraftSectionsForPilotPoints(server_, client, (GetAircraftSectionsForPilotPointsRequest) message);
		}

		// get aircraft sections for spectra
		else if (message instanceof GetAircraftSectionsForSpectraRequest) {
			task = new GetAircraftSectionsForSpectra(server_, client, (GetAircraftSectionsForSpectraRequest) message);
		}

		// get bug reports
		else if (message instanceof GetBugReportsRequest && checkPermission(client, Permission.GET_BUG_REPORTS, message)) {
			task = new GetBugReports(server_, client, (GetBugReportsRequest) message);
		}

		// get fatigue missions for pilot points
		else if (message instanceof GetFatigueMissionsForPilotPointsRequest) {
			task = new GetFatigueMissionsForPilotPoints(server_, client, (GetFatigueMissionsForPilotPointsRequest) message);
		}

		// get fatigue missions for spectra
		else if (message instanceof GetFatigueMissionsForSpectraRequest) {
			task = new GetFatigueMissionsForSpectra(server_, client, (GetFatigueMissionsForSpectraRequest) message);
		}

		// get help videos
		else if (message instanceof GetHelpVideosRequest && checkPermission(client, Permission.GET_HELP_VIDEOS, message)) {
			task = new GetHelpVideos(server_, client, (GetHelpVideosRequest) message);
		}

		// get pilot point image
		else if (message instanceof GetPilotPointImageRequest && checkPermission(client, Permission.SEARCH_PILOT_POINT, message)) {
			task = new GetPilotPointImage(server_, client, (GetPilotPointImageRequest) message);
		}

		// get wishes
		else if (message instanceof GetWishesRequest && checkPermission(client, Permission.GET_WISHES, message)) {
			task = new GetWishes(server_, client, (GetWishesRequest) message);
		}

		// like wish
		else if (message instanceof LikeWishRequest && checkPermission(client, Permission.GET_WISHES, message)) {
			task = new LikeWish(server_, client, (LikeWishRequest) message);
		}

		// plot contribution statistics
		else if (message instanceof PlotContributionStatisticsRequest && checkPermission(client, Permission.PLOT_CONTRIBUTION_STATISTICS, message)) {
			task = new PlotContributionStatistics(server_, client, (PlotContributionStatisticsRequest) message);
		}

		// plot pilot point count
		else if (message instanceof PlotPilotPointCountRequest && checkPermission(client, Permission.PLOT_PILOT_POINT_COUNT, message)) {
			task = new PlotPilotPointCount(server_, client, (PlotPilotPointCountRequest) message);
		}

		// plot spectrum count
		else if (message instanceof PlotSpectrumCountRequest && checkPermission(client, Permission.PLOT_SPECTRUM_COUNT, message)) {
			task = new PlotSpectrumCount(server_, client, (PlotSpectrumCountRequest) message);
		}

		// plot spectrum size
		else if (message instanceof PlotSpectrumSizeRequest && checkPermission(client, Permission.PLOT_SPECTRUM_SIZE, message)) {
			task = new PlotSpectrumSize(server_, client, (PlotSpectrumSizeRequest) message);
		}

		// reset exchange table
		else if (message instanceof ResetExchangeTableRequest && checkPermission(client, Permission.RESET_EXCHANGE_DATA, message)) {
			task = new ResetExchangeTable(server_, client, (ResetExchangeTableRequest) message);
		}

		// save pilot point image
		else if (message instanceof SavePilotPointImageRequest && checkPermission(client, Permission.SAVE_PILOT_POINT_IMAGE, message)) {
			task = new SavePilotPointImage(server_, client, (SavePilotPointImageRequest) message);
		}

		// submit bug report
		else if (message instanceof SubmitBugReportRequest && checkPermission(client, Permission.SUBMIT_BUG_REPORT, message)) {
			task = new SubmitBugReport(server_, client, (SubmitBugReportRequest) message);
		}

		// submit wish
		else if (message instanceof SubmitWishRequest && checkPermission(client, Permission.SUBMIT_WISH, message)) {
			task = new SubmitWish(server_, client, (SubmitWishRequest) message);
		}

		// get fatigue materials
		else if (message instanceof GetFatigueMaterialsRequest && checkPermission(client, Permission.GET_MATERIALS, message)) {
			task = new GetFatigueMaterials(server_, client, (GetFatigueMaterialsRequest) message);
		}

		// get linear materials
		else if (message instanceof GetLinearMaterialsRequest && checkPermission(client, Permission.GET_MATERIALS, message)) {
			task = new GetLinearMaterials(server_, client, (GetLinearMaterialsRequest) message);
		}

		// get preffas materials
		else if (message instanceof GetPreffasMaterialsRequest && checkPermission(client, Permission.GET_MATERIALS, message)) {
			task = new GetPreffasMaterials(server_, client, (GetPreffasMaterialsRequest) message);
		}

		// update multiplication table info
		else if (message instanceof UpdateMultiplicationTableRequest && checkPermission(client, Permission.UPDATE_MULTIPLICATION_TABLE_INFO, message)) {
			task = new UpdateMultiplicationTable(server_, client, (UpdateMultiplicationTableRequest) message);
		}

		// update pilot point info
		else if (message instanceof UpdatePilotPointRequest && checkPermission(client, Permission.UPDATE_PILOT_POINT_INFO, message)) {
			task = new UpdatePilotPoint(server_, client, (UpdatePilotPointRequest) message);
		}

		// update spectrum info
		else if (message instanceof UpdateSpectrumRequest && checkPermission(client, Permission.UPDATE_SPECTRUM_INFO, message)) {
			task = new UpdateSpectrum(server_, client, (UpdateSpectrumRequest) message);
		}

		// upload damage contributions
		else if (message instanceof UploadDamageContributionsRequest && checkPermission(client, Permission.UPLOAD_DAMAGE_CONTRIBUTIONS, message)) {
			task = new UploadDamageContributions(server_, client, (UploadDamageContributionsRequest) message);
		}

		// upload help video
		else if (message instanceof UploadHelpVideoRequest && checkPermission(client, Permission.UPLOAD_HELP_VIDEO, message)) {
			task = new UploadHelpVideo(server_, client, (UploadHelpVideoRequest) message);
		}

		// upload materials
		else if (message instanceof UploadMaterialsRequest && checkPermission(client, Permission.UPLOAD_MATERIALS, message)) {
			task = new UploadMaterials(server_, client, (UploadMaterialsRequest) message);
		}

		// upload multiplication tables
		else if (message instanceof UploadMultiplicationTablesRequest && checkPermission(client, Permission.UPLOAD_MULTIPLICATION_TABLES, message)) {
			task = new UploadMultiplicationTables(server_, client, (UploadMultiplicationTablesRequest) message);
		}

		// upload pilot points
		else if (message instanceof UploadPilotPointsRequest && checkPermission(client, Permission.UPLOAD_PILOT_POINTS, message)) {
			task = new UploadPilotPoints(server_, client, (UploadPilotPointsRequest) message);
		}

		// upload plugin
		else if (message instanceof UploadPluginRequest && checkPermission(client, Permission.UPLOAD_EQUINOX_PLUGIN, message)) {
			task = new UploadPlugin(server_, client, (UploadPluginRequest) message);
		}

		// upload sample inputs
		else if (message instanceof UploadSampleInputsRequest && checkPermission(client, Permission.UPLOAD_SAMPLE_INPUTS, message)) {
			task = new UploadSampleInputs(server_, client, (UploadSampleInputsRequest) message);
		}

		// upload spectra
		else if (message instanceof UploadSpectraRequest && checkPermission(client, Permission.UPLOAD_SPECTRA, message)) {
			task = new UploadSpectra(server_, client, (UploadSpectraRequest) message);
		}

		// upload update
		else if (message instanceof UploadContainerUpdateRequest && checkPermission(client, Permission.UPLOAD_EQUINOX_UPDATE, message)) {
			task = new UploadEquinoxContainerUpdate(server_, client, (UploadContainerUpdateRequest) message);
		}

		// get pilot point images
		else if (message instanceof GetPilotPointImagesRequest && checkPermission(client, Permission.GET_PILOT_POINT_IMAGES, message)) {
			task = new GetPilotPointImages(server_, client, (GetPilotPointImagesRequest) message);
		}

		// add new user
		else if (message instanceof AddNewUserRequest && checkPermission(client, Permission.ADD_NEW_USER, message)) {
			task = new AddNewUser(server_, client, (AddNewUserRequest) message);
		}

		// delete users
		else if (message instanceof DeleteUsersRequest && checkPermission(client, Permission.DELETE_USER, message)) {
			task = new DeleteUsers(server_, client, (DeleteUsersRequest) message);
		}

		// get user permissions
		else if (message instanceof GetUserPermissionsRequest && checkPermission(client, Permission.GET_USER_PERMISSIONS, message)) {
			task = new GetUserPermissions(server_, client, (GetUserPermissionsRequest) message);
		}

		// edit user permissions
		else if (message instanceof EditUserPermissionsRequest && checkPermission(client, Permission.EDIT_USER_PERMISSIONS, message)) {
			task = new EditUserPermissions(server_, client, (EditUserPermissionsRequest) message);
		}

		// submit user access request
		else if (message instanceof SubmitAccessRequestRequest && checkPermission(client, Permission.SUBMIT_ACCESS_REQUEST, message)) {
			task = new SubmitAccessRequest(server_, client, (SubmitAccessRequestRequest) message);
		}

		// get user access requests
		else if (message instanceof GetAccessRequestsRequest && checkPermission(client, Permission.GET_ACCESS_REQUESTS, message)) {
			task = new GetAccessRequests(server_, client, (GetAccessRequestsRequest) message);
		}

		// close user access request
		else if (message instanceof CloseAccessRequestRequest && checkPermission(client, Permission.CLOSE_ACCESS_REQUEST, message)) {
			task = new CloseAccessRequest(server_, client, (CloseAccessRequestRequest) message);
		}

		// submit task
		if (task != null) {
			server_.getThreadPool().submit(task);
		}
	}

	/**
	 * Returns true if client has the given permission.
	 *
	 * @param client
	 *            Client.
	 * @param permission
	 *            Permission to check.
	 * @param message
	 *            The message sent from client.
	 * @return True if client has the given permission, or sends <code>PermissionDenied</code> message and return false.
	 */
	private static boolean checkPermission(DataClient client, Permission permission, NetworkMessage message) {

		// client doesn't have necessary permission
		if (!client.hasPermission(permission)) {

			// database query request
			if (message instanceof DatabaseQueryRequest) {
				DatabaseQueryPermissionDenied response = new DatabaseQueryPermissionDenied(permission);
				response.setListenerHashCode(((DatabaseQueryRequest) message).getListenerHashCode());
				client.sendMessage(response);
			}

			// other request
			else {
				client.sendMessage(new PermissionDenied(permission));
			}

			// return
			return false;
		}

		// client has permission
		return true;
	}
}