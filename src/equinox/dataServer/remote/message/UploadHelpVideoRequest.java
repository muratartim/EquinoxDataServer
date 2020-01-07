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

import equinox.dataServer.remote.data.HelpVideoInfo;

/**
 * Class for upload help video request network message.
 *
 * @author Murat Artim
 * @date 2 Mar 2018
 * @time 11:14:30
 */
public class UploadHelpVideoRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Help video info. */
	private HelpVideoInfo info;

	/**
	 * No argument constructor for serialization.
	 */
	public UploadHelpVideoRequest() {
	}

	/**
	 * Returns help video info.
	 *
	 * @return Help video info.
	 */
	public HelpVideoInfo getInfo() {
		return info;
	}

	/**
	 * Sets help video info.
	 *
	 * @param info
	 *            Help video info.
	 */
	public void setInfo(HelpVideoInfo info) {
		this.info = info;
	}
}
