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
 * Class for Linear material.
 *
 * @author Murat Artim
 * @date Dec 7, 2015
 * @time 12:45:58 PM
 */
public class LinearMaterial extends Material {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Material parameters. */
	private double ceff_, m_, a_, b_, c_, ftu_, fty_;

	/**
	 * Creates Linear material.
	 *
	 * @param id
	 *            Material ID.
	 */
	public LinearMaterial(long id) {
		super(id);
	}

	/**
	 * Sets material parameter, Ceff.
	 *
	 * @param ceff
	 *            Material parameter.
	 */
	public void setCeff(double ceff) {
		ceff_ = ceff;
	}

	/**
	 * Sets material parameter, m.
	 *
	 * @param m
	 *            Material parameter.
	 */
	public void setM(double m) {
		m_ = m;
	}

	/**
	 * Sets material parameter, A.
	 *
	 * @param a
	 *            Material parameter.
	 */
	public void setA(double a) {
		a_ = a;
	}

	/**
	 * Sets material parameter, B.
	 *
	 * @param b
	 *            Material parameter.
	 */
	public void setB(double b) {
		b_ = b;
	}

	/**
	 * Sets material parameter, C.
	 *
	 * @param c
	 *            Material parameter.
	 */
	public void setC(double c) {
		c_ = c;
	}

	/**
	 * Sets ultimate stress value.
	 *
	 * @param ftu
	 *            Ultimate stress.
	 */
	public void setFtu(double ftu) {
		ftu_ = ftu;
	}

	/**
	 * Sets yield stress value.
	 *
	 * @param fty
	 *            Yield stress.
	 */
	public void setFty(double fty) {
		fty_ = fty;
	}

	/**
	 * Returns material parameter, Ceff.
	 *
	 * @return Material parameter, Ceff.
	 */
	public double getCeff() {
		return ceff_;
	}

	/**
	 * Returns material parameter, m.
	 *
	 * @return Material parameter, m.
	 */
	public double getM() {
		return m_;
	}

	/**
	 * Returns material parameter, A.
	 *
	 * @return Material parameter, A.
	 */
	public double getA() {
		return a_;
	}

	/**
	 * Returns material parameter, B.
	 *
	 * @return Material parameter, B.
	 */
	public double getB() {
		return b_;
	}

	/**
	 * Returns material parameter, C.
	 *
	 * @return Material parameter, C.
	 */
	public double getC() {
		return c_;
	}

	/**
	 * Returns ultimate stress value.
	 *
	 * @return Ultimate stress value.
	 */
	public double getFtu() {
		return ftu_;
	}

	/**
	 * Returns yield stress value.
	 *
	 * @return Yield stress value.
	 */
	public double getFty() {
		return fty_;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(11, 69).append(id_).toHashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof LinearMaterial))
			return false;
		if (o == this)
			return true;
		LinearMaterial item = (LinearMaterial) o;
		return new EqualsBuilder().append(id_, item.id_).isEquals();
	}
}
