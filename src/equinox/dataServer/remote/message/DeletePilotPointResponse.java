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
 * Class for delete pilot point response network message.
 * 
 * @author Murat Artim
 * @date 9 Feb 2018
 * @time 11:34:24
 */
public class DeletePilotPointResponse extends DataMessage {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** True if pilot point is deleted. */
	private boolean isPilotPointDeleted;

	/**
	 * No argument constructor for serialization.
	 */
	public DeletePilotPointResponse() {
	}

	/**
	 * Returns true if pilot point is deleted.
	 * 
	 * @return True if pilot point is deleted.
	 */
	public boolean isPilotPointDeleted() {
		return isPilotPointDeleted;
	}

	/**
	 * Sets if the pilot point is deleted.
	 * 
	 * @param isPilotPointDeleted
	 *            True if pilot point is deleted.
	 */
	public void setPilotPointDeleted(boolean isPilotPointDeleted) {
		this.isPilotPointDeleted = isPilotPointDeleted;
	}
}
