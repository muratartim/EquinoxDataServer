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
 * Class for multiplication table info.
 *
 * @author Murat Artim
 * @date Feb 29, 2016
 * @time 11:32:49 AM
 */
public class MultiplicationTableInfo implements DownloadInfo {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Enumeration for multiplication table info types.
	 *
	 * @author Murat Artim
	 * @date May 4, 2014
	 * @time 9:41:10 AM
	 */
	public enum MultiplicationTableInfoType {

		/** Multiplication table info type. */
		ID("id"), NAME("name"), SPECTRUM_NAME("spectrum_name"), PILOT_POINT_NAME("pilot_point_name"), AC_PROGRAM("ac_program"), AC_SECTION("ac_section"), FAT_MISSION("fat_mission"), ISSUE("issue"), DELIVERY_REF("delivery_ref_num"), DESCRIPTION("description"), DATA_URL("data_url");

		/** Database column name. */
		private final String columnName_;

		/**
		 * Creates multiplication table info constant.
		 *
		 * @param columnName
		 *            Database column name.
		 */
		MultiplicationTableInfoType(String columnName) {
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
	private final HashMap<MultiplicationTableInfoType, Object> info_ = new HashMap<>();

	/**
	 * No argument constructor for serialization.
	 */
	public MultiplicationTableInfo() {
	}

	@Override
	public long getID() {
		return (long) getInfo(MultiplicationTableInfoType.ID);
	}

	/**
	 * Returns the demanded multiplication table info.
	 *
	 * @param type
	 *            Info type.
	 * @return The demanded multiplication table info.
	 */
	public Object getInfo(MultiplicationTableInfoType type) {
		return info_.get(type);
	}

	/**
	 * Sets multiplication table info.
	 *
	 * @param type
	 *            Info type.
	 * @param info
	 *            Info to set.
	 */
	public void setInfo(MultiplicationTableInfoType type, Object info) {
		info_.put(type, info);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(77, 87).append(info_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof MultiplicationTableInfo))
			return false;
		if (o == this)
			return true;
		MultiplicationTableInfo info = (MultiplicationTableInfo) o;
		return new EqualsBuilder().append(info_, info.info_).isEquals();
	}
}
