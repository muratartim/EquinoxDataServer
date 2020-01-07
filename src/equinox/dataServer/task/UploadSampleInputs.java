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
package equinox.dataServer.task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import equinox.dataServer.client.DataClient;
import equinox.dataServer.remote.data.SampleInputInfo;
import equinox.dataServer.remote.message.UploadSampleInputsRequest;
import equinox.dataServer.remote.message.UploadSampleInputsResponse;
import equinox.dataServer.server.DataServer;

/**
 * Class for upload sample inputs task.
 *
 * @author Murat Artim
 * @date 15 Mar 2018
 * @time 14:01:26
 */
public class UploadSampleInputs extends DatabaseQueryTask {

	/**
	 * Creates upload sample inputs task.
	 *
	 * @param server
	 *            Server instance.
	 * @param client
	 *            Requesting client.
	 * @param request
	 *            Request message.
	 */
	public UploadSampleInputs(DataServer server, DataClient client, UploadSampleInputsRequest request) {
		super(server, client, request);
	}

	@Override
	protected void runTask() throws Exception {

		// no client
		if (client_ == null)
			throw new Exception("No connected client found for uploading sample inputs.");

		// create response message
		UploadSampleInputsResponse response = new UploadSampleInputsResponse();
		response.setListenerHashCode(request_.getListenerHashCode());

		// get request message
		UploadSampleInputsRequest request = (UploadSampleInputsRequest) request_;
		ArrayList<SampleInputInfo> infos = request.getInfo();

		// get connection to database
		try (Connection connection = server_.getDCP().getConnection()) {

			try {

				// disable auto-commit
				connection.setAutoCommit(false);

				// prepare statement to delete sample inputs
				String sql = "delete from input_samples where name = ?";
				try (PreparedStatement delete = connection.prepareStatement(sql)) {

					// prepare insert statement
					sql = "insert into input_samples(name, data_size, data_url) values(?, ?, ?)";
					try (PreparedStatement insert = connection.prepareStatement(sql)) {

						// loop over info
						for (SampleInputInfo info : infos) {

							// delete sample input from database (if exists)
							delete.setString(1, info.getName());
							delete.executeUpdate();

							// insert
							insert.setString(1, info.getName());
							insert.setLong(2, info.getDataSize());
							insert.setString(3, info.getDataUrl());
							insert.executeUpdate();
						}
					}
				}

				// commit updates
				connection.commit();
				connection.setAutoCommit(true);
			}

			// exception occurred during process
			catch (Exception e) {

				// roll back updates
				if (connection != null) {
					connection.rollback();
					connection.setAutoCommit(true);
				}

				// propagate exception
				throw e;
			}
		}

		// set updated
		response.setUploaded(true);

		// respond to client
		client_.sendMessage(response);
	}
}
