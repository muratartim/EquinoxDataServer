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
 * Class for get aircraft sections for pilot points request network message.
 *
 * @author Murat Artim
 * @date 15 Feb 2018
 * @time 12:24:58
 */
public class GetAircraftSectionsForPilotPointsRequest extends DatabaseQueryRequest {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Constant for all aircraft sections. */
	public static final String ALL_SECTIONS = "All aircraft sections";

	/** Aircraft program. */
	private String program;

	/**
	 * No argument constructor for serialization.
	 */
	public GetAircraftSectionsForPilotPointsRequest() {
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
