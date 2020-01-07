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

import equinox.dataServer.remote.data.EquinoxUpdate;
import equinox.serverUtilities.BigMessage;

/**
 * Class for upload container update request network message.
 *
 * @author Murat Artim
 * @date 15 Mar 2018
 * @time 23:25:39
 */
public class UploadContainerUpdateRequest extends DatabaseQueryRequest implements BigMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Equinox updates. */
	private final ArrayList<EquinoxUpdate> updates;

	/** Version description. */
	private String versionDescription;

	/**
	 * No argument constructor for serialization.
	 */
	public UploadContainerUpdateRequest() {
		updates = new ArrayList<>();
	}

	/**
	 * Returns version description string.
	 *
	 * @return Version description string.
	 */
	public String getVersionDescription() {
		return versionDescription;
	}

	/**
	 * Returns Equinox updates. Note that the version description info is null due to message size limits. Version description should be obtained from <code>getVersionDescription()</code> method.
	 *
	 * @return Equinox updates.
	 */
	public ArrayList<EquinoxUpdate> getUpdates() {
		return updates;
	}

	/**
	 * Sets version description string.
	 *
	 * @param versionDescription
	 *            Version description string.
	 */
	public void setVersionDescription(String versionDescription) {
		this.versionDescription = versionDescription;
	}

	/**
	 * Adds Equinox update.
	 *
	 * @param update
	 *            Equinox update.
	 */
	public void addUpdate(EquinoxUpdate update) {
		updates.add(update);
	}

	@Override
	public boolean isReallyBig() {
		return true;
	}
}
