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

import java.io.Serializable;

/**
 * Class for sample input info.
 *
 * @author Murat Artim
 * @date 15 Mar 2018
 * @time 15:49:16
 */
public class SampleInputInfo implements Serializable {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Input file name and URL. */
	private String name, dataUrl;

	/** Data size in bytes. */
	private long dataSize;

	/**
	 * No argument constructor for serialization.
	 */
	public SampleInputInfo() {
	}

	/**
	 * Returns sample input name.
	 *
	 * @return Sample input name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns data URL.
	 *
	 * @return Data URL.
	 */
	public String getDataUrl() {
		return dataUrl;
	}

	/**
	 * Returns data size in bytes.
	 *
	 * @return Data size in bytes.
	 */
	public long getDataSize() {
		return dataSize;
	}

	/**
	 * Sets sample input name.
	 *
	 * @param name
	 *            Sample input name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets data URL.
	 *
	 * @param dataUrl
	 *            Data URL.
	 */
	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	/**
	 * Sets data size in bytes.
	 *
	 * @param dataSize
	 *            Data size in bytes.
	 */
	public void setDataSize(long dataSize) {
		this.dataSize = dataSize;
	}
}
