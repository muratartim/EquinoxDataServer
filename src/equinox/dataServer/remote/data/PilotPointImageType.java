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
package equinox.dataServer.remote.data;

import equinox.serverUtilities.FilerConnection;

/**
 * Enumeration for pilot point image type.
 *
 * @author Murat Artim
 * @date May 8, 2016
 * @time 4:52:41 PM
 */
public enum PilotPointImageType {

	/** Pilot point image type. */
	// @formatter:off
	IMAGE("ppImage.png", "pilot_point_image", "Pilot Point Image", FilerConnection.PP_IMAGES), MISSION_PROFILE("missionProfile.png", "pilot_point_mp", "Mission Profile", FilerConnection.PP_MISSION_PROFILE_PLOTS),
	LONGEST_FLIGHT("longestFlight.png", "pilot_point_tf_l", "Longest Typical Flight", FilerConnection.PP_LONGEST_TYPICAL_FLIGHT_PLOTS),
	FLIGHT_WITH_HIGHEST_OCCURRENCE("flightWithHighestOccurrence.png", "pilot_point_tf_ho", "Flight With Highest Occurrence", FilerConnection.PP_TYPICAL_FLIGHT_WITH_HIGHEST_OCCURRENCE_PLOTS),
	FLIGHT_WITH_MAX_TOTAL_STRESS("flightWithHighestTotalStress.png", "pilot_point_tf_hs", "Flight With Highest Total Stress", FilerConnection.PP_TYPICAL_FLIGHT_WITH_HIGHEST_STRESS_PLOTS),
	LEVEL_CROSSING("levelCrossing.png", "pilot_point_lc", "Level Crossings", FilerConnection.PP_LEVEL_CROSSING_PLOTS), DAMAGE_ANGLE("damageAngle.png", "pilot_point_da", "Damage Angles", FilerConnection.PP_DAMAGE_ANGLE_PLOTS),
	NUMBER_OF_PEAKS("numberOfPeaks.png", "pilot_point_st_nop", "Typical Flight Number Of Peaks", FilerConnection.PP_NUMBER_OF_PEAKS_PLOTS),
	FLIGHT_OCCURRENCE("flightOccurrence.png", "pilot_point_st_fo", "Typical Flight Occurrences", FilerConnection.PP_FLIGHT_OCCURRENCE_PLOTS),
	RAINFLOW_HISTOGRAM("rainflowHistogram.png", "pilot_point_st_rh", "Rainflow Histogram", FilerConnection.PP_RAINFLOW_HISTOGRAM_PLOTS),
	LOADCASE_DAMAGE_CONTRIBUTION("loadcaseDamageContribution.png", "pilot_point_dc", "Loadcase Damage Contributions", FilerConnection.PP_LOADCASE_DAMAGE_CONTRIBUTION_PLOTS),
	FLIGHT_DAMAGE_CONTRIBUTION("flightDamageContribution.png", "pilot_point_tf_dc", "Flight Damage Contributions", FilerConnection.PP_TYPICAL_FLIGHT_DAMAGE_CONTRIBUTION_PLOTS);
	// @formatter:on

	/** Attributes of image type. */
	private final String fileName_, tableName_, pageName_, filerDirectoryName_;

	/** Maximum image file size. */
	public static final long MAX_IMAGE_SIZE = 2000000L;

	/**
	 * Creates pilot point image type.
	 *
	 * @param fileName
	 *            File name.
	 * @param tableName
	 *            Table name.
	 * @param pageName
	 *            Page name.
	 * @param filerDirectoryName
	 *            Filer directory name.
	 */
	PilotPointImageType(String fileName, String tableName, String pageName, String filerDirectoryName) {
		fileName_ = fileName;
		tableName_ = tableName;
		pageName_ = pageName;
		filerDirectoryName_ = filerDirectoryName;
	}

	/**
	 * Returns file name.
	 *
	 * @return File name.
	 */
	public String getFileName() {
		return fileName_;
	}

	/**
	 * Returns table name.
	 *
	 * @return Table name.
	 */
	public String getTableName() {
		return tableName_;
	}

	/**
	 * Returns page name.
	 *
	 * @return Page name.
	 */
	public String getPageName() {
		return pageName_;
	}

	/**
	 * Returns the filer directory name.
	 *
	 * @return The filer directory name.
	 */
	public String getFilerDirectoryName() {
		return filerDirectoryName_;
	}

	@Override
	public String toString() {
		return pageName_;
	}
}
