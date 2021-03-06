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
 * Class for get fatigue missions for pilot points response network message.
 *
 * @author Murat Artim
 * @date 16 Feb 2018
 * @time 10:10:08
 */
public class GetFatigueMissionsForPilotPointsResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Fatigue missions. */
	private final ArrayList<String> missions = new ArrayList<>();

	/**
	 * No argument constructor for serialization.
	 */
	public GetFatigueMissionsForPilotPointsResponse() {
	}

	/**
	 * Returns fatigue missions.
	 *
	 * @return Fatigue missions.
	 */
	public ArrayList<String> getMissions() {
		return missions;
	}

	/**
	 * Adds given fatigue mission.
	 *
	 * @param mission
	 *            Fatigue mission.
	 */
	public void addMission(String mission) {
		missions.add(mission);
	}
}
