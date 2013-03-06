package org.esupportail.sympa.domain.listfinder.model;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Object representing row in model table in the SympaRemote database. 
 * @author Eric Groning
 *
 */
@Entity
@Table(name = "model")
public class Model {

	@Id
	@Column(name = "id", insertable=false, updatable=false)
	private BigInteger id;
	@Column(name = "modelname")
	private String modelName;
	@Column(name = "listname")
	private String listname;
	@Column(name = "description")
	private String description;
	@Column(name = "subject")
	private String subject;
	@Column(name = "family")
	private String family;
	@Column(name = "need_parameter")	
	private Boolean needParameter;
	@Column(name = "pattern")
	private String pattern;

	
	

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
	 * @return the modelName
	 */
	
	public String getModelName() {
		return modelName;
	}

	/**
	 * @param modelName the modelName to set
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	/**
	 * @return the listname
	 */
	
	public String getListname() {
		return listname;
	}

	/**
	 * @param listname the listname to set
	 */
	public void setListname(String listname) {
		this.listname = listname;
	}

	/**
	 * @return the description
	 */
	
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the subject
	 */
	
	public String getSubject() {
		return subject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * @return the family
	 */
	
	public String getFamily() {
		return family;
	}

	/**
	 * @param family the family to set
	 */
	public void setFamily(String family) {
		this.family = family;
	}

	/**
	 * @return the needParameter
	 */
	public Boolean getNeedParameter() {
		return needParameter;
	}

	/**
	 * @param needParameter the needParameter to set
	 */
	public void setNeedParameter(Boolean needParameter) {
		this.needParameter = needParameter;
	}

	/**
	 * @return the pattern
	 */
	
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern the pattern to set
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	

}
