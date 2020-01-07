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
 * Class for delete multiplication table request network message.
 *
 * @author Murat Artim
 * @date 9 Feb 2018
 * @time 09:56:34
 */
public class DeleteMultiplicationTableRequest extends DatabaseQueryRequest {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** Multiplication table to delete. */
	private MultiplicationTableInfo multiplicationTableInfo;

	/**
	 * No argument constructor for serialization.
	 */
	public DeleteMultiplicationTableRequest() {
	}

	/**
	 * Returns multiplication table info.
	 *
	 * @return Multiplication table info.
	 */
	public MultiplicationTableInfo getMultiplicationTableInfo() {
		return multiplicationTableInfo;
	}

	/**
	 * Sets multiplication table info.
	 *
	 * @param multiplicationTableInfo
	 *            Multiplication table info.
	 */
	public void setMultiplicationTableInfo(MultiplicationTableInfo multiplicationTableInfo) {
		this.multiplicationTableInfo = multiplicationTableInfo;
	}
}
