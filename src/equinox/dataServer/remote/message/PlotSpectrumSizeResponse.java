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

import org.jfree.data.category.CategoryDataset;

/**
 * Class for spectrum count response network message.
 * 
 * @author Murat Artim
 * @date 20 Feb 2018
 * @time 12:00:24
 */
public class PlotSpectrumSizeResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Plot dataset. */
	private CategoryDataset dataset;

	/**
	 * No argument constructor for serialization.
	 */
	public PlotSpectrumSizeResponse() {
	}

	/**
	 * Returns plot dataset.
	 *
	 * @return Plot dataset.
	 */
	public CategoryDataset getDataset() {
		return dataset;
	}

	/**
	 * Sets plot dataset.
	 *
	 * @param dataset
	 *            Plot dataset.
	 */
	public void setDataset(CategoryDataset dataset) {
		this.dataset = dataset;
	}
}
