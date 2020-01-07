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

import java.util.ArrayList;

import equinox.dataServer.remote.data.BugReport;
import equinox.serverUtilities.BigMessage;

/**
 * Class for get bug reports response network message.
 *
 * @author Murat Artim
 * @date 15 Feb 2018
 * @time 15:19:54
 */
public class GetBugReportsResponse extends DataMessage implements BigMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Bug reports. */
	private final ArrayList<BugReport> reports = new ArrayList<>();

	/**
	 * No argument constructor for serialization.
	 */
	public GetBugReportsResponse() {
	}

	/**
	 * Returns bug reports.
	 *
	 * @return Bug reports.
	 */
	public ArrayList<BugReport> getReports() {
		return reports;
	}

	/**
	 * Adds given bug report.
	 *
	 * @param report
	 *            Bug report to add.
	 */
	public void addReport(BugReport report) {
		reports.add(report);
	}

	@Override
	public boolean isReallyBig() {
		return true;
	}
}
