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
import java.util.HashMap;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class for bug report.
 *
 * @author Murat Artim
 * @date Sep 12, 2014
 * @time 4:35:38 PM
 */
public class BugReport implements Serializable {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Maximum number of characters. */
	public static final int MAX_REPORT_SIZE = 1000, MAX_LOG_SIZE = 10000, MAX_SYS_INFO_SIZE = 500, MAX_SOLUTION_SIZE = 500;

	/** Bug status. */
	public static final String OPEN = "open", CLOSED = "closed";

	/**
	 * Enumeration for bug report info.
	 *
	 * @author Murat Artim
	 * @date 15 Feb 2018
	 * @time 14:56:13
	 */
	public enum BugReportInfo {

		/** Bug report info. */
		ID("id"), OWNER("owner"), REPORT("report"), SYS_INFO("sys_info"), EVENT_LOG("event_log"), RECORDED("recorded"), STATUS("status"), SOLUTION("solution"), CLOSED_BY("closed_by");

		/** Database column name. */
		private final String column;

		/**
		 * Creates bug report info constant.
		 *
		 * @param column
		 *            Database column name.
		 */
		BugReportInfo(String column) {
			this.column = column;
		}

		/**
		 * Returns database column name.
		 *
		 * @return Database column name.
		 */
		public String getColumn() {
			return column;
		}
	}

	/** Bug report info. */
	private final HashMap<BugReportInfo, Object> info_ = new HashMap<>();

	/**
	 * No argument constructor for serialization.
	 */
	public BugReport() {
	}

	/**
	 * Returns tile of bug.
	 *
	 * @return Title of bug.
	 */
	public String getTitle() {
		return "Bug #" + (long) getInfo(BugReportInfo.ID);
	}

	/**
	 * Returns the demanded help video info.
	 *
	 * @param type
	 *            Info type.
	 * @return The demanded help video info.
	 */
	public Object getInfo(BugReportInfo type) {
		return info_.get(type);
	}

	/**
	 * Sets help video info.
	 *
	 * @param type
	 *            Info type.
	 * @param info
	 *            Info to set.
	 */
	public void setInfo(BugReportInfo type, Object info) {
		info_.put(type, info);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(info_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BugReport))
			return false;
		if (o == this)
			return true;
		BugReport info = (BugReport) o;
		return new EqualsBuilder().append(info_, info.info_).isEquals();
	}
}
