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
 * Class for update pilot point response network message.
 * 
 * @author Murat Artim
 * @date 28 Feb 2018
 * @time 17:22:30
 */
public class UpdatePilotPointResponse extends DataMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** True if pilot point is updated. */
	private boolean isUpdated;

	/**
	 * No argument constructor for serialization.
	 */
	public UpdatePilotPointResponse() {
	}

	/**
	 * Returns true if pilot point is updated.
	 *
	 * @return True if pilot point is updated.
	 */
	public boolean isUpdated() {
		return isUpdated;
	}

	/**
	 * Sets if pilot point is updated.
	 *
	 * @param isUpdated
	 *            True if pilot point is updated.
	 */
	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
}
