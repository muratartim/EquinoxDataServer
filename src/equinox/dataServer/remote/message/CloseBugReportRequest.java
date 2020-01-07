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

/**
 * Class for close bug report request network message.
 *
 * @author Murat Artim
 * @date 8 Feb 2018
 * @time 12:18:04
 */
public class CloseBugReportRequest extends DatabaseQueryRequest {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Bug report to close. */
	private long bugReportId;

	/** Solution text. */
	private String solution;

	/**
	 * Empty constructor for serialization.
	 */
	public CloseBugReportRequest() {
	}

	/**
	 * Returns bug report ID.
	 *
	 * @return Bug report ID.
	 */
	public long getBugReportId() {
		return bugReportId;
	}

	/**
	 * Returns solution text.
	 *
	 * @return Solution text.
	 */
	public String getSolution() {
		return solution;
	}

	/**
	 * Sets solution text.
	 *
	 * @param solution
	 *            Solution text.
	 */
	public void setSolution(String solution) {
		this.solution = solution;
	}

	/**
	 * Sets bug report ID.
	 *
	 * @param bugReportId
	 *            Bug report ID.
	 */
	public void setBugReportId(long bugReportId) {
		this.bugReportId = bugReportId;
	}
}
