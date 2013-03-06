package org.esupportail.sympa.domain.listfinder.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Object representing row in model_subscribers table in the SympaRemote database. 
 * @author Eric Groning
 *
 */
@Entity
@Table(name = "model_subscribers")
public class ModelSubscribers {

	private BigInteger id;
	private String groupFilter;

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "id")
	public BigInteger getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(BigInteger id) {
		this.id = id;
	}

	/**
	 * @return the groupFilter
	 */
	@Column(name = "group_filter")
	public String getGroupFilter() {
		return groupFilter;
	}

	/**
	 * @param groupFilter
	 *            the groupFilter to set
	 */
	public void setGroupFilter(String groupFilter) {
		this.groupFilter = groupFilter;
	}

}
