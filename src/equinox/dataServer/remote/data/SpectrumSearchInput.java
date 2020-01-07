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
package equinox.dataServer.remote.data;

import java.util.HashMap;

import equinox.dataServer.remote.data.SpectrumInfo.SpectrumInfoType;

/**
 * Class for spectrum search input.
 *
 * @author Murat Artim
 * @date May 3, 2014
 * @time 7:44:10 PM
 */
public class SpectrumSearchInput extends SearchInput {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Input map. */
	private final HashMap<SpectrumInfoType, SearchItem> inputs_ = new HashMap<>();

	/**
	 * No argument constructor for serialization.
	 */
	public SpectrumSearchInput() {
	}

	/**
	 * Returns demanded type of search item.
	 *
	 * @param type
	 *            Type of search item.
	 * @return Demanded type of search item.
	 */
	public SearchItem getInput(SpectrumInfoType type) {
		return inputs_.get(type);
	}

	/**
	 * Adds given search item to inputs.
	 *
	 * @param type
	 *            Type of search item.
	 * @param input
	 *            Search item to add.
	 */
	public void addInput(SpectrumInfoType type, SearchItem input) {
		inputs_.put(type, input);
	}

	/**
	 * Returns true if there are no search items (i.e. inputs).
	 *
	 * @return True if there are no search items (i.e. inputs).
	 */
	public boolean isEmpty() {
		return inputs_.isEmpty();
	}
}
