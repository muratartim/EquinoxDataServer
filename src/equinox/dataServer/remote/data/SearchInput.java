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
 * Abstract class for search input.
 *
 * @author Murat Artim
 * @date 18 Jan 2017
 * @time 16:53:56
 */
public abstract class SearchInput implements Serializable {

	/** Serial ID. */
	private static final long serialVersionUID = 1L;

	/** Order results by criteria. */
	public static final String NAME = "Name", PROGRAM = "Aircraft program", SECTION = "Aircraft section", MISSION = "Fatigue mission", DELIVERY = "Delivery reference";

	/** Maximum number of results. */
	private int maxHits_ = 100;

	/** Options. */
	private boolean isAndOperator_ = true, isIgnoreCase_ = true, isAscending_ = true;

	/** Order results by criteria. */
	private String orderBy_ = NAME;

	/**
	 * No argument constructor for serialization.
	 */
	public SearchInput() {
	}

	/**
	 * Returns order results by criteria.
	 *
	 * @return Order results by criteria.
	 */
	public String getOrderByCriteria() {
		return orderBy_;
	}

	/**
	 * Returns maximum number of results.
	 *
	 * @return Maximum number of results.
	 */
	public int getMaxHits() {
		return maxHits_;
	}

	/**
	 * Returns true if the logical operator is 'AND'. False if it is 'OR'.
	 *
	 * @return True if the logical operator is 'AND'. False if it is 'OR'.
	 */
	public boolean getOperator() {
		return isAndOperator_;
	}

	/**
	 * Returns true if string search case is ignored.
	 *
	 * @return True if string search case is ignored.
	 */
	public boolean getCase() {
		return isIgnoreCase_;
	}

	/**
	 * Returns true if results ordering is ascending.
	 *
	 * @return True if results ordering is ascending.
	 */
	public boolean getOrder() {
		return isAscending_;
	}

	/**
	 * Sets maximum number of results.
	 *
	 * @param maxHits
	 *            Maximum number of results.
	 */
	public void setMaxHits(int maxHits) {
		maxHits_ = maxHits;
	}

	/**
	 * Sets order results by criteria.
	 *
	 * @param orderBy
	 *            Order results by criteria.
	 */
	public void setOrderByCriteria(String orderBy) {
		orderBy_ = orderBy;
	}

	/**
	 * Sets logical operator.
	 *
	 * @param isAndOperator
	 *            True if the logical operator is 'AND'. False if it is 'OR'.
	 */
	public void setOperator(boolean isAndOperator) {
		isAndOperator_ = isAndOperator;
	}

	/**
	 * Sets string search case.
	 *
	 * @param isIgnoreCase
	 *            True if string search case is ignored.
	 */
	public void setCase(boolean isIgnoreCase) {
		isIgnoreCase_ = isIgnoreCase;
	}

	/**
	 * Sets results ordering.
	 *
	 * @param isAscending
	 *            True if ascending order.
	 */
	public void setOrder(boolean isAscending) {
		isAscending_ = isAscending;
	}
}
