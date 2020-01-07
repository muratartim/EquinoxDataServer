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

import equinox.dataServer.remote.data.PilotPointImageType;

/**
 * Class for get pilot point image request network message.
 *
 * @author Murat Artim
 * @date 16 Feb 2018
 * @time 11:19:42
 */
public class GetPilotPointImageRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Pilot point id. */
	private long pilotPointId;

	/** Pilot point image type. */
	private PilotPointImageType imageType;

	/**
	 * No argument constructor for serialization.
	 */
	public GetPilotPointImageRequest() {
	}

	/**
	 * Returns pilot point id.
	 *
	 * @return Pilot point id.
	 */
	public long getPilotPointId() {
		return pilotPointId;
	}

	/**
	 * Returns pilot point image type.
	 *
	 * @return Pilot point image type.
	 */
	public PilotPointImageType getImageType() {
		return imageType;
	}

	/**
	 * Sets pilot point id.
	 *
	 * @param pilotPointId
	 *            Pilot point id.
	 */
	public void setPilotPointId(long pilotPointId) {
		this.pilotPointId = pilotPointId;
	}

	/**
	 * Sets pilot point image type.
	 *
	 * @param imageType
	 *            Pilot point image type.
	 */
	public void setImageType(PilotPointImageType imageType) {
		this.imageType = imageType;
	}
}
