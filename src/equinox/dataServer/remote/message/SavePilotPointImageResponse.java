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
 * Class for save pilot point image response network message.
 *
 * @author Murat Artim
 * @date 21 Feb 2018
 * @time 14:24:21
 */
public class SavePilotPointImageResponse extends DataMessage {

	/** Serial id. */
	private static final long serialVersionUID = 1L;

	/** Image URL. */
	private String imageUrl;

	/** Filer operation to be performed by the client. */
	private boolean removeFromFiler, uploadToFiler;

	/**
	 * No argument constructor for serialization.
	 */
	public SavePilotPointImageResponse() {
	}

	/**
	 * Returns image URL.
	 *
	 * @return Image URL.
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * Returns true if image data should be removed from filer.
	 *
	 * @return True if image data should be removed from filer.
	 */
	public boolean isRemoveFromFiler() {
		return removeFromFiler;
	}

	/**
	 * Returns true if image data should be uploaded to filer.
	 *
	 * @return True if image data should be uploaded to filer.
	 */
	public boolean isUploadToFiler() {
		return uploadToFiler;
	}

	/**
	 * Sets image URL.
	 *
	 * @param imageUrl
	 *            Image URL.
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * Sets if image data should be removed from filer.
	 *
	 * @param removeFromFiler
	 *            True if image data should be removed from filer.
	 */
	public void setRemoveFromFiler(boolean removeFromFiler) {
		this.removeFromFiler = removeFromFiler;
	}

	/**
	 * Sets if image data should be uploaded to filer.
	 *
	 * @param uploadToFiler
	 *            True if image data should be uploaded to filer.
	 */
	public void setUploadToFiler(boolean uploadToFiler) {
		this.uploadToFiler = uploadToFiler;
	}
}
