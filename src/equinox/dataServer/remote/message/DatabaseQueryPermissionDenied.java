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

import equinox.serverUtilities.Permission;

/**
 * Class for database query permission denied network message.
 *
 * @author Murat Artim
 * @date 18 Feb 2018
 * @time 19:10:50
 */
public class DatabaseQueryPermissionDenied extends DataMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** The denied permission. */
	private Permission permission;

	/**
	 * Empty constructor for serialization.
	 */
	public DatabaseQueryPermissionDenied() {
	}

	/**
	 * Creates permission denied message.
	 *
	 * @param permission
	 *            The denied permission.
	 */
	public DatabaseQueryPermissionDenied(Permission permission) {
		this.permission = permission;
	}

	/**
	 * Returns the denied permission.
	 *
	 * @return The denied permission.
	 */
	public Permission getPermission() {
		return permission;
	}

	/**
	 * Sets the denied permission.
	 *
	 * @param permission
	 *            Denied permission.
	 */
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
}
