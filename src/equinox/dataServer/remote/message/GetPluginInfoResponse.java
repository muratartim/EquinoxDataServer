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

import equinox.dataServer.remote.data.ServerPluginInfo;
import equinox.serverUtilities.BigMessage;

/**
 * Class for plugin info response from server.
 *
 * @author Murat Artim
 * @date 16 Dec 2017
 * @time 20:55:40
 */
public class GetPluginInfoResponse extends DataMessage implements BigMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Vector containing the plugin info. */
	private final ArrayList<ServerPluginInfo> plugins_;

	/**
	 * Creates plugin info response message.
	 */
	public GetPluginInfoResponse() {
		plugins_ = new ArrayList<>();
	}

	/**
	 * Resets the plugins list.
	 */
	public void reset() {
		plugins_.clear();
	}

	/**
	 * Adds plugin to message.
	 *
	 * @param plugin
	 *            Plugin to add.
	 */
	public void add(ServerPluginInfo plugin) {
		plugins_.add(plugin);
	}

	/**
	 * Returns the plugin at the given index.
	 *
	 * @param index
	 *            The index of the plugin.
	 *
	 * @return The plugin at the given index.
	 */
	public ServerPluginInfo getPlugin(int index) {
		return plugins_.get(index);
	}

	/**
	 * Returns the size of plugins list.
	 *
	 * @return The size of plugins list.
	 */
	public int size() {
		return plugins_.size();
	}

	@Override
	public boolean isReallyBig() {
		return plugins_.size() > 10 ? true : false;
	}
}
