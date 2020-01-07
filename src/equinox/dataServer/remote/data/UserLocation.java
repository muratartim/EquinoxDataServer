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

/**
 * Class for geographical user location.
 *
 * @author Murat Artim
 * @date 26 Jul 2018
 * @time 13:51:45
 */
public class UserLocation implements Serializable {

	/** Serial Id. */
	private static final long serialVersionUID = 1L;

	/** City and country name. */
	private String city, country;

	/** Latitude and longitude. */
	private float latitude, longtitude;

	/**
	 * No argument constructor for serialization.
	 */
	public UserLocation() {
	}

	/**
	 * Returns the city name.
	 * 
	 * @return The city name.
	 */
	public String getCity() {
		return city;
	}

	/**
	 * Returns the country name.
	 * 
	 * @return The country name.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Returns the latitude.
	 * 
	 * @return The latitude.
	 */
	public float getLatitude() {
		return latitude;
	}

	/**
	 * Returns the longitude.
	 * 
	 * @return The longitude.
	 */
	public float getLongtitude() {
		return longtitude;
	}

	/**
	 * Sets city name.
	 * 
	 * @param city
	 *            Name of city.
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * Sets country name.
	 * 
	 * @param country
	 *            Country name.
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * Sets latitude.
	 * 
	 * @param latitude
	 *            Latitude.
	 */
	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	/**
	 * Sets longitude.
	 * 
	 * @param longtitude
	 *            Longitude.
	 */
	public void setLongtitude(float longtitude) {
		this.longtitude = longtitude;
	}
}