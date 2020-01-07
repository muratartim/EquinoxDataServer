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

import equinox.dataServer.remote.data.Wish;

/**
 * Class for like wish request network message.
 *
 * @author Murat Artim
 * @date 18 Feb 2018
 * @time 20:26:38
 */
public class LikeWishRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Wish to like. */
	private Wish wish;

	/**
	 * No argument constructor for serialization.
	 */
	public LikeWishRequest() {
	}

	/**
	 * Returns the wish.
	 *
	 * @return The wish.
	 */
	public Wish getWish() {
		return wish;
	}

	/**
	 * Sets wish.
	 *
	 * @param wish
	 *            Wish.
	 */
	public void setWish(Wish wish) {
		this.wish = wish;
	}
}
