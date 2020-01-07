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

import equinox.dataServer.remote.data.ServerPluginInfo;

/**
 * Class for upload plugin request network message.
 *
 * @author Murat Artim
 * @date 15 Mar 2018
 * @time 11:38:15
 */
public class UploadPluginRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Plugin info. */
	private ServerPluginInfo pluginInfo;

	/**
	 * No argument constructor for serialization.
	 */
	public UploadPluginRequest() {
	}

	/**
	 * Returns plugin info.
	 *
	 * @return Plugin info.
	 */
	public ServerPluginInfo getPluginInfo() {
		return pluginInfo;
	}

	/**
	 * Sets plugin info.
	 *
	 * @param pluginInfo
	 *            Plugin info.
	 */
	public void setPluginInfo(ServerPluginInfo pluginInfo) {
		this.pluginInfo = pluginInfo;
	}
}
