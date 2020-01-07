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
 * Class for download help video request.
 *
 * @author Murat Artim
 * @date 10 Feb 2018
 * @time 21:53:22
 */
public class DownloadHelpVideoRequest extends DatabaseQueryRequest {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** Video name. */
	private String videoName;

	/** Video id. */
	private long videoId;

	/**
	 * No argument constructor for serialization.
	 */
	public DownloadHelpVideoRequest() {
	}

	/**
	 * Returns video name.
	 * 
	 * @return Video name.
	 */
	public String getVideoName() {
		return videoName;
	}

	/**
	 * Video id.
	 * 
	 * @return Video id.
	 */
	public long getVideoId() {
		return videoId;
	}

	/**
	 * Sets video name.
	 * 
	 * @param videoName
	 *            Video name.
	 */
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}

	/**
	 * Sets video id.
	 * 
	 * @param videoId
	 *            Video id.
	 */
	public void setVideoId(long videoId) {
		this.videoId = videoId;
	}
}
