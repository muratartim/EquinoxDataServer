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

/**
 * Class for get fatigue materials request network message.
 *
 * @author Murat Artim
 * @date 23 Feb 2018
 * @time 12:49:51
 */
public class GetFatigueMaterialsRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** ISAMI version of the requested materials. */
	private String materialIsamiVersion;

	/**
	 * No argument constructor for serialization.
	 */
	public GetFatigueMaterialsRequest() {
	}

	/**
	 * Returns the ISAMI version of the requested materials.
	 * 
	 * @return ISAMI version of the requested materials.
	 */
	public String getMaterialIsamiVersion() {
		return materialIsamiVersion;
	}

	/**
	 * Sets ISAMI version of the requested materials.
	 * 
	 * @param materialIsamiVersion
	 *            ISAMI version of the requested materials.
	 */
	public void setMaterialIsamiVersion(String materialIsamiVersion) {
		this.materialIsamiVersion = materialIsamiVersion;
	}
}
