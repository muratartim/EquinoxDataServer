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
 * Class for download pilot point request network message.
 *
 * @author Murat Artim
 * @date 11 Feb 2018
 * @time 22:03:11
 */
public class DownloadPilotPointRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Download id. */
	private long downloadId;

	/**
	 * No argument constructor for serialization.
	 */
	public DownloadPilotPointRequest() {
	}

	/**
	 * Returns download id.
	 * 
	 * @return Download id.
	 */
	public long getDownloadId() {
		return downloadId;
	}

	/**
	 * Sets download id.
	 * 
	 * @param downloadId
	 *            Download id.
	 */
	public void setDownloadId(long downloadId) {
		this.downloadId = downloadId;
	}
}
