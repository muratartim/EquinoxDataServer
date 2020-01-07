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

import java.util.ArrayList;

/**
 * Class for basic search input.
 *
 * @author Murat Artim
 * @date 19 Jan 2017
 * @time 09:55:53
 */
public class BasicSearchInput extends SearchInput {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Search keywords. */
	private ArrayList<String> keywords_;

	/**
	 * No argument constructor for serialization.
	 */
	public BasicSearchInput() {
	}

	/**
	 * Returns keywords.
	 *
	 * @return Keywords.
	 */
	public ArrayList<String> getKeywords() {
		return keywords_;
	}

	/**
	 * Sets keywords.
	 *
	 * @param keywords
	 *            Keywords.
	 */
	public void setKeywords(ArrayList<String> keywords) {
		keywords_ = keywords;
	}
}
