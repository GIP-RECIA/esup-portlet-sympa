/**
 * 
 */
package org.esupportail.sympa.domain.listFinder.model;

import org.esupportail.sympa.domain.listFinder.IMailingListModel;

/**
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class BasicMailingListModel implements IMailingListModel {

	/** id */
	private String id;
	/** nom de la liste */
	private String listname;
	/** description */
	private String description;
	/** pattern de nom de groupe */
	private String groupPatternToMatch;

	/**
	 * Constructor.
	 * 
	 * @param theId id of the model
	 * @param theListname template for the name of the lists which will be created
	 * with the model
	 * @param groupPattern
	 * @param description
	 */
	public BasicMailingListModel(final String theId, final String theListname, final String groupPattern, final String description) {
		this.id = theId;
		this.listname = theListname;
		this.groupPatternToMatch = groupPattern;
		this.description = description;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}
	/**
	 * @return the listname
	 */
	public String getListname() {
		return this.listname;
	}
	/**
	 * @return the groupPatternToMatch
	 */
	public String getGroupPatternToMatch() {
		return this.groupPatternToMatch;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * @return object toString
	 */
	@Override
	public String toString() {
		return "Model ln: " + this.listname + " desc: " + this.description + " pattern: " + this.groupPatternToMatch;
	}

}
