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

import equinox.dataServer.remote.data.DownloadInfo;
import equinox.dataServer.remote.data.HelpVideoInfo;

/**
 * Class for get help videos response network message.
 *
 * @author Murat Artim
 * @date 16 Feb 2018
 * @time 10:52:43
 */
public class GetHelpVideosResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Help videos. */
	private final ArrayList<DownloadInfo> videos = new ArrayList<>();

	/**
	 * No argument constructor for serialization.
	 */
	public GetHelpVideosResponse() {
	}

	/**
	 * Returns help videos.
	 *
	 * @return Help videos.
	 */
	public ArrayList<DownloadInfo> getVideos() {
		return videos;
	}

	/**
	 * Adds given help video info.
	 *
	 * @param info
	 *            Help video info.
	 */
	public void addVideo(HelpVideoInfo info) {
		videos.add(info);
	}
}
