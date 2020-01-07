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
 * Class for delete users request network message.
 *
 * @author Murat Artim
 * @date 4 Apr 2018
 * @time 18:16:25
 */
public class DeleteUsersRequest extends DatabaseQueryRequest {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** User aliases. */
	private String[] aliases;

	/**
	 * No argument constructor for serialization.
	 */
	public DeleteUsersRequest() {
	}

	/**
	 * Returns user aliases.
	 *
	 * @return User aliases.
	 */
	public String[] getAliases() {
		return aliases;
	}

	/**
	 * Sets user aliases.
	 *
	 * @param aliases
	 *            User aliases.
	 */
	public void setAliases(String[] aliases) {
		this.aliases = aliases;
	}
}
