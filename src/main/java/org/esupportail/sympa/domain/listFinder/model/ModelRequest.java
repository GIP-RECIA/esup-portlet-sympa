package org.esupportail.sympa.domain.listFinder.model;

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
	@Column(name = "id_model")	
	BigInteger idModel;	
	@Column(name = "id_request")	
	BigInteger idRequest;	
}