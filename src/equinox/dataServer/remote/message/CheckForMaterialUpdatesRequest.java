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
 * Class for check for material updates request network message.
 *
 * @author Murat Artim
 * @date 26 Jan 2018
 * @time 14:38:05
 */
public class CheckForMaterialUpdatesRequest extends DatabaseQueryRequest {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The material ISAMI versions of the client. */
	private final ArrayList<String> materialIsamiVersions;

	/**
	 * No argument constructor for serialization.
	 */
	public CheckForMaterialUpdatesRequest() {
		materialIsamiVersions = new ArrayList<>();
	}

	/**
	 * Returns the material ISAMI versions of the client.
	 *
	 * @return The material ISAMI versions of the client.
	 */
	public ArrayList<String> getMaterialIsamiVersions() {
		return materialIsamiVersions;
	}

	/**
	 * Adds material ISAMI version.
	 *
	 * @param materialIsamiVersion
	 *            Material ISAMI version.
	 */
	public void addMaterialIsamiVersion(String materialIsamiVersion) {
		materialIsamiVersions.add(materialIsamiVersion);
	}
}
