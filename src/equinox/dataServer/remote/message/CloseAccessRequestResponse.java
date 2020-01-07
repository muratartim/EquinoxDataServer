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
 * Class for close access request response network message.
 *
 * @author Murat Artim
 * @date 15 Apr 2018
 * @time 20:35:00
 */
public class CloseAccessRequestResponse extends DataMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** True if access request is closed. */
	private boolean isAccessRequestClosed;

	/**
	 * Empty constructor for serialization.
	 */
	public CloseAccessRequestResponse() {
	}

	/**
	 * Returns true if access request is closed.
	 *
	 * @return True if access request is closed.
	 */
	public boolean isAccessRequestClosed() {
		return isAccessRequestClosed;
	}

	/**
	 * Sets whether or not access request is closed.
	 *
	 * @param isAccessRequestClosed
	 *            True if access request is closed.
	 */
	public void setAccessRequestClosed(boolean isAccessRequestClosed) {
		this.isAccessRequestClosed = isAccessRequestClosed;
	}
}
