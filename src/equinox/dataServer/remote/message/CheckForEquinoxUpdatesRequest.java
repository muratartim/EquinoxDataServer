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
package equinox.dataServer.remote.message;

/**
 * Class for check for Equinox updates request network message.
 *
 * @author Murat Artim
 * @date 26 Jan 2018
 * @time 10:13:38
 */
public class CheckForEquinoxUpdatesRequest extends DatabaseQueryRequest {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Operating system type and architecture of the client. */
	private String osType, osArch;

	/** Equinox version number of the client. */
	private double versionNumber;

	/**
	 * No argument constructor for serialization.
	 */
	public CheckForEquinoxUpdatesRequest() {
	}

	/**
	 * Returns the operating system type.
	 *
	 * @return Operating system type.
	 */
	public String getOsType() {
		return osType;
	}

	/**
	 * Returns the operating system architecture.
	 *
	 * @return Operating system architecture.
	 */
	public String getOsArch() {
		return osArch;
	}

	/**
	 * Returns the Equinox version number.
	 *
	 * @return Equinox version number.
	 */
	public double getVersionNumber() {
		return versionNumber;
	}

	/**
	 * Sets the operating system type.
	 *
	 * @param osType
	 *            Operating system type.
	 */
	public void setOsType(String osType) {
		this.osType = osType;
	}

	/**
	 * Sets the operating system architecture.
	 *
	 * @param osArch
	 *            Operating system architecture.
	 */
	public void setOsArch(String osArch) {
		this.osArch = osArch;
	}

	/**
	 * Sets the Equinox version number.
	 *
	 * @param versionNumber
	 *            Equinox version number.
	 */
	public void setVersionNumber(double versionNumber) {
		this.versionNumber = versionNumber;
	}
}
