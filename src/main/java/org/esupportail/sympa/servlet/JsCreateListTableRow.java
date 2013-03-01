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
