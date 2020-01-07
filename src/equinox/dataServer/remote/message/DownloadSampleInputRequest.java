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

/**
 * Class for download sample input request network message.
 *
 * @author Murat Artim
 * @date 12 Feb 2018
 * @time 00:08:13
 */
public class DownloadSampleInputRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Input sample file name. */
	private String name;

	/**
	 * No argument constructor for serialization.
	 */
	public DownloadSampleInputRequest() {
	}

	/**
	 * Returns name of sample input.
	 * 
	 * @return Name of sample input.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name of sample input.
	 * 
	 * @param name
	 *            Name of sample input.
	 */
	public void setName(String name) {
		this.name = name;
	}
}
