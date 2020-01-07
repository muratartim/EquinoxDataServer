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

import equinox.dataServer.remote.data.BasicSearchInput;

/**
 * Class for basic spectrum search request network message.
 *
 * @author Murat Artim
 * @date 24 Jan 2018
 * @time 15:20:07
 */
public class BasicSpectrumSearchRequest extends DatabaseQueryRequest {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Search input. */
	private BasicSearchInput input;

	/**
	 * Empty constructor for serialization.
	 */
	public BasicSpectrumSearchRequest() {
	}

	/**
	 * Sets search input.
	 *
	 * @param input
	 *            Search input.
	 */
	public void setInput(BasicSearchInput input) {
		this.input = input;
	}

	/**
	 * Returns search input.
	 *
	 * @return Search input.
	 */
	public BasicSearchInput getInput() {
		return input;
	}
}
