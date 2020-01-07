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

/**
 * Class for check for material updates response network message.
 *
 * @author Murat Artim
 * @date 26 Jan 2018
 * @time 14:42:27
 */
public class CheckForMaterialUpdatesResponse extends DataMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The material ISAMI versions to be downloaded. */
	private final ArrayList<String> materialIsamiVersions;

	/**
	 * No argument constructor for serialization.
	 */
	public CheckForMaterialUpdatesResponse() {
		materialIsamiVersions = new ArrayList<>();
	}

	/**
	 * Returns true if material library update is available.
	 *
	 * @return True if material library update is available.
	 */
	public boolean isUpdateAvailable() {
		return !materialIsamiVersions.isEmpty();
	}

	/**
	 * Returns material ISAMI versions to be downloaded from the server.
	 *
	 * @return Material ISAMI versions to be downloaded from the server.
	 */
	public ArrayList<String> getMaterialIsamiVersions() {
		return materialIsamiVersions;
	}

	/**
	 * Adds material ISAMI version to be downloaded.
	 *
	 * @param materialIsamiVersion
	 *            Material ISAMI version.
	 */
	public void addMaterialIsamiVersion(String materialIsamiVersion) {
		materialIsamiVersions.add(materialIsamiVersion);
	}
}
