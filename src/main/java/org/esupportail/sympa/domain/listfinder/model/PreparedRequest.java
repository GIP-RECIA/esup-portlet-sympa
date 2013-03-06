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

}
