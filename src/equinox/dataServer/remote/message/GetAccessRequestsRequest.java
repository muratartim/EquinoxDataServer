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
 * Class for get access requests request network message.
 *
 * @author Murat Artim
 * @date 15 Apr 2018
 * @time 01:46:03
 */
public class GetAccessRequestsRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Status index. */
	public static int ALL = 0, PENDING = 1, GRANTED = 2, REJECTED = 3;

	/** Status of requested access requests. */
	private int status;

	/**
	 * No argument constructor for serialization.
	 */
	public GetAccessRequestsRequest() {
	}

	/**
	 * Returns the status of requested access requests.
	 *
	 * @return Status of requested access requests.
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Sets the status of the requested access requests.
	 *
	 * @param status
	 *            Status of the requested access requests.
	 */
	public void setStatus(int status) {
		this.status = status;
	}
}
