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
 * Class for like wish response network message.
 *
 * @author Murat Artim
 * @date 18 Feb 2018
 * @time 20:29:56
 */
public class LikeWishResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** True if wish is liked. */
	private boolean isWishLiked;

	/**
	 * No argument constructor for serialization.
	 */
	public LikeWishResponse() {
	}

	/**
	 * Returns true if wish is liked.
	 * 
	 * @return True if wish is liked.
	 */
	public boolean isWishLiked() {
		return isWishLiked;
	}

	/**
	 * Sets if wish is liked.
	 * 
	 * @param isWishLiked
	 *            True if wish is liked.
	 */
	public void setWishLiked(boolean isWishLiked) {
		this.isWishLiked = isWishLiked;
	}
}
