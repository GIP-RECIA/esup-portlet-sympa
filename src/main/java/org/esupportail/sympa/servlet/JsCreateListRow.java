package org.esupportail.sympa.servlet;

/**
 * Class used to represent a row representing a 
 * group that can be subscribed to a list in the create list screen.
 * 
 * @author Eric Groning
 *
 */
public class JsCreateListRow {
	//Name of the group
	String name;
	
	//True if the group will be part of the mailing list
	Boolean checked;
	
	//True if the user can toggle the checked value
	Boolean editable;
	
	//Database id of the group
	String idRequest;

	/**
	 * @return the idRequest
	 */
	public String getIdRequest() {
		return idRequest;
	}

	/**
	 * @param idRequest
	 *            the idRequest to set
	 */
	public void setIdRequest(String idRequest) {
		this.idRequest = idRequest;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the checked
	 */
	public Boolean getChecked() {
		return checked;
	}

	/**
	 * @param checked
	 *            the checked to set
	 */
	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	/**
	 * @return the editable
	 */
	public Boolean getEditable() {
		return editable;
	}

	/**
	 * @param editable
	 *            the editable to set
	 */
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
}
