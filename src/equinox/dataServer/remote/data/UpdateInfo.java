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

import java.util.HashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class for Equinox update info.
 *
 * @author Murat Artim
 * @date Apr 3, 2016
 * @time 9:42:09 PM
 */
public class UpdateInfo implements DownloadInfo {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Enumeration for help video info types.
	 *
	 * @author Murat Artim
	 * @date May 4, 2014
	 * @time 9:41:10 AM
	 */
	public enum UpdateInfoType {

		/** Multiplication table info type. */
		ID("id"), VERSION_NUMBER("version_number"), UPLOAD_DATE("upload_date"), OS_TYPE("os_type"), DATA_SIZE("data_size");

		/** Database column name. */
		private final String columnName_;

		/**
		 * Creates help video info constant.
		 *
		 * @param columnName
		 *            Database column name.
		 */
		UpdateInfoType(String columnName) {
			columnName_ = columnName;
		}

		/**
		 * Returns the database column name of the info.
		 *
		 * @return Database column name.
		 */
		public String getColumnName() {
			return columnName_;
		}
	}

	/** Map containing the info. */
	private final HashMap<UpdateInfoType, Object> info_ = new HashMap<>();

	/**
	 * No argument constructor for serialization.
	 */
	public UpdateInfo() {
	}

	@Override
	public long getID() {
		return (long) getInfo(UpdateInfoType.ID);
	}

	/**
	 * Returns the demanded help video info.
	 *
	 * @param type
	 *            Info type.
	 * @return The demanded help video info.
	 */
	public Object getInfo(UpdateInfoType type) {
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
	public void setInfo(UpdateInfoType type, Object info) {
		info_.put(type, info);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(99, 21).append(info_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof UpdateInfo))
			return false;
		if (o == this)
			return true;
		UpdateInfo info = (UpdateInfo) o;
		return new EqualsBuilder().append(info_, info.info_).isEquals();
	}
}
