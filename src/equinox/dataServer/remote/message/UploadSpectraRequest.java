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

import equinox.dataServer.remote.data.SpectrumInfo;
import equinox.serverUtilities.BigMessage;

/**
 * Class for upload spectra request network message.
 *
 * @author Murat Artim
 * @date 15 Mar 2018
 * @time 21:34:33
 */
public class UploadSpectraRequest extends DatabaseQueryRequest implements BigMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** List of spectra. */
	private final ArrayList<SpectrumInfo> spectrumInfo;

	/**
	 * No argument constructor for serialization.
	 */
	public UploadSpectraRequest() {
		spectrumInfo = new ArrayList<>();
	}

	/**
	 * Returns list of spectrum info.
	 *
	 * @return List of spectrum info.
	 */
	public ArrayList<SpectrumInfo> getSpectrumInfo() {
		return spectrumInfo;
	}

	/**
	 * Adds given spectrum info to this message.
	 *
	 * @param info
	 *            Spectrum info.
	 */
	public void addInfo(SpectrumInfo info) {
		spectrumInfo.add(info);
	}

	@Override
	public boolean isReallyBig() {
		return spectrumInfo.size() > 10;
	}
}
