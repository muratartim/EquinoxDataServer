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
 * Class for server plugin info.
 *
 * @author Murat Artim
 * @date Mar 31, 2015
 * @time 9:57:33 AM
 */
public class ServerPluginInfo implements Serializable {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Enumeration for plugin info type.
	 * 
	 * @author Murat Artim
	 * @date 9 Feb 2018
	 * @time 14:49:04
	 */
	public enum PluginInfoType {

		/** Plugin info type. */
		ID("id"), NAME("name"), JAR_NAME("jar_name"), DESCRIPTION("description"), VERSION_NUMBER("version_number"), IMAGE_URL("image_url"), DATA_SIZE("data_size"), DEVELOPER_NAME("developer_name"), DEVELOPER_EMAIL("developer_email"), DATA_URL("data_url");

		/** Database column name. */
		private final String columnName_;

		/**
		 * Creates help plugin info type.
		 *
		 * @param columnName
		 *            Database column name.
		 */
		PluginInfoType(String columnName) {
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
	private final HashMap<PluginInfoType, Object> info_ = new HashMap<>();

	/**
	 * No argument constructor for serialization.
	 */
	public ServerPluginInfo() {
	}

	/**
	 * Returns the demanded help video info.
	 *
	 * @param type
	 *            Info type.
	 * @return The demanded help video info.
	 */
	public Object getInfo(PluginInfoType type) {
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
	public void setInfo(PluginInfoType type, Object info) {
		info_.put(type, info);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(info_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ServerPluginInfo))
			return false;
		if (o == this)
			return true;
		ServerPluginInfo info = (ServerPluginInfo) o;
		return new EqualsBuilder().append(info_, info.info_).isEquals();
	}
}
