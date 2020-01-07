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

import java.util.ArrayList;

import equinox.dataServer.remote.data.Wish;
import equinox.serverUtilities.BigMessage;

/**
 * Class for get wishes response network message.
 *
 * @author Murat Artim
 * @date 16 Feb 2018
 * @time 16:14:23
 */
public class GetWishesResponse extends DataMessage implements BigMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Wishes. */
	private final ArrayList<Wish> wishes = new ArrayList<>();

	/**
	 * No argument constructor for serialization.
	 */
	public GetWishesResponse() {
	}

	/**
	 * Returns wishes.
	 *
	 * @return wishes.
	 */
	public ArrayList<Wish> getWishes() {
		return wishes;
	}

	/**
	 * Adds given wish.
	 *
	 * @param wish
	 *            Wish to add.
	 */
	public void addWish(Wish wish) {
		wishes.add(wish);
	}

	@Override
	public boolean isReallyBig() {
		return wishes.size() > 10 ? true : false;
	}
}
