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
 * Class for download pilot point attributes response network message.
 *
 * @author Murat Artim
 * @date 7 Jul 2018
 * @time 00:56:41
 */
public class DownloadPilotPointAttributesResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Download URL. */
	private String downloadUrl;

	/**
	 * No argument constructor for serialization.
	 */
	public DownloadPilotPointAttributesResponse() {
	}

	/**
	 * Returns download URL.
	 *
	 * @return Download URL.
	 */
	public String getDownloadUrl() {
		return downloadUrl;
	}

	/**
	 * Sets download URL.
	 *
	 * @param downloadUrl
	 *            Download URL.
	 */
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
}
