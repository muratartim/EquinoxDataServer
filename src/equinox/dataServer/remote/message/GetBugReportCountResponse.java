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

import java.util.Map;

/**
 * Class for get bug report count response network message.
 *
 * @author Murat Artim
 * @date 28 Jul 2018
 * @time 00:10:23
 */
public class GetBugReportCountResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Pilot point counts. */
	private Map<String, Integer> bugReportCounts;

	/**
	 * No argument constructor for serialization.
	 */
	public GetBugReportCountResponse() {
	}

	/**
	 * Returns bug report counts.
	 *
	 * @return Bug report counts.
	 */
	public Map<String, Integer> getBugReportCounts() {
		return bugReportCounts;
	}

	/**
	 * Sets bug report counts.
	 *
	 * @param bugReportCounts
	 *            Bug report counts.
	 */
	public void setBugReportCounts(Map<String, Integer> bugReportCounts) {
		this.bugReportCounts = bugReportCounts;
	}
}