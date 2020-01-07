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
 * Class for get spectrum counts response network message.
 *
 * @author Murat Artim
 * @date 27 Jul 2018
 * @time 22:29:05
 */
public class GetSpectrumCountsResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Popular search hits. */
	private Map<String, Integer> spectrumCounts;

	/**
	 * No argument constructor for serialization.
	 */
	public GetSpectrumCountsResponse() {
	}

	/**
	 * Returns spectrum counts.
	 *
	 * @return Spectrum counts.
	 */
	public Map<String, Integer> getSpectrumCounts() {
		return spectrumCounts;
	}

	/**
	 * Sets spectrum counts.
	 *
	 * @param spectrumCounts
	 *            Spectrum counts.
	 */
	public void setSpectrumCounts(Map<String, Integer> spectrumCounts) {
		this.spectrumCounts = spectrumCounts;
	}
}
