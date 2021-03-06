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
 * Class for upload pilot points response network message.
 *
 * @author Murat Artim
 * @date 14 Mar 2018
 * @time 12:37:17
 */
public class UploadPilotPointsResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** True if pilot points are uploaded. */
	private boolean isUploaded;

	/**
	 * No argument constructor for serialization.
	 */
	public UploadPilotPointsResponse() {
	}

	/**
	 * Returns true if pilot points are uploaded.
	 *
	 * @return True if pilot points are uploaded.
	 */
	public boolean isUploaded() {
		return isUploaded;
	}

	/**
	 * Sets if the pilot points are uploaded.
	 *
	 * @param isUploaded
	 *            True if pilot points are uploaded.
	 */
	public void setUploaded(boolean isUploaded) {
		this.isUploaded = isUploaded;
	}
}
