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

import java.util.Date;

/**
 * Class for get data queries request network message.
 *
 * @author Murat Artim
 * @date 27 Jul 2018
 * @time 20:45:38
 */
public class GetDataQueriesRequest extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Diagnostics interval dates. */
	private Date from, to;

	/**
	 * No argument constructor for serialization.
	 */
	public GetDataQueriesRequest() {
	}

	/**
	 * Returns from date.
	 *
	 * @return From date.
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * Returns to date.
	 *
	 * @return To date.
	 */
	public Date getTo() {
		return to;
	}

	/**
	 * Sets from date.
	 *
	 * @param from
	 *            From date.
	 */
	public void setFrom(Date from) {
		this.from = from;
	}

	/**
	 * Sets to date.
	 *
	 * @param to
	 *            To date.
	 */
	public void setTo(Date to) {
		this.to = to;
	}
}