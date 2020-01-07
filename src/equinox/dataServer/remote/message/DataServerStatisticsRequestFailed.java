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
 * Class for data server statistics request failed network message.
 *
 * @author Murat Artim
 * @date 18 Jun 2018
 * @time 13:39:25
 */
public class DataServerStatisticsRequestFailed extends DataMessage {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Thrown exception message. */
	private String exceptionMessage_;

	/**
	 * No argument constructor for serialization.
	 */
	public DataServerStatisticsRequestFailed() {
	}

	/**
	 * Sets thrown exception to this message.
	 *
	 * @param exception
	 *            Thrown exception of the analysis.
	 */
	public void setException(Exception exception) {
		exceptionMessage_ = exception.getMessage() + "\n";
		for (StackTraceElement ste : exception.getStackTrace()) {
			exceptionMessage_ += ste.toString() + "\n";
		}
	}

	/**
	 * Returns the thrown exception message of the share. This message also contains the stack trace.
	 *
	 * @return The thrown exception message of the share.
	 */
	public String getExceptionMessage() {
		return exceptionMessage_;
	}
}