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
package equinox.dataServer.remote.message;

import equinox.dataServer.remote.data.ContributionType;

/**
 * Class for plot contribution statistics request network message.
 *
 * @author Murat Artim
 * @date 18 Feb 2018
 * @time 21:36:58
 */
public class PlotContributionStatisticsRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Aircraft program, section and mission. */
	private String program, section, mission;

	/** Damage contribution type. */
	private ContributionType contributionType;

	/** Number of increments to plot. */
	private int limit = 3;

	/**
	 * No argument constructor for serialization.
	 */
	public PlotContributionStatisticsRequest() {
	}

	/**
	 * Returns aircraft program.
	 *
	 * @return Aircraft program.
	 */
	public String getProgram() {
		return program;
	}

	/**
	 * Returns aircraft section.
	 *
	 * @return Aircraft section.
	 */
	public String getSection() {
		return section;
	}

	/**
	 * Returns fatigue mission.
	 *
	 * @return Fatigue mission.
	 */
	public String getMission() {
		return mission;
	}

	/**
	 * Returns contribution type.
	 *
	 * @return Contribution type.
	 */
	public ContributionType getContributionType() {
		return contributionType;
	}

	/**
	 * Returns number of increments to plot.
	 *
	 * @return Number of increments to plot.
	 */
	public int getLimit() {
		return limit;
	}

	/**
	 * Sets aircraft program.
	 *
	 * @param program
	 *            Aircraft program.
	 */
	public void setProgram(String program) {
		this.program = program;
	}

	/**
	 * Sets aircraft section.
	 *
	 * @param section
	 *            Aircraft section.
	 */
	public void setSection(String section) {
		this.section = section;
	}

	/**
	 * Sets fatigue mission.
	 *
	 * @param mission
	 *            Fatigue mission.
	 */
	public void setMission(String mission) {
		this.mission = mission;
	}

	/**
	 * Sets contribution type.
	 *
	 * @param contributionType
	 *            Contribution type.
	 */
	public void setContributionType(ContributionType contributionType) {
		this.contributionType = contributionType;
	}

	/**
	 * Sets number of increments to plot.
	 *
	 * @param limit
	 *            Number of increments to plot.
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}
}
