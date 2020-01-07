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

import equinox.dataServer.remote.data.PilotPointImageType;

/**
 * Class for download pilot point images response network message.
 *
 * @author Murat Artim
 * @date 6 Jul 2018
 * @time 23:42:08
 */
public class DownloadPilotPointImagesResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Download URLs. */
	private HashMap<PilotPointImageType, String> downloadUrls;

	/**
	 * No argument constructor for serialization.
	 */
	public DownloadPilotPointImagesResponse() {
		downloadUrls = new HashMap<>();
	}

	/**
	 * Returns download URLs.
	 *
	 * @return Download URLs.
	 */
	public HashMap<PilotPointImageType, String> getDownloadUrls() {
		return downloadUrls;
	}

	/**
	 * Sets download URL.
	 *
	 * @param imageType
	 *            Image type.
	 * @param url
	 *            URL.
	 */
	public void putDownloadUrl(PilotPointImageType imageType, String url) {
		downloadUrls.put(imageType, url);
	}
}
