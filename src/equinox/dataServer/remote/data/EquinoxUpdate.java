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
 * Class for update object.
 *
 * @author Murat Artim
 * @date May 26, 2014
 * @time 3:47:59 PM
 */
public class EquinoxUpdate implements Serializable {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Enumeration for Equinox update info type.
	 *
	 * @author Murat Artim
	 * @date 15 Mar 2018
	 * @time 23:11:57
	 */
	public enum EquinoxUpdateInfoType {

		/** Equinox update info type. */
		ID("id"), VERSION_NUMBER("version_number"), UPLOAD_DATE("upload_date"), VERSION_DESCRIPTION("version_description"), OS_TYPE("os_type"), OS_ARCH("os_arch"), DATA_URL("data_url"), DATA_SIZE("data_size");

		/** Database column name. */
		private final String columnName;

		/**
		 * Creates Equinox update info type constant.
		 *
		 * @param columnName
		 *            Database column name.
		 */
		EquinoxUpdateInfoType(String columnName) {
			this.columnName = columnName;
		}

		/**
		 * Returns database column name.
		 *
		 * @return Database column name.
		 */
		public String getColumnName() {
			return columnName;
		}
	}

	/** Map containing the info. */
	private final HashMap<EquinoxUpdateInfoType, Object> info_ = new HashMap<>();

	/**
	 * No argument constructor for serialization.
	 */
	public EquinoxUpdate() {
	}

	/**
	 * Returns the demanded help video info.
	 *
	 * @param type
	 *            Info type.
	 * @return The demanded help video info.
	 */
	public Object getInfo(EquinoxUpdateInfoType type) {
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
	public void setInfo(EquinoxUpdateInfoType type, Object info) {
		info_.put(type, info);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(info_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof EquinoxUpdate))
			return false;
		if (o == this)
			return true;
		EquinoxUpdate info = (EquinoxUpdate) o;
		return new EqualsBuilder().append(info_, info.info_).isEquals();
	}
}
