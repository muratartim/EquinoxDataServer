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
package equinox.dataServer.remote;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.jfree.data.DefaultKeyedValues;
import org.jfree.data.DefaultKeyedValues2D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetGroup;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import encoder.Base64Encoder;
import equinox.dataServer.remote.data.AccessRequest;
import equinox.dataServer.remote.data.AccessRequest.AccessRequestInfo;
import equinox.dataServer.remote.data.AircraftModelInfo;
import equinox.dataServer.remote.data.AircraftModelInfo.AircraftModelInfoType;
import equinox.dataServer.remote.data.BasicSearchInput;
import equinox.dataServer.remote.data.BugReport;
import equinox.dataServer.remote.data.BugReport.BugReportInfo;
import equinox.dataServer.remote.data.ContributionType;
import equinox.dataServer.remote.data.DownloadInfo;
import equinox.dataServer.remote.data.EquinoxUpdate;
import equinox.dataServer.remote.data.EquinoxUpdate.EquinoxUpdateInfoType;
import equinox.dataServer.remote.data.FatigueMaterial;
import equinox.dataServer.remote.data.HelpVideoInfo;
import equinox.dataServer.remote.data.HelpVideoInfo.HelpVideoInfoType;
import equinox.dataServer.remote.data.LinearMaterial;
import equinox.dataServer.remote.data.Material;
import equinox.dataServer.remote.data.MultiplicationTableInfo;
import equinox.dataServer.remote.data.MultiplicationTableInfo.MultiplicationTableInfoType;
import equinox.dataServer.remote.data.MultiplicationTableSearchInput;
import equinox.dataServer.remote.data.PeriodicDataServerStatistic;
import equinox.dataServer.remote.data.PilotPointImageType;
import equinox.dataServer.remote.data.PilotPointInfo;
import equinox.dataServer.remote.data.PilotPointInfo.PilotPointInfoType;
import equinox.dataServer.remote.data.PilotPointSearchInput;
import equinox.dataServer.remote.data.PreffasMaterial;
import equinox.dataServer.remote.data.SampleInputInfo;
import equinox.dataServer.remote.data.SearchInput;
import equinox.dataServer.remote.data.SearchItem;
import equinox.dataServer.remote.data.ServerPluginInfo;
import equinox.dataServer.remote.data.ServerPluginInfo.PluginInfoType;
import equinox.dataServer.remote.data.SpectrumInfo;
import equinox.dataServer.remote.data.SpectrumInfo.SpectrumInfoType;
import equinox.dataServer.remote.data.SpectrumSearchInput;
import equinox.dataServer.remote.data.UpdateInfo;
import equinox.dataServer.remote.data.UpdateInfo.UpdateInfoType;
import equinox.dataServer.remote.data.UserLocation;
import equinox.dataServer.remote.data.Wish;
import equinox.dataServer.remote.data.Wish.WishInfo;
import equinox.dataServer.remote.listener.DataMessageListener;
import equinox.dataServer.remote.message.*;
import equinox.serverUtilities.AdministratorMessage;
import equinox.serverUtilities.BigMessage;
import equinox.serverUtilities.FilerConnection;
import equinox.serverUtilities.NetworkMessage;
import equinox.serverUtilities.PartialMessage;
import equinox.serverUtilities.Permission;
import equinox.serverUtilities.PermissionDenied;
import equinox.serverUtilities.ServerUtility;
import equinox.serverUtilities.SharedFileInfo;
import equinox.serverUtilities.SharedFileInfo.SharedFileInfoType;
import equinox.serverUtilities.SplitMessage;

/**
 * Class for object registry. This class registers objects that are going to be sent over the network.
 *
 * @version 1.0
 * @author Murat Artim
 * @time 4:01:25 PM
 * @date Jul 10, 2011
 */
public class Registry {

	/**
	 * This registers objects that are going to be sent over the network.
	 *
	 * @param endPoint
	 *            End point to retrieve the kryo object serializer.
	 */
	public static void register(EndPoint endPoint) {

		// get object serializer
		Kryo kryo = endPoint.getKryo();

		// register JDK classes
		kryo.register(String[].class);
		kryo.register(char[].class);
		kryo.register(byte[].class);
		kryo.register(int[].class);
		kryo.register(int[][].class);
		kryo.register(long[].class);
		kryo.register(boolean[].class);
		kryo.register(List.class);
		kryo.register(ArrayList.class);
		kryo.register(Vector.class);
		kryo.register(HashMap.class);
		kryo.register(Exception.class);
		kryo.register(StackTraceElement.class);
		kryo.register(Timestamp.class);
		kryo.register(Date.class);
		kryo.register(Map.class);
		kryo.register(LinkedHashMap.class);

		// register utility classes
		kryo.register(SplitMessage.class);
		kryo.register(FilerConnection.class);
		kryo.register(Base64Encoder.class);
		kryo.register(ServerUtility.class);

		// register data classes
		kryo.register(PeriodicDataServerStatistic.class);
		kryo.register(PeriodicDataServerStatistic[].class);
		kryo.register(Permission.class);
		kryo.register(Permission[].class);
		kryo.register(ServerPluginInfo.class);
		kryo.register(PluginInfoType.class);
		kryo.register(SearchInput.class);
		kryo.register(MultiplicationTableSearchInput.class);
		kryo.register(MultiplicationTableInfo.class);
		kryo.register(MultiplicationTableInfoType.class);
		kryo.register(SearchItem.class);
		kryo.register(DownloadInfo.class);
		kryo.register(PilotPointInfo.class);
		kryo.register(PilotPointInfoType.class);
		kryo.register(PilotPointSearchInput.class);
		kryo.register(SpectrumSearchInput.class);
		kryo.register(SpectrumInfo.class);
		kryo.register(SpectrumInfoType.class);
		kryo.register(BasicSearchInput.class);
		kryo.register(EquinoxUpdate.class);
		kryo.register(EquinoxUpdateInfoType.class);
		kryo.register(BugReport.class);
		kryo.register(BugReportInfo.class);
		kryo.register(Wish.class);
		kryo.register(WishInfo.class);
		kryo.register(HelpVideoInfo.class);
		kryo.register(HelpVideoInfoType.class);
		kryo.register(PilotPointImageType.class);
		kryo.register(ContributionType.class);
		kryo.register(AircraftModelInfo.class);
		kryo.register(AircraftModelInfoType.class);
		kryo.register(UpdateInfo.class);
		kryo.register(UpdateInfoType.class);
		kryo.register(SharedFileInfo.class);
		kryo.register(SharedFileInfoType.class);
		kryo.register(Material.class);
		kryo.register(FatigueMaterial.class);
		kryo.register(LinearMaterial.class);
		kryo.register(PreffasMaterial.class);
		kryo.register(SampleInputInfo.class);
		kryo.register(AccessRequest.class);
		kryo.register(AccessRequestInfo.class);
		kryo.register(UserLocation.class);

		// register message classes
		kryo.register(NetworkMessage.class);
		kryo.register(BigMessage.class);
		kryo.register(PartialMessage.class);
		kryo.register(AdministratorMessage.class);
		kryo.register(HandshakeWithDataServer.class);
		kryo.register(Login.class);
		kryo.register(DataMessage.class);
		kryo.register(DatabaseQueryRequest.class);
		kryo.register(DatabaseQueryPermissionDenied.class);
		kryo.register(DatabaseQueryProgress.class);
		kryo.register(DatabaseQueryFailed.class);
		kryo.register(GetPluginInfoRequest.class);
		kryo.register(GetPluginInfoResponse.class);
		kryo.register(PermissionDenied.class);
		kryo.register(LoginFailed.class);
		kryo.register(LoginSuccessful.class);
		kryo.register(AdvancedMultiplicationTableSearchRequest.class);
		kryo.register(AdvancedMultiplicationTableSearchResponse.class);
		kryo.register(AdvancedPilotPointSearchRequest.class);
		kryo.register(AdvancedPilotPointSearchResponse.class);
		kryo.register(AdvancedSpectrumSearchRequest.class);
		kryo.register(AdvancedSpectrumSearchResponse.class);
		kryo.register(BasicMultiplicationTableSearchRequest.class);
		kryo.register(BasicMultiplicationTableSearchResponse.class);
		kryo.register(BasicPilotPointSearchRequest.class);
		kryo.register(BasicPilotPointSearchResponse.class);
		kryo.register(BasicSpectrumSearchRequest.class);
		kryo.register(BasicSpectrumSearchResponse.class);
		kryo.register(CheckForEquinoxUpdatesRequest.class);
		kryo.register(CheckForEquinoxUpdatesResponse.class);
		kryo.register(CheckForMaterialUpdatesRequest.class);
		kryo.register(CheckForMaterialUpdatesResponse.class);
		kryo.register(CloseBugReportRequest.class);
		kryo.register(CloseBugReportResponse.class);
		kryo.register(CloseWishRequest.class);
		kryo.register(CloseWishResponse.class);
		kryo.register(DeleteHelpVideoRequest.class);
		kryo.register(DeleteHelpVideoResponse.class);
		kryo.register(DeleteMultiplicationTableRequest.class);
		kryo.register(DeleteMultiplicationTableResponse.class);
		kryo.register(DeletePilotPointRequest.class);
		kryo.register(DeletePilotPointResponse.class);
		kryo.register(DeletePluginRequest.class);
		kryo.register(DeletePluginResponse.class);
		kryo.register(DeleteSpectrumRequest.class);
		kryo.register(DeleteSpectrumResponse.class);
		kryo.register(DownloadHelpVideoRequest.class);
		kryo.register(DownloadHelpVideoResponse.class);
		kryo.register(DownloadPilotPointRequest.class);
		kryo.register(DownloadPilotPointResponse.class);
		kryo.register(DownloadPilotPointsRequest.class);
		kryo.register(DownloadPilotPointsResponse.class);
		kryo.register(DownloadSampleInputRequest.class);
		kryo.register(DownloadSampleInputResponse.class);
		kryo.register(ExecuteSQLStatementRequest.class);
		kryo.register(ExecuteSQLStatementResponse.class);
		kryo.register(GetAircraftProgramsForPilotPointsRequest.class);
		kryo.register(GetAircraftProgramsForPilotPointsResponse.class);
		kryo.register(GetAircraftProgramsForSpectraRequest.class);
		kryo.register(GetAircraftProgramsForSpectraResponse.class);
		kryo.register(GetAircraftSectionsForPilotPointsRequest.class);
		kryo.register(GetAircraftSectionsForPilotPointsResponse.class);
		kryo.register(GetAircraftSectionsForSpectraRequest.class);
		kryo.register(GetAircraftSectionsForSpectraResponse.class);
		kryo.register(GetBugReportsRequest.class);
		kryo.register(GetBugReportsResponse.class);
		kryo.register(GetFatigueMissionsForPilotPointsRequest.class);
		kryo.register(GetFatigueMissionsForPilotPointsResponse.class);
		kryo.register(GetFatigueMissionsForSpectraRequest.class);
		kryo.register(GetFatigueMissionsForSpectraResponse.class);
		kryo.register(GetHelpVideosRequest.class);
		kryo.register(GetHelpVideosResponse.class);
		kryo.register(GetPilotPointImageRequest.class);
		kryo.register(GetPilotPointImageResponse.class);
		kryo.register(GetWishesRequest.class);
		kryo.register(GetWishesResponse.class);
		kryo.register(LikeWishRequest.class);
		kryo.register(LikeWishResponse.class);
		kryo.register(PlotContributionStatisticsRequest.class);
		kryo.register(PlotContributionStatisticsResponse.class);
		kryo.register(PlotPilotPointCountRequest.class);
		kryo.register(PlotPilotPointCountResponse.class);
		kryo.register(PlotSpectrumCountRequest.class);
		kryo.register(PlotSpectrumCountResponse.class);
		kryo.register(PlotSpectrumSizeRequest.class);
		kryo.register(PlotSpectrumSizeResponse.class);
		kryo.register(ResetExchangeTableRequest.class);
		kryo.register(ResetExchangeTableResponse.class);
		kryo.register(SavePilotPointImageRequest.class);
		kryo.register(SavePilotPointImageResponse.class);
		kryo.register(SubmitBugReportRequest.class);
		kryo.register(SubmitBugReportResponse.class);
		kryo.register(SubmitWishRequest.class);
		kryo.register(SubmitWishResponse.class);
		kryo.register(GetFatigueMaterialsRequest.class);
		kryo.register(GetFatigueMaterialsResponse.class);
		kryo.register(GetLinearMaterialsRequest.class);
		kryo.register(GetLinearMaterialsResponse.class);
		kryo.register(GetPreffasMaterialsRequest.class);
		kryo.register(GetPreffasMaterialsResponse.class);
		kryo.register(UpdateMultiplicationTableRequest.class);
		kryo.register(UpdateMultiplicationTableResponse.class);
		kryo.register(UpdatePilotPointRequest.class);
		kryo.register(UpdatePilotPointResponse.class);
		kryo.register(UpdateSpectrumRequest.class);
		kryo.register(UpdateSpectrumResponse.class);
		kryo.register(UploadDamageContributionsRequest.class);
		kryo.register(UploadDamageContributionsResponse.class);
		kryo.register(UploadHelpVideoRequest.class);
		kryo.register(UploadHelpVideoResponse.class);
		kryo.register(UploadMaterialsRequest.class);
		kryo.register(UploadMaterialsResponse.class);
		kryo.register(UploadMultiplicationTablesRequest.class);
		kryo.register(UploadMultiplicationTablesResponse.class);
		kryo.register(UploadPilotPointsRequest.class);
		kryo.register(UploadPilotPointsResponse.class);
		kryo.register(UploadPluginRequest.class);
		kryo.register(UploadPluginResponse.class);
		kryo.register(UploadSampleInputsRequest.class);
		kryo.register(UploadSampleInputsResponse.class);
		kryo.register(UploadSpectraRequest.class);
		kryo.register(UploadSpectraResponse.class);
		kryo.register(UploadContainerUpdateRequest.class);
		kryo.register(UploadContainerUpdateResponse.class);
		kryo.register(GetPilotPointImagesRequest.class);
		kryo.register(GetPilotPointImagesResponse.class);
		kryo.register(AddNewUserRequest.class);
		kryo.register(AddNewUserResponse.class);
		kryo.register(DeleteUsersRequest.class);
		kryo.register(DeleteUsersResponse.class);
		kryo.register(GetUserPermissionsRequest.class);
		kryo.register(GetUserPermissionsResponse.class);
		kryo.register(EditUserPermissionsRequest.class);
		kryo.register(EditUserPermissionsResponse.class);
		kryo.register(SubmitAccessRequestRequest.class);
		kryo.register(SubmitAccessRequestResponse.class);
		kryo.register(GetAccessRequestsRequest.class);
		kryo.register(GetAccessRequestsResponse.class);
		kryo.register(CloseAccessRequestRequest.class);
		kryo.register(CloseAccessRequestResponse.class);
		kryo.register(DownloadPilotPointImagesRequest.class);
		kryo.register(DownloadPilotPointImagesResponse.class);
		kryo.register(DownloadPilotPointAttributesRequest.class);
		kryo.register(DownloadPilotPointAttributesResponse.class);
		kryo.register(DataServerStatisticsRequestFailed.class);
		kryo.register(GetDataQueriesRequest.class);
		kryo.register(GetDataQueriesResponse.class);
		kryo.register(GetSpectrumCountsRequest.class);
		kryo.register(GetSpectrumCountsResponse.class);
		kryo.register(GetSearchHitsRequest.class);
		kryo.register(GetSearchHitsResponse.class);
		kryo.register(GetPilotPointCountsRequest.class);
		kryo.register(GetPilotPointCountsResponse.class);
		kryo.register(GetBugReportCountRequest.class);
		kryo.register(GetBugReportCountResponse.class);
		kryo.register(GetWishCountRequest.class);
		kryo.register(GetWishCountResponse.class);
		kryo.register(GetAccessRequestCountRequest.class);
		kryo.register(GetAccessRequestCountResponse.class);

		// register external library classes
		kryo.register(CategoryDataset.class);
		kryo.register(DefaultCategoryDataset.class);
		kryo.register(DefaultKeyedValues2D.class);
		kryo.register(DefaultKeyedValues.class);
		kryo.register(DatasetGroup.class);

		// register listener classes
		kryo.register(DataMessageListener.class);
	}
}