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
 * Class for download pilot points request network message.
 *
 * @author Murat Artim
 * @date 11 Feb 2018
 * @time 22:45:57
 */
public class DownloadPilotPointsRequest extends DatabaseQueryRequest {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** Download IDs. */
	private final ArrayList<Long> downloadIds = new ArrayList<>();

	/**
	 * No argument constructor for serialization.
	 */
	public DownloadPilotPointsRequest() {
	}

	/**
	 * Adds given download ID.
	 * 
	 * @param id
	 *            Download ID.
	 */
	public void addDownloadId(long id) {
		downloadIds.add(id);
	}

	/**
	 * Returns download ids.
	 * 
	 * @return Download ids.
	 */
	public ArrayList<Long> getDownloadIds() {
		return downloadIds;
	}
}
