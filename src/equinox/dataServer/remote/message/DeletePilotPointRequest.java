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
 * Class for delete pilot point request network message.
 *
 * @author Murat Artim
 * @date 9 Feb 2018
 * @time 11:31:01
 */
public class DeletePilotPointRequest extends DatabaseQueryRequest {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** Pilot point to delete. */
	private PilotPointInfo pilotPointInfo;

	/**
	 * No argument constructor for serialization.
	 */
	public DeletePilotPointRequest() {
	}

	/**
	 * Returns pilot point info.
	 *
	 * @return Pilot point info.
	 */
	public PilotPointInfo getPilotPointInfo() {
		return pilotPointInfo;
	}

	/**
	 * Sets pilot point info.
	 *
	 * @param pilotPointInfo
	 *            Pilot point info.
	 */
	public void setPilotPointInfo(PilotPointInfo pilotPointInfo) {
		this.pilotPointInfo = pilotPointInfo;
	}
}
