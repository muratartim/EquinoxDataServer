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

import java.util.HashMap;

/**
 * Class for download pilot point response network message.
 *
 * @author Murat Artim
 * @date 11 Feb 2018
 * @time 22:50:16
 */
public class DownloadPilotPointsResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Download URLs. */
	private final HashMap<Long, String> downloadUrls = new HashMap<>();

	/**
	 * No argument constructor for serialization.
	 */
	public DownloadPilotPointsResponse() {
	}

	/**
	 * Adds download URL.
	 *
	 * @param id
	 *            Download id.
	 * @param url
	 *            Download URL.
	 */
	public void addDownloadUrl(long id, String url) {
		downloadUrls.put(id, url);
	}

	/**
	 * Returns download URL for given id.
	 *
	 * @param id
	 *            Download id.
	 * @return Download URL for given id.
	 */
	public String getDownloadUrl(long id) {
		return downloadUrls.get(id);
	}
}
