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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Class for fatigue material.
 *
 * @author Murat Artim
 * @date Nov 30, 2015
 * @time 3:55:42 PM
 */
public class FatigueMaterial extends Material {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Material parameters. */
	private double p_, q_, M_;

	/**
	 * Creates fatigue material.
	 *
	 * @param id
	 *            Material ID.
	 */
	public FatigueMaterial(long id) {
		super(id);
	}

	/**
	 * Sets material parameter, p.
	 *
	 * @param p
	 *            Material parameter.
	 */
	public void setP(double p) {
		p_ = p;
	}

	/**
	 * Sets material parameter, q.
	 *
	 * @param q
	 *            Material parameter.
	 */
	public void setQ(double q) {
		q_ = q;
	}

	/**
	 * Sets material parameter, M.
	 *
	 * @param M
	 *            Material parameter.
	 */
	public void setM(double M) {
		M_ = M;
	}

	/**
	 * Returns material parameter, p.
	 *
	 * @return Material parameter, p.
	 */
	public double getP() {
		return p_;
	}

	/**
	 * Returns material parameter, q.
	 *
	 * @return Material parameter, q.
	 */
	public double getQ() {
		return q_;
	}

	/**
	 * Returns material parameter, M.
	 *
	 * @return Material parameter, M.
	 */
	public double getM() {
		return M_;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(87, 19).append(id_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FatigueMaterial))
			return false;
		if (o == this)
			return true;
		FatigueMaterial item = (FatigueMaterial) o;
		return new EqualsBuilder().append(id_, item.id_).isEquals();
	}
}
