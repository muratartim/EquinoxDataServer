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

import equinox.serverUtilities.NetworkMessage;

/**
 * Abstract class for database query messages.
 *
 * @author Murat Artim
 * @date 12 Dec 2017
 * @time 11:04:27
 */
public abstract class DataMessage implements NetworkMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Hash code of the listening object on the client side. This is used to ensure that the listener object receives the server response. */
	private int listenerHashCode = -1;

	/**
	 * Sets hash code of the listening object on the client side. This is used to ensure that the listening object receives the server response.
	 *
	 * @param listenerHashCode
	 *            Hash code of the listening object on the client side.
	 */
	public void setListenerHashCode(int listenerHashCode) {
		this.listenerHashCode = listenerHashCode;
	}

	/**
	 * Returns hash code of the listening object on the client side, or -1 if listener is determined by the type of message.. This is used to ensure that the listening object receives the server response.
	 *
	 * @return Hash code of the listening object on the client side, or -1 if listener is determined by the type of message.
	 */
	public int getListenerHashCode() {
		return listenerHashCode;
	}
}
