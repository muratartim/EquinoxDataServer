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
 * Class for close wish request network message.
 *
 * @author Murat Artim
 * @date 8 Feb 2018
 * @time 21:54:12
 */
public class CloseWishRequest extends DatabaseQueryRequest {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Wish ID. */
	private long wishId;

	/** Closure text. */
	private String closure;

	/**
	 * No argument constructor for serialization.
	 */
	public CloseWishRequest() {
	}

	/**
	 * Returns the wish ID.
	 * 
	 * @return The wish ID.
	 */
	public long getWishId() {
		return wishId;
	}

	/**
	 * Returns the closure text.
	 * 
	 * @return Closure text.
	 */
	public String getClosure() {
		return closure;
	}

	/**
	 * Sets wish ID.
	 * 
	 * @param wishId
	 *            Wish ID.
	 */
	public void setWishId(long wishId) {
		this.wishId = wishId;
	}

	/**
	 * Sets closure text.
	 * 
	 * @param closure
	 *            Closure text.
	 */
	public void setClosure(String closure) {
		this.closure = closure;
	}
}
