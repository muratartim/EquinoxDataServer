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

import java.util.Map;

/**
 * Class for get wish count response network message.
 * 
 * @author Murat Artim
 * @date 28 Jul 2018
 * @time 00:14:30
 */
public class GetWishCountResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Pilot point counts. */
	private Map<String, Integer> wishCounts;

	/**
	 * No argument constructor for serialization.
	 */
	public GetWishCountResponse() {
	}

	/**
	 * Returns wish counts.
	 *
	 * @return Wish counts.
	 */
	public Map<String, Integer> getWishCounts() {
		return wishCounts;
	}

	/**
	 * Sets wish counts.
	 *
	 * @param wishCounts
	 *            Wish counts.
	 */
	public void setWishCounts(Map<String, Integer> wishCounts) {
		this.wishCounts = wishCounts;
	}
}
