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
 * Class for upload damage contributions response network message.
 *
 * @author Murat Artim
 * @date 1 Mar 2018
 * @time 18:15:38
 */
public class UploadDamageContributionsResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** True if the info is uploaded. */
	private boolean isUploaded;

	/**
	 * No argument constructor for serialization.
	 */
	public UploadDamageContributionsResponse() {
	}

	/**
	 * Returns true if data is uploaded.
	 * 
	 * @return True if data is uploaded.
	 */
	public boolean isUploaded() {
		return isUploaded;
	}

	/**
	 * Sets if the data is uploaded.
	 * 
	 * @param isUploaded
	 *            True if the data is uploaded.
	 */
	public void setUploaded(boolean isUploaded) {
		this.isUploaded = isUploaded;
	}
}
