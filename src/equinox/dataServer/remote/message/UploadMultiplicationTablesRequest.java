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

import equinox.dataServer.remote.data.MultiplicationTableInfo;
import equinox.serverUtilities.BigMessage;

/**
 * Class for upload multiplication tables request network message.
 *
 * @author Murat Artim
 * @date 13 Mar 2018
 * @time 11:23:36
 */
public class UploadMultiplicationTablesRequest extends DatabaseQueryRequest implements BigMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Multiplication tables. */
	private final ArrayList<MultiplicationTableInfo> multTables;

	/**
	 * No argument constructor for serialization.
	 */
	public UploadMultiplicationTablesRequest() {
		multTables = new ArrayList<>();
	}

	/**
	 * Returns list of multiplication tables.
	 *
	 * @return List of multiplication tables.
	 */
	public ArrayList<MultiplicationTableInfo> getMultiplicationTables() {
		return multTables;
	}

	/**
	 * Adds given info to this message.
	 *
	 * @param info
	 *            Multiplication table info to add.
	 */
	public void addMultiplicationTableInfo(MultiplicationTableInfo info) {
		multTables.add(info);
	}

	@Override
	public boolean isReallyBig() {
		return multTables.size() > 5;
	}
}
