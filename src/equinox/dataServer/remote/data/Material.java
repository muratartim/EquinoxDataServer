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

/**
 * Abstract class for materials.
 *
 * @author Murat Artim
 * @date Nov 30, 2015
 * @time 3:45:21 PM
 */
public abstract class Material implements Serializable {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Material ID. */
	protected final long id_;

	/** Common attributes of material. */
	protected String name_, specification_, libraryVersion_, family_, orientation_, configuration_, isamiVersion_;

	/**
	 * Creates material.
	 *
	 * @param id
	 *            Material ID.
	 */
	public Material(long id) {
		id_ = id;
	}

	/**
	 * Sets material name.
	 *
	 * @param name
	 *            Material name.
	 */
	public void setName(String name) {
		name_ = name;
	}

	/**
	 * Sets specification.
	 *
	 * @param specification
	 *            Specification.
	 */
	public void setSpecification(String specification) {
		specification_ = specification;
	}

	/**
	 * Sets material library version.
	 *
	 * @param libraryVersion
	 *            Material library version.
	 */
	public void setLibraryVersion(String libraryVersion) {
		libraryVersion_ = libraryVersion;
	}

	/**
	 * Sets material ISAMI version.
	 *
	 * @param isamiVersion
	 *            Material ISAMI version.
	 */
	public void setIsamiVersion(String isamiVersion) {
		isamiVersion_ = isamiVersion;
	}

	/**
	 * Sets material family.
	 *
	 * @param fanily
	 *            Material family.
	 */
	public void setFamily(String fanily) {
		family_ = fanily;
	}

	/**
	 * Sets orientation.
	 *
	 * @param orientation
	 *            Orientation.
	 */
	public void setOrientation(String orientation) {
		orientation_ = orientation;
	}

	/**
	 * Sets configuration.
	 *
	 * @param configuration
	 *            Configuration.
	 */
	public void setConfiguration(String configuration) {
		configuration_ = configuration;
	}

	/**
	 * Returns material ID.
	 *
	 * @return Material ID.
	 */
	public long getID() {
		return id_;
	}

	/**
	 * Returns material name.
	 *
	 * @return Material name.
	 */
	public String getName() {
		return name_;
	}

	/**
	 * Returns specification.
	 *
	 * @return Specification.
	 */
	public String getSpecification() {
		return specification_;
	}

	/**
	 * Returns material library version.
	 *
	 * @return Material library version.
	 */
	public String getLibraryVersion() {
		return libraryVersion_;
	}

	/**
	 * Returns material ISAMI version.
	 *
	 * @return Material ISAMI version.
	 */
	public String getIsamiVersion() {
		return isamiVersion_;
	}

	/**
	 * Returns material family.
	 *
	 * @return Material family.
	 */
	public String getFamily() {
		return family_;
	}

	/**
	 * Returns orientation.
	 *
	 * @return Orientation.
	 */
	public String getOrientation() {
		return orientation_;
	}

	/**
	 * Returns configuration.
	 *
	 * @return Configuration.
	 */
	public String getConfiguration() {
		return configuration_;
	}

	@Override
	public String toString() {
		return name_ + "/" + specification_ + "/" + orientation_ + "/" + configuration_ + "/" + libraryVersion_;
	}
}
