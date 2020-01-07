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
 * Class for get bug reports request network message.
 *
 * @author Murat Artim
 * @date 15 Feb 2018
 * @time 15:12:32
 */
public class GetBugReportsRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Status index. */
	public static int ALL = 0, OPEN = 1, CLOSED = 2;

	/** Status of requested bug reports. */
	private int status;

	/**
	 * No argument constructor for serialization.
	 */
	public GetBugReportsRequest() {
	}

	/**
	 * Returns the status of requested bug reports.
	 * 
	 * @return Status of requested bug reports.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status of the requested bug reports.
	 * 
	 * @param status
	 *            Status of the requested bug reports.
	 */
	public void setStatus(int status) {
		this.status = status;
	}
}
