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
 * Class for update multiplication table info response network message.
 * 
 * @author Murat Artim
 * @date 28 Feb 2018
 * @time 15:35:05
 */
public class UpdateMultiplicationTableResponse extends DataMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** True if multiplication table is updated. */
	private boolean isUpdated;

	/**
	 * No argument constructor for serialization.
	 */
	public UpdateMultiplicationTableResponse() {
	}

	/**
	 * Returns true if multiplication table is updated.
	 * 
	 * @return True if multiplication table is updated.
	 */
	public boolean isUpdated() {
		return isUpdated;
	}

	/**
	 * Sets if multiplication table is updated.
	 * 
	 * @param isUpdated
	 *            True if multiplication table is updated.
	 */
	public void setUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}
}
