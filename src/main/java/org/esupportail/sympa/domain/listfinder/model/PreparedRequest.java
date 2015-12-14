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
 * Object representing row in prepared_request table in the SympaRemote database. 
 * @author Eric Groning
 *
 */
@Entity
@Table(name = "prepared_request")
public class PreparedRequest {
	@Id
	@Column(name = "id_request")
	BigInteger id;
	
	@Column(name = "display_name")	
	String displayName;
	
	@Column(name = "ldapfilter")	
	String ldapFilter;

	@Column(name = "data_source")	
	String dataSource;
	
	@Column(name = "ldap_suffix")	
	String ldapSuffix;

	/**
	 * @return the id
	 */	
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
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the ldapFilter
	 */
	public String getLdapFilter() {
		return ldapFilter;
	}

	/**
	 * @param ldapFilter the ldapFilter to set
	 */
	public void setLdapFilter(String ldapFilter) {
		this.ldapFilter = ldapFilter;
	}

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public String getLdapSuffix() {
		return ldapSuffix;
	}

	public void setLdapSuffix(String ldapSuffix) {
		this.ldapSuffix = ldapSuffix;
	}
}
