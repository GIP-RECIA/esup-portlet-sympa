/**
 * Copyright (C) 2010 INSA LYON http://www.insa-lyon.fr
 * Copyright (C) 2010 Esup Portail http://www.esup-portail.org
 * @Author (C) 2010 Olivier Franco <Olivier.Franco@insa-lyon.fr>
 * @Contributor (C) 2010 Doriane Dusart <Doriane.Dusart@univ-valenciennes.fr>
 * @Contributor (C) 2010 Jean-Pierre Tran <Jean-Pierre.Tran@univ-rouen.fr>
 * @Contributor (C) 2010 Vincent Bonamy <Vincent.Bonamy@univ-rouen.fr>
 * @Contributor (C) 2013 Maxime BOSSARD (GIP-RECIA) <mxbossard@gmail.com>
 *
 * Licensed under the GPL License, (please see the LICENCE file)
 */
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
