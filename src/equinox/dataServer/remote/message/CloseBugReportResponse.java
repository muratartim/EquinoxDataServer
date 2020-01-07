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
 * Class for close bug report response network message.
 *
 * @author Murat Artim
 * @date 8 Feb 2018
 * @time 12:30:10
 */
public class CloseBugReportResponse extends DataMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** True if bug report is closed. */
	private boolean isBugReportClosed;

	/**
	 * Empty constructor for serialization.
	 */
	public CloseBugReportResponse() {
	}

	/**
	 * Returns true if bug report is closed.
	 * 
	 * @return True if bug report is closed.
	 */
	public boolean isBugReportClosed() {
		return isBugReportClosed;
	}

	/**
	 * Sets whether or not bug report is closed.
	 * 
	 * @param isBugReportClosed
	 *            True if bug report is closed.
	 */
	public void setBugReportClosed(boolean isBugReportClosed) {
		this.isBugReportClosed = isBugReportClosed;
	}
}
