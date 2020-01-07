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

import equinox.serverUtilities.BigMessage;

/**
 * Class for submit bug report request network message.
 *
 * @author Murat Artim
 * @date 22 Feb 2018
 * @time 13:22:03
 */
public class SubmitBugReportRequest extends DatabaseQueryRequest implements BigMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Bug report info. */
	private String report, systemInfo, eventLog;

	/**
	 * No argument constructor for serialization.
	 */
	public SubmitBugReportRequest() {
	}

	/**
	 * Returns report text.
	 *
	 * @return Report text.
	 */
	public String getReport() {
		return report;
	}

	/**
	 * Returns system info, or null if none given.
	 *
	 * @return System info, or null if none given.
	 */
	public String getSystemInfo() {
		return systemInfo;
	}

	/**
	 * Returns event log, or null if none given.
	 *
	 * @return Event log, or null if none given.
	 */
	public String getEventLog() {
		return eventLog;
	}

	/**
	 * Sets bug report text.
	 *
	 * @param report
	 *            Bug report text.
	 */
	public void setReport(String report) {
		this.report = report;
	}

	/**
	 * System information.
	 *
	 * @param systemInfo
	 *            System information.
	 */
	public void setSystemInfo(String systemInfo) {
		this.systemInfo = systemInfo;
	}

	/**
	 * Event log.
	 *
	 * @param eventLog
	 *            Event log.
	 */
	public void setEventLog(String eventLog) {
		this.eventLog = eventLog;
	}

	@Override
	public boolean isReallyBig() {
		return true;
	}
}
