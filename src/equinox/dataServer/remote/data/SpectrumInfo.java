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
 * Class for spectrum info.
 *
 * @author Murat Artim
 * @date May 4, 2014
 * @time 9:28:10 AM
 */
public class SpectrumInfo implements DownloadInfo {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Enumeration for spectrum info types.
	 *
	 * @author Murat Artim
	 * @date May 4, 2014
	 * @time 9:41:10 AM
	 */
	public enum SpectrumInfoType {

		/** CDF info type. */
		ID("id"), NAME("name"), AC_PROGRAM("ac_program"), AC_SECTION("ac_section"), FAT_MISSION("fat_mission"), FAT_MISSION_ISSUE("fat_mission_issue"), FLP_ISSUE("flp_issue"), IFLP_ISSUE("iflp_issue"), CDF_ISSUE("cdf_issue"), DELIVERY_REF("delivery_ref"), DESCRIPTION("description"),
		DATA_SIZE("data_size"), DATA_URL("data_url"), PILOT_POINTS(""), MULT_TABLES("");

		/** Database column name. */
		private final String columnName_;

		/**
		 * Creates spectrum info constant.
		 *
		 * @param columnName
		 *            Database column name.
		 */
		SpectrumInfoType(String columnName) {
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
	private final HashMap<SpectrumInfoType, Object> info_ = new HashMap<>();

	/**
	 * No argument constructor for serialization.
	 */
	public SpectrumInfo() {
	}

	@Override
	public long getID() {
		return (long) getInfo(SpectrumInfoType.ID);
	}

	/**
	 * Returns the demanded spectrum info.
	 *
	 * @param type
	 *            Info type.
	 * @return The demanded spectrum info.
	 */
	public Object getInfo(SpectrumInfoType type) {
		return info_.get(type);
	}

	/**
	 * Sets spectrum info.
	 *
	 * @param type
	 *            Info type.
	 * @param info
	 *            Info to set.
	 */
	public void setInfo(SpectrumInfoType type, Object info) {
		info_.put(type, info);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(95, 127).append(info_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SpectrumInfo))
			return false;
		if (o == this)
			return true;
		SpectrumInfo info = (SpectrumInfo) o;
		return new EqualsBuilder().append(info_, info.info_).isEquals();
	}
}
