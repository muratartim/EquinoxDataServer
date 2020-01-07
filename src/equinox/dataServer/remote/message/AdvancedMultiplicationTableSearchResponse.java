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

import equinox.dataServer.remote.data.DownloadInfo;
import equinox.serverUtilities.BigMessage;

/**
 * Class for advanced multiplication table search response network message.
 *
 * @author Murat Artim
 * @date 23 Jan 2018
 * @time 00:47:59
 */
public class AdvancedMultiplicationTableSearchResponse extends DataMessage implements BigMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Search results. */
	private final ArrayList<DownloadInfo> searchResults;

	/**
	 * Creates advanced multiplication table search response network message.
	 */
	public AdvancedMultiplicationTableSearchResponse() {
		searchResults = new ArrayList<>();
	}

	/**
	 * Resets the search results list.
	 */
	public void reset() {
		searchResults.clear();
	}

	/**
	 * Adds search result to message.
	 *
	 * @param result
	 *            search result to add.
	 */
	public void add(DownloadInfo result) {
		searchResults.add(result);
	}

	/**
	 * Returns the search results.
	 *
	 * @return The search results.
	 */
	public ArrayList<DownloadInfo> getSearchResults() {
		return searchResults;
	}

	@Override
	public boolean isReallyBig() {
		return searchResults.size() > 10 ? true : false;
	}
}
