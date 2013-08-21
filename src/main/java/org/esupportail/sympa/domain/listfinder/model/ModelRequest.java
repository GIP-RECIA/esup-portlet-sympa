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

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Object representing row in j_model_request table in the SympaRemote database. 
 * @author Eric Groning
 *
 */
@Entity
@Table(name = "j_model_request")
public class ModelRequest {
	
	@EmbeddedId
	ModelRequestId id;
	String category;
	
	public enum ModelRequestRequired {
    	MANDATORY,
    	UNCHECKED,
    	CHECKED
    }
	
	
	
	/**
	 * @return the id
	 */
	public ModelRequestId getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(ModelRequestId id) {
		this.id = id;
	}
	/**
	 * @return the category
	 */
	@Column(name = "category", columnDefinition="enum('MANDATORY','UNCHECKED','CHECKED')")
	public String getCategory() {
		return category;
	}
	
	public ModelRequestRequired getCategoryAsEnum() {
		return ModelRequestRequired.valueOf(category);
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}
	
	/**
	 * @return the idModel
	 */		
	public BigInteger getIdModel() {
		return id.idModel;
	}
	/**
	 * @param idModel the idModel to set
	 */
	public void setIdModel(BigInteger idModel) {
		id.idModel = idModel;
	}
	/**
	 * @return the idRequest
	 */
	public BigInteger getIdRequest() {
		return id.idRequest;		       
	}
	/**
	 * @param idRequest the idRequest to set
	 */
	public void setIdRequest(BigInteger idRequest) {
		this.id.idRequest = idRequest;
	}
	
	
	
}

@Embeddable
class ModelRequestId implements Serializable {
	
	/** Svuid. */
	private static final long serialVersionUID = 2561811354459813795L;
	
	@Column(name = "id_model")	
	BigInteger idModel;	
	@Column(name = "id_request")	
	BigInteger idRequest;	
}