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
 * Class for delete spectrum response network message.
 *
 * @author Murat Artim
 * @date 10 Feb 2018
 * @time 00:03:16
 */
public class DeleteSpectrumResponse extends DataMessage {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** True if spectrum is deleted. */
	private boolean isSpectrumDeleted;

	/**
	 * No argument constructor for serialization.
	 */
	public DeleteSpectrumResponse() {
	}

	/**
	 * Returns true if spectrum is deleted.
	 * 
	 * @return True if spectrum is deleted.
	 */
	public boolean isSpectrumDeleted() {
		return isSpectrumDeleted;
	}

	/**
	 * Sets if spectrum is deleted.
	 * 
	 * @param isSpectrumDeleted
	 *            True is spectrum is deleted.
	 */
	public void setSpectrumDeleted(boolean isSpectrumDeleted) {
		this.isSpectrumDeleted = isSpectrumDeleted;
	}
}
