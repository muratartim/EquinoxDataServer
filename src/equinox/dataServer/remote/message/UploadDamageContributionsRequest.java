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

import equinox.serverUtilities.SharedFileInfo;

/**
 * Class for upload damage contributions request network message.
 *
 * @author Murat Artim
 * @date 1 Mar 2018
 * @time 18:11:04
 */
public class UploadDamageContributionsRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Shared file info. */
	private SharedFileInfo sharedFileInfo;

	/**
	 * No argument constructor for serialization.
	 */
	public UploadDamageContributionsRequest() {
	}

	/**
	 * Returns the uploaded MS Excel file info which contains damage contribution data.
	 *
	 * @return The uploaded MS Excel file info.
	 */
	public SharedFileInfo getSharedFileInfo() {
		return sharedFileInfo;
	}

	/**
	 * Sets the MS Excel file info which contains damage contribution data.
	 *
	 * @param sharedFileInfo
	 *            MS Excel file info.
	 */
	public void setSharedFileInfo(SharedFileInfo sharedFileInfo) {
		this.sharedFileInfo = sharedFileInfo;
	}
}
