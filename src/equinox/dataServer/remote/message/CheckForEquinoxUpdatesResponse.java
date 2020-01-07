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

import equinox.dataServer.remote.data.EquinoxUpdate;
import equinox.dataServer.remote.data.EquinoxUpdate.EquinoxUpdateInfoType;
import equinox.serverUtilities.BigMessage;

/**
 * Class for check for Equinox update response network message.
 *
 * @author Murat Artim
 * @date 26 Jan 2018
 * @time 10:43:09
 */
public class CheckForEquinoxUpdatesResponse extends DataMessage implements BigMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Update information. */
	private EquinoxUpdate update;

	/**
	 * No argument constructor for serialization.
	 */
	public CheckForEquinoxUpdatesResponse() {
	}

	/**
	 * Returns update info.
	 *
	 * @return Update info.
	 */
	public EquinoxUpdate getUpdate() {
		return update;
	}

	/**
	 * Sets update info.
	 *
	 * @param update
	 *            Update info.
	 */
	public void setUpdate(EquinoxUpdate update) {
		this.update = update;
	}

	@Override
	public boolean isReallyBig() {
		if (update == null)
			return false;
		String desc = (String) update.getInfo(EquinoxUpdateInfoType.VERSION_DESCRIPTION);
		if (desc == null)
			return false;
		if (desc.length() <= 5000)
			return false;
		return true;
	}
}
