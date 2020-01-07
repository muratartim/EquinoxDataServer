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
 * Class for get pilot point images response network message.
 *
 * @author Murat Artim
 * @date 28 Mar 2018
 * @time 00:14:20
 */
public class GetPilotPointImagesResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Image urls. */
	private final HashMap<PilotPointImageType, String> imageUrls;

	/**
	 * No argument constructor for serialization.
	 */
	public GetPilotPointImagesResponse() {
		imageUrls = new HashMap<>();
	}

	/**
	 * Returns pilot point image URLs.
	 *
	 * @return Pilot point image URLs.
	 */
	public HashMap<PilotPointImageType, String> getImageUrls() {
		return imageUrls;
	}

	/**
	 * Puts given URL for given image type.
	 *
	 * @param imageType
	 *            Pilot point image type.
	 * @param url
	 *            Image URL.
	 */
	public void putImageUrl(PilotPointImageType imageType, String url) {
		imageUrls.put(imageType, url);
	}
}
