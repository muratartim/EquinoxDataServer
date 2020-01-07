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
import java.util.HashMap;

import equinox.dataServer.remote.data.PilotPointImageType;
import equinox.dataServer.remote.data.PilotPointInfo;
import equinox.serverUtilities.BigMessage;

/**
 * Class for upload pilot points request network message.
 *
 * @author Murat Artim
 * @date 13 Mar 2018
 * @time 23:26:37
 */
public class UploadPilotPointsRequest extends DatabaseQueryRequest implements BigMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Pilot point info list. */
	private final ArrayList<PilotPointInfo> info;

	/** Data URL list. */
	private final ArrayList<String> dataUrls, attributeUrls;

	/** Image URL list. */
	private final ArrayList<HashMap<PilotPointImageType, String>> imageUrls;

	/**
	 * No argument constructor for serialization.
	 */
	public UploadPilotPointsRequest() {
		info = new ArrayList<>();
		dataUrls = new ArrayList<>();
		attributeUrls = new ArrayList<>();
		imageUrls = new ArrayList<>();
	}

	/**
	 * Returns pilot point info list.
	 *
	 * @return Pilot point info list.
	 */
	public ArrayList<PilotPointInfo> getInfo() {
		return info;
	}

	/**
	 * Returns data URL list.
	 *
	 * @return Data URL list.
	 */
	public ArrayList<String> getDataUrls() {
		return dataUrls;
	}

	/**
	 * Returns attribute URL list.
	 *
	 * @return Attribute URL list.
	 */
	public ArrayList<String> getAttributeUrls() {
		return attributeUrls;
	}

	/**
	 * Returns pilot point image URL list.
	 *
	 * @return Pilot point image URL list.
	 */
	public ArrayList<HashMap<PilotPointImageType, String>> getImageUrls() {
		return imageUrls;
	}

	/**
	 * Adds given pilot point info to this message.
	 *
	 * @param info
	 *            Pilot point info.
	 */
	public void addInfo(PilotPointInfo info) {
		this.info.add(info);
	}

	/**
	 * Adds given pilot point data URL to this message.
	 *
	 * @param url
	 *            Pilot point data URL.
	 */
	public void addDataUrl(String url) {
		dataUrls.add(url);
	}

	/**
	 * Adds given pilot point data URL to this message.
	 *
	 * @param url
	 *            Pilot point data URL.
	 */
	public void addAttributeUrl(String url) {
		attributeUrls.add(url);
	}

	/**
	 * Adds given pilot point image URLS to this message.
	 *
	 * @param urls
	 *            Pilot point image URLS.
	 */
	public void addImageUrls(HashMap<PilotPointImageType, String> urls) {
		imageUrls.add(urls);
	}

	@Override
	public boolean isReallyBig() {
		return info.size() > 5;
	}
}
