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

import equinox.dataServer.remote.data.PilotPointInfo;

/**
 * Class for update pilot point request network message.
 *
 * @author Murat Artim
 * @date 28 Feb 2018
 * @time 17:19:16
 */
public class UpdatePilotPointRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Pilot point to update. */
	private PilotPointInfo info;

	/**
	 * No argument constructor for serialization.
	 */
	public UpdatePilotPointRequest() {
	}

	/**
	 * Returns pilot point info.
	 *
	 * @return Pilot point info.
	 */
	public PilotPointInfo getInfo() {
		return info;
	}

	/**
	 * Sets pilot point info.
	 *
	 * @param info
	 *            Pilot point info.
	 */
	public void setInfo(PilotPointInfo info) {
		this.info = info;
	}
}
