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

import equinox.dataServer.remote.data.MultiplicationTableInfo;

/**
 * Class for update multiplication table info request network message.
 *
 * @author Murat Artim
 * @date 28 Feb 2018
 * @time 15:04:10
 */
public class UpdateMultiplicationTableRequest extends DatabaseQueryRequest {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Multiplication table to update. */
	private MultiplicationTableInfo info;

	/**
	 * No argument constructor for serialization.
	 */
	public UpdateMultiplicationTableRequest() {
	}

	/**
	 * Returns the multiplication table info.
	 *
	 * @return Multiplication table info.
	 */
	public MultiplicationTableInfo getInfo() {
		return info;
	}

	/**
	 * Sets multiplication table info.
	 *
	 * @param info
	 *            Multiplication table info.
	 */
	public void setInfo(MultiplicationTableInfo info) {
		this.info = info;
	}
}
