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

/**
 * Enumeration for damage contribution type.
 *
 * @author Murat Artim
 * @date Apr 15, 2015
 * @time 11:39:47 AM
 */
public enum ContributionType {

	/** Contribution type. */
	INCREMENT("Increment", "INC"), ONEG("1G", "ONEG"), DELTA_P("Delta-P", "DP"), DELTA_T("Delta-T", "DT"), GAG("Ground-Air-Ground", "GAG");

	/** Name of contribution type. */
	private final String name_, columnName_;

	/**
	 * Creates contribution type.
	 *
	 * @param name
	 *            Name of contribution type.
	 * @param columnName
	 *            Global database column name.
	 */
	ContributionType(String name, String columnName) {
		name_ = name;
		columnName_ = columnName;
	}

	/**
	 * Returns the name of contribution type.
	 *
	 * @return The name of contribution type.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns the global database column name.
	 * 
	 * @return The global database column name.
	 */
	public String getColumnName() {
		return columnName_;
	}

	@Override
	public String toString() {
		return name_;
	}
}
