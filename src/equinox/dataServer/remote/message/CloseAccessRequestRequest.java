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
 * Class for close access request request network message.
 *
 * @author Murat Artim
 * @date 15 Apr 2018
 * @time 20:30:34
 */
public class CloseAccessRequestRequest extends DatabaseQueryRequest {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Request ID. */
	private long requestId;

	/** Closure text. */
	private String closure;

	/** True if access is granted. */
	private boolean isGrantAccess;

	/**
	 * No argument constructor for serialization.
	 */
	public CloseAccessRequestRequest() {
	}

	/**
	 * Returns the request ID.
	 *
	 * @return The request ID.
	 */
	public long getRequestId() {
		return requestId;
	}

	/**
	 * Returns the closure text.
	 *
	 * @return Closure text.
	 */
	public String getClosure() {
		return closure;
	}

	/**
	 * Returns true if access is granted.
	 *
	 * @return True if access is granted.
	 */
	public boolean isGrantAccess() {
		return isGrantAccess;
	}

	/**
	 * Sets request ID.
	 *
	 * @param requestId
	 *            Request ID.
	 */
	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	/**
	 * Sets closure text.
	 *
	 * @param closure
	 *            Closure text.
	 */
	public void setClosure(String closure) {
		this.closure = closure;
	}

	/**
	 * Sets if access is granted.
	 *
	 * @param isGrantAccess
	 *            True if access is granted.
	 */
	public void setIsGrantAccess(boolean isGrantAccess) {
		this.isGrantAccess = isGrantAccess;
	}
}
