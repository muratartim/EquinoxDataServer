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
 * Class for add new user request network message.
 *
 * @author Murat Artim
 * @date 4 Apr 2018
 * @time 16:32:40
 */
public class AddNewUserRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** User info. */
	private String alias, name, organization, email, imageUrl;

	/** User permissions. */
	private Permission[] permissions;

	/**
	 * No argument constructor for serialization.
	 */
	public AddNewUserRequest() {
	}

	/**
	 * Returns user alias.
	 *
	 * @return User alias.
	 */
	public String getAlias() {
		return alias;
	}

	/**
	 * Returns user name.
	 *
	 * @return User name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns user organization.
	 *
	 * @return User organization.
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 * Returns user email.
	 *
	 * @return User email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns user profile image URL, or <code>null</code> if no image supplied.
	 *
	 * @return User profile image URL, or <code>null</code> if no image supplied.
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * Returns user permissions.
	 *
	 * @return User permissions.
	 */
	public Permission[] getPermissions() {
		return permissions;
	}

	/**
	 * Sets user alias.
	 *
	 * @param alias
	 *            User alias.
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * Sets user name.
	 *
	 * @param name
	 *            User name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets user organization.
	 *
	 * @param organization
	 *            User organization.
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * Sets user email.
	 *
	 * @param email
	 *            User email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Sets user profile image URL.
	 *
	 * @param imageUrl
	 *            User profile image URL.
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * Sets user permissions.
	 *
	 * @param permissions
	 *            User permissions.
	 */
	public void setPermissions(Permission[] permissions) {
		this.permissions = permissions;
	}
}
