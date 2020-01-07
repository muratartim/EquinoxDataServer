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
 * Class for update spectrum response network message.
 * 
 * @author Murat Artim
 * @date 28 Feb 2018
 * @time 17:40:21
 */
public class UpdateSpectrumResponse extends DataMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** True if spectrum is updated. */
	private boolean isUpdated;

	/**
	 * No argument constructor for serialization.
	 */
	public UpdateSpectrumResponse() {
	}

	/**
	 * Returns true if spectrum is updated.
	 *
	 * @return True if spectrum is updated.
	 */
	public boolean isUpdated() {
		return isUpdated;
	}

	/**
	 * Sets if spectrum is updated.
	 *
	 * @param isUpdated
	 *            True if spectrum is updated.
	 */
	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
}
