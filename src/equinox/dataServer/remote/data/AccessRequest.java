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
package equinox.dataServer.remote.data;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class for access request.
 *
 * @author Murat Artim
 * @date 15 Apr 2018
 * @time 01:50:16
 */
public class AccessRequest implements Serializable {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** Access request status. */
	public static final String PENDING = "pending", GRANTED = "granted", REJECTED = "rejected";

	/**
	 * Enumeration for wish info.
	 *
	 * @author Murat Artim
	 * @date 15 Feb 2018
	 * @time 14:56:13
	 */
	public enum AccessRequestInfo {

		/** Bug report info. */
		ID("id"), USER_ID("user_id"), USER_ALIAS("alias"), USERNAME("username"), USER_ORGANIZATION("Organization"), USER_EMAIL("email"), PERMISSION_NAME("permission_name"), IS_ADMIN_PERMISSION("is_admin_permission"), RECORDED("recorded"), STATUS("status"), CLOSURE("closure"), CLOSED_BY("closed_by");

		/** Database column name. */
		private final String column;

		/**
		 * Creates wish info constant.
		 *
		 * @param column
		 *            Database column name.
		 */
		AccessRequestInfo(String column) {
			this.column = column;
		}

		/**
		 * Returns database column name.
		 *
		 * @return Database column name.
		 */
		public String getColumn() {
			return column;
		}
	}

	/** Bug report info. */
	private final HashMap<AccessRequestInfo, Object> info_ = new HashMap<>();

	/**
	 * No argument constructor for serialization.
	 */
	public AccessRequest() {
	}

	/**
	 * Returns the demanded help video info.
	 *
	 * @param type
	 *            Info type.
	 * @return The demanded help video info.
	 */
	public Object getInfo(AccessRequestInfo type) {
		return info_.get(type);
	}

	/**
	 * Sets help video info.
	 *
	 * @param type
	 *            Info type.
	 * @param info
	 *            Info to set.
	 */
	public void setInfo(AccessRequestInfo type, Object info) {
		info_.put(type, info);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(info_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AccessRequest))
			return false;
		if (o == this)
			return true;
		AccessRequest info = (AccessRequest) o;
		return new EqualsBuilder().append(info_, info.info_).isEquals();
	}
}
