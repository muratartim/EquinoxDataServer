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
 * Class for execute SQL statement response network message.
 *
 * @author Murat Artim
 * @date 15 Feb 2018
 * @time 11:34:36
 */
public class ExecuteSQLStatementResponse extends DataMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** True if SQL statement is executed. */
	private boolean isSqlExecuted;

	/**
	 * No argument constructor for serialization.
	 */
	public ExecuteSQLStatementResponse() {
	}

	/**
	 * Returns true if SQL statement is executed.
	 * 
	 * @return True if SQL statement is executed.
	 */
	public boolean isSqlExecuted() {
		return isSqlExecuted;
	}

	/**
	 * Sets if SQL statement is executed.
	 * 
	 * @param isSqlExecuted
	 *            True if SQL statement is executed.
	 */
	public void setSqlExecuted(boolean isSqlExecuted) {
		this.isSqlExecuted = isSqlExecuted;
	}
}
