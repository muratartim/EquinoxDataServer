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
 * Class for submit access request response network message.
 *
 * @author Murat Artim
 * @date 14 Apr 2018
 * @time 23:06:54
 */
public class SubmitAccessRequestResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Server response index. */
	public static final int SUBMITTED = 0, PENDING = 1, REJECTED = 2;

	/** Server response index. */
	private int response;

	/**
	 * No argument constructor for serialization.
	 */
	public SubmitAccessRequestResponse() {
	}

	/**
	 * Returns server response index.
	 *
	 * @return Server response index.
	 */
	public int getResponse() {
		return response;
	}

	/**
	 * Sets server response index.
	 *
	 * @param response
	 *            Server response index.
	 */
	public void setResponse(int response) {
		this.response = response;
	}
}
