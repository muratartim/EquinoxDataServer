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
 * Class for get aircraft sections for spectra request network message.
 *
 * @author Murat Artim
 * @date 15 Feb 2018
 * @time 12:39:18
 */
public class GetAircraftSectionsForSpectraRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Aircraft program. */
	private String program;

	/**
	 * No argument constructor for serialization.
	 */
	public GetAircraftSectionsForSpectraRequest() {
	}

	/**
	 * Returns the aircraft program.
	 *
	 * @return Aircraft program.
	 */
	public String getProgram() {
		return program;
	}

	/**
	 * Sets the aircraft program.
	 *
	 * @param program
	 *            Aircraft program.
	 */
	public void setProgram(String program) {
		this.program = program;
	}
}
