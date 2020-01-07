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
 * Class for login successful network message.
 *
 * @author Murat Artim
 * @date 20 Dec 2017
 * @time 17:18:38
 */
public class LoginSuccessful extends DataMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** List of administrative permissions. */
	private final List<String> adminPermissionNames_ = new ArrayList<>();

	/**
	 * No argument constructor for serialization.
	 */
	public LoginSuccessful() {
	}

	/**
	 * Returns user administrative permissions.
	 *
	 * @return User administrative permissions.
	 */
	public List<String> getAdminPermissionNames() {
		return adminPermissionNames_;
	}

	/**
	 * Adds administrative permission name.
	 *
	 * @param adminPermissionName
	 *            Administrative permission name.
	 */
	public void addAdminPermissionName(String adminPermissionName) {
		if (adminPermissionName == null)
			return;
		if (!adminPermissionNames_.contains(adminPermissionName)) {
			adminPermissionNames_.add(adminPermissionName);
		}
	}
}
