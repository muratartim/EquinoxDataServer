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
 * Class for A/C model info.
 *
 * @author Murat Artim
 * @date 15 Aug 2016
 * @time 17:19:27
 */
public class AircraftModelInfo implements DownloadInfo {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Enumeration for spectrum info types.
	 *
	 * @author Murat Artim
	 * @date May 4, 2014
	 * @time 9:41:10 AM
	 */
	public enum AircraftModelInfoType {

		/** A/C model info type. */
		ID("model_id"), AC_PROGRAM("ac_program"), MODEL_NAME("name"), DELIVERY_REF("delivery_ref"), DESCRIPTION("description"), DATA_SIZE("data_size");

		/** Database column name. */
		private final String columnName_;

		/**
		 * Creates A/C model info constant.
		 *
		 * @param columnName
		 *            Database column name.
		 */
		AircraftModelInfoType(String columnName) {
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
	private final HashMap<AircraftModelInfoType, Object> info_ = new HashMap<>();

	/**
	 * No argument constructor for serialization.
	 */
	public AircraftModelInfo() {
	}

	@Override
	public long getID() {
		return (long) getInfo(AircraftModelInfoType.ID);
	}

	/**
	 * Returns the demanded A/C model info.
	 *
	 * @param type
	 *            Info type.
	 * @return The demanded A/C model info.
	 */
	public Object getInfo(AircraftModelInfoType type) {
		return info_.get(type);
	}

	/**
	 * Sets A/C model info.
	 *
	 * @param type
	 *            Info type.
	 * @param info
	 *            Info to set.
	 */
	public void setInfo(AircraftModelInfoType type, Object info) {
		info_.put(type, info);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(47, 127).append(info_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof AircraftModelInfo))
			return false;
		if (o == this)
			return true;
		AircraftModelInfo info = (AircraftModelInfo) o;
		return new EqualsBuilder().append(info_, info.info_).isEquals();
	}
}
