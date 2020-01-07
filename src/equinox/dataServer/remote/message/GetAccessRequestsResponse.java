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

import equinox.dataServer.remote.data.AccessRequest;
import equinox.serverUtilities.BigMessage;

/**
 * Class for get access requests response network message.
 *
 * @author Murat Artim
 * @date 15 Apr 2018
 * @time 01:48:09
 */
public class GetAccessRequestsResponse extends DataMessage implements BigMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Access requests. */
	private final ArrayList<AccessRequest> requests = new ArrayList<>();

	/**
	 * No argument constructor for serialization.
	 */
	public GetAccessRequestsResponse() {
	}

	/**
	 * Returns access requests.
	 *
	 * @return access requests.
	 */
	public ArrayList<AccessRequest> getRequests() {
		return requests;
	}

	/**
	 * Adds given access request.
	 *
	 * @param request
	 *            Access request to add.
	 */
	public void addRequest(AccessRequest request) {
		requests.add(request);
	}

	@Override
	public boolean isReallyBig() {
		return requests.size() > 10 ? true : false;
	}
}
