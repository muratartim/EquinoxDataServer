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
import equinox.dataServer.remote.data.PilotPointInfo;

/**
 * Class for save pilot point image request network message.
 *
 * @author Murat Artim
 * @date 21 Feb 2018
 * @time 14:19:40
 */
public class SavePilotPointImageRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Pilot point info. */
	private PilotPointInfo pilotPointInfo;

	/** Pilot point image type. */
	private PilotPointImageType imageType;

	/** True if pilot point image should be deleted. */
	private boolean isDelete;

	/**
	 * No argument constructor for serialization.
	 */
	public SavePilotPointImageRequest() {
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
	 * Returns pilot point image type.
	 *
	 * @return Pilot point image type.
	 */
	public PilotPointImageType getImageType() {
		return imageType;
	}

	/**
	 * Returns true if pilot point image should be deleted.
	 *
	 * @return True if pilot point image should be deleted.
	 */
	public boolean isDelete() {
		return isDelete;
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

	/**
	 * Sets pilot point image type.
	 *
	 * @param imageType
	 *            Pilot point image type.
	 */
	public void setImageType(PilotPointImageType imageType) {
		this.imageType = imageType;
	}

	/**
	 * Sets if pilot point image should be deleted.
	 *
	 * @param isDelete
	 *            True if pilot point image should be deleted.
	 */
	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
}
