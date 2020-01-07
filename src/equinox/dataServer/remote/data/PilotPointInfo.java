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
 * Class for pilot point download info.
 *
 * @author Murat Artim
 * @date Feb 12, 2016
 * @time 4:33:18 PM
 */
public class PilotPointInfo implements DownloadInfo {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Enumeration for pilot point info types.
	 *
	 * @author Murat Artim
	 * @date May 4, 2014
	 * @time 9:41:10 AM
	 */
	public enum PilotPointInfoType {

		/** Pilot point info type. */
		ID("id"), SPECTRUM_NAME("spectrum_name"), NAME("name"), FAT_MISSION("fat_mission"), DESCRIPTION("description"), ELEMENT_TYPE("element_type"), FRAME_RIB_POSITION("frame_rib_position"), STRINGER_POSITION("stringer_position"), DATA_SOURCE("data_source"), GENERATION_SOURCE("generation_source"),
		DELIVERY_REF_NUM("delivery_ref_num"), ISSUE("issue"), FATIGUE_MATERIAL("fatigue_material"), PREFFAS_MATERIAL("preffas_material"), LINEAR_MATERIAL("linear_material"), AC_PROGRAM("ac_program"), AC_SECTION("ac_section"), EID("eid");

		/** Database column name. */
		private final String columnName_;

		/**
		 * Creates pilot point info constant.
		 *
		 * @param columnName
		 *            Database column name.
		 */
		PilotPointInfoType(String columnName) {
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
	private final HashMap<PilotPointInfoType, Object> info_ = new HashMap<>();

	/**
	 * No argument constructor for serialization.
	 */
	public PilotPointInfo() {
	}

	@Override
	public long getID() {
		return (long) getInfo(PilotPointInfoType.ID);
	}

	/**
	 * Returns the demanded pilot point info.
	 *
	 * @param type
	 *            Info type.
	 * @return The demanded pilot point info.
	 */
	public Object getInfo(PilotPointInfoType type) {
		return info_.get(type);
	}

	/**
	 * Sets pilot point info.
	 *
	 * @param type
	 *            Info type.
	 * @param info
	 *            Info to set.
	 */
	public void setInfo(PilotPointInfoType type, Object info) {
		if ((info != null) && (info instanceof String)) {
			String string = (String) info;
			if (string.equals("-") || string.trim().isEmpty()) {
				info = null;
			}
		}
		info_.put(type, info);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(45, 89).append(info_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PilotPointInfo))
			return false;
		if (o == this)
			return true;
		PilotPointInfo info = (PilotPointInfo) o;
		return new EqualsBuilder().append(info_, info.info_).isEquals();
	}
}
