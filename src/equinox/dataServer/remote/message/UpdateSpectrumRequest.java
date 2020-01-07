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

import equinox.dataServer.remote.data.SpectrumInfo;

/**
 * Class for update spectrum request network message.
 *
 * @author Murat Artim
 * @date 28 Feb 2018
 * @time 17:38:18
 */
public class UpdateSpectrumRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Spectrum to update. */
	private SpectrumInfo info;

	/**
	 * No argument constructor for serialization.
	 */
	public UpdateSpectrumRequest() {
	}

	/**
	 * Returns spectrum info.
	 *
	 * @return Spectrum info.
	 */
	public SpectrumInfo getInfo() {
		return info;
	}

	/**
	 * Sets spectrum info.
	 *
	 * @param info
	 *            Spectrum info.
	 */
	public void setInfo(SpectrumInfo info) {
		this.info = info;
	}
}
