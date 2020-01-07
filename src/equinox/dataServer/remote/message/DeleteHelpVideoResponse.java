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
 * Class for delete help video response network message.
 *
 * @author Murat Artim
 * @date 8 Feb 2018
 * @time 22:55:16
 */
public class DeleteHelpVideoResponse extends DataMessage {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** True if video is deleted. */
	private boolean isVideoDeleted;

	/**
	 * No argument constructor for serialization.
	 */
	public DeleteHelpVideoResponse() {
	}

	/**
	 * Returns true if video is deleted.
	 * 
	 * @return True if video is deleted.
	 */
	public boolean isVideoDeleted() {
		return isVideoDeleted;
	}

	/**
	 * Sets if the video is deleted.
	 * 
	 * @param isVideoDeleted
	 *            True if video is deleted.
	 */
	public void setVideoDeleted(boolean isVideoDeleted) {
		this.isVideoDeleted = isVideoDeleted;
	}
}
