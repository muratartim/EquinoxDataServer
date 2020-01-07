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
 * Class for edit user permissions response network message.
 *
 * @author Murat Artim
 * @date 6 Apr 2018
 * @time 12:31:13
 */
public class EditUserPermissionsResponse extends DataMessage {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** True if user permissions are edited. */
	private boolean isEdited;

	/**
	 * No argument constructor for serialization.
	 */
	public EditUserPermissionsResponse() {
	}

	/**
	 * Returns true if user permissions are edited.
	 *
	 * @return True if user permissions are edited.
	 */
	public boolean isEdited() {
		return isEdited;
	}

	/**
	 * Sets if the user is added.
	 *
	 * @param isEdited
	 *            True if user permissions are edited.
	 */
	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}
}
