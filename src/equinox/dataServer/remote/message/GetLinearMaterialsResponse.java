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

import equinox.dataServer.remote.data.LinearMaterial;
import equinox.serverUtilities.BigMessage;

/**
 * Class for get linear materials response network message.
 *
 * @author Murat Artim
 * @date 23 Feb 2018
 * @time 13:10:44
 */
public class GetLinearMaterialsResponse extends DataMessage implements BigMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Linear materials. */
	private final ArrayList<LinearMaterial> materials;

	/**
	 * No argument constructor for serialization.
	 */
	public GetLinearMaterialsResponse() {
		materials = new ArrayList<>();
	}

	/**
	 * Returns linear materials.
	 *
	 * @return linear materials.
	 */
	public ArrayList<LinearMaterial> getMaterials() {
		return materials;
	}

	/**
	 * Adds given material.
	 *
	 * @param material
	 *            Material to add.
	 */
	public void addMaterial(LinearMaterial material) {
		materials.add(material);
	}

	@Override
	public boolean isReallyBig() {
		return materials.size() > 10;
	}
}
