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
package equinox.dataServer.remote.listener;

import java.io.Serializable;

import equinox.dataServer.remote.message.DataMessage;

/**
 * Interface for all data server listeners.
 *
 * @author Murat Artim
 * @date 12 Dec 2017
 * @time 12:16:05
 */
public interface DataMessageListener extends Serializable {

	/**
	 * Responds to given server data message.
	 *
	 * @param message
	 *            Server data message to respond.
	 * @throws Exception
	 *             If exception occurs during responding the server data message.
	 */
	void respondToDataMessage(DataMessage message) throws Exception;
}
