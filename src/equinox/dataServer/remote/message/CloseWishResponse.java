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
 * Class for close wish response network message.
 *
 * @author Murat Artim
 * @date 8 Feb 2018
 * @time 21:59:35
 */
public class CloseWishResponse extends DataMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** True if wish is closed. */
	private boolean isWishClosed;

	/**
	 * Empty constructor for serialization.
	 */
	public CloseWishResponse() {
	}

	/**
	 * Returns true if wish is closed.
	 *
	 * @return True if wish is closed.
	 */
	public boolean isWishClosed() {
		return isWishClosed;
	}

	/**
	 * Sets whether or not wish is closed.
	 *
	 * @param isWishClosed
	 *            True if wish is closed.
	 */
	public void setWishClosed(boolean isWishClosed) {
		this.isWishClosed = isWishClosed;
	}
}
