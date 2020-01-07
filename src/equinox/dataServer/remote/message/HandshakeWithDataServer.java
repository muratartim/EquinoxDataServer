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
import java.util.List;

/**
 * Class for handshake message.
 *
 * @version 1.0
 * @author Murat Artim
 * @time 1:43:02 PM
 * @date Jan 29, 2011
 *
 */
public final class HandshakeWithDataServer extends DataMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** User alias and username. */
	private String alias_, username_;

	/** Server reply to handshake message. */
	private boolean isSuccessful_, isAdmin_ = false;

	/** List of non-administrative permission names. */
	private final List<String> permissionNames_ = new ArrayList<>();

	/**
	 * No argument constructor for serialization.
	 */
	public HandshakeWithDataServer() {
	}

	/**
	 * Creates a handshake message.
	 *
	 * @param alias
	 *            User alias.
	 */
	public HandshakeWithDataServer(String alias) {
		alias_ = alias;
	}

	/**
	 * Returns the user alias.
	 *
	 * @return The user alias.
	 */
	public String getAlias() {
		return alias_;
	}

	/**
	 * Returns the username of the client.
	 *
	 * @return The username of the client.
	 */
	public String getUsername() {
		return username_;
	}

	/**
	 * Returns user non-administrative permission names.
	 *
	 * @return User non-administrative permission names.
	 */
	public List<String> getPermissionNames() {
		return permissionNames_;
	}

	/**
	 * Returns true if the user is an administrator.
	 *
	 * @return True if the user is an administrator.
	 */
	public boolean isAdministrator() {
		return isAdmin_;
	}

	/**
	 * Returns true if handshake is successful.
	 *
	 * @return True if handshake is successful.
	 */
	public boolean isHandshakeSuccessful() {
		return isSuccessful_;
	}

	/**
	 * Sets reply to this handshake message.
	 *
	 * @param isSuccessful
	 *            True if handshake is successful.
	 */
	public void setReply(boolean isSuccessful) {
		isSuccessful_ = isSuccessful;
	}

	/**
	 * Sets username of the client.
	 *
	 * @param username
	 *            Username of the client.
	 */
	public void setUsername(String username) {
		username_ = username;
	}

	/**
	 * Adds given non-administrative user permission name.
	 *
	 * @param permissionName
	 *            Non-administrative user permission name.
	 */
	public void addPermissionName(String permissionName) {
		permissionNames_.add(permissionName);
	}

	/**
	 * Sets whether the requesting equinoxServer.client is administrator or not.
	 *
	 * @param isAdmin
	 *            True if the user is an administrator.
	 */
	public void setAsAdministrator(boolean isAdmin) {
		isAdmin_ = isAdmin;
	}
}
