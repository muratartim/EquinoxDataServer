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
 * Class for submit wish request network message.
 *
 * @author Murat Artim
 * @date 22 Feb 2018
 * @time 14:12:44
 */
public class SubmitWishResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Wish id. */
	private long wishId;

	/**
	 * No argument constructor for serialization.
	 */
	public SubmitWishResponse() {
	}

	/**
	 * Returns wish id.
	 * 
	 * @return Wish id.
	 */
	public long getWishId() {
		return wishId;
	}

	/**
	 * Sets wish id.
	 * 
	 * @param wishId
	 *            Wish id.
	 */
	public void setWishId(long wishId) {
		this.wishId = wishId;
	}
}
