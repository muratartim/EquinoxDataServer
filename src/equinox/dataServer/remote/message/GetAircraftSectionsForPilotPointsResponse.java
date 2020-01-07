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

import java.util.ArrayList;

/**
 * Class for get aircraft sections for pilot points response network message.
 *
 * @author Murat Artim
 * @date 15 Feb 2018
 * @time 12:28:35
 */
public class GetAircraftSectionsForPilotPointsResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Aircraft sections. */
	private final ArrayList<String> sections = new ArrayList<>();

	/**
	 * No argument constructor for serialization.
	 */
	public GetAircraftSectionsForPilotPointsResponse() {
	}

	/**
	 * Returns aircraft sections.
	 *
	 * @return Aircraft sections.
	 */
	public ArrayList<String> getSections() {
		return sections;
	}

	/**
	 * Adds given section.
	 *
	 * @param section
	 *            Section to add.
	 */
	public void addSection(String section) {
		sections.add(section);
	}
}
