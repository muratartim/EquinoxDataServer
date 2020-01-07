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
 * Class for add new user response network message.
 *
 * @author Murat Artim
 * @date 4 Apr 2018
 * @time 16:40:44
 */
public class AddNewUserResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** True if user is added. */
	private boolean isAdded;

	/**
	 * No argument constructor for serialization.
	 */
	public AddNewUserResponse() {
	}

	/**
	 * Returns true if user is added.
	 *
	 * @return True if user is added.
	 */
	public boolean isAdded() {
		return isAdded;
	}

	/**
	 * Sets if the user is added.
	 *
	 * @param isAdded
	 *            True if user is added.
	 */
	public void setAdded(boolean isAdded) {
		this.isAdded = isAdded;
	}
}
