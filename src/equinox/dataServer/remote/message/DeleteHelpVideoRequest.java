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
 * Class for delete help video request network message.
 *
 * @author Murat Artim
 * @date 8 Feb 2018
 * @time 22:43:56
 */
public class DeleteHelpVideoRequest extends DatabaseQueryRequest {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** Help video to delete. */
	private HelpVideoInfo videoInfo;

	/**
	 * No argument constructor for serialization.
	 */
	public DeleteHelpVideoRequest() {
	}

	/**
	 * Returns help video info.
	 *
	 * @return Help video info.
	 */
	public HelpVideoInfo getVideoInfo() {
		return videoInfo;
	}

	/**
	 * Sets help video info.
	 *
	 * @param videoInfo
	 *            Help video info.
	 */
	public void setVideoInfo(HelpVideoInfo videoInfo) {
		this.videoInfo = videoInfo;
	}
}
