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
package org.esupportail.sympa.servlet;


/**
 * Represents a row in the table of lists that can be created
 * @author Eric Groning
 *
 */
public class JsCreateListTableRow {
	//Name of the list
	String name;
	
	//Subject of list
	String subject;
	
	//Model id
	String modelId;
	
	//Name of the model parameter 
	String modelParam;
	
	public String toString() {
		return "Name: " + name + " Subject: " + subject + " Model Id: " + modelId + " Model Param: " + modelParam;
	}
	
	/**
	 * @return the modelParam
	 */
	public String getModelParam() {
		return modelParam;
	}
	/**
	 * @param modelParam the modelParam to set
	 */
	public void setModelParam(String modelParam) {
		this.modelParam = modelParam;
	}
	/**
	 * @return the modelId
	 */
	public String getModelId() {
		return modelId;
	}
	/**
	 * @param modelId the modelId to set
	 */
	public void setModelId(String modelId) {
		this.modelId = modelId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
}
