/**
 * 
 */
package org.esupportail.sympa.domain.listfinder.model;

import java.util.Collection;

import org.esupportail.sympa.domain.listfinder.IMailingList;

/**
 * Object containing the available list founds.
 * 
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class AvailableMailingListsFound {

	private Collection<IMailingList> creatableLists;

	private Collection<IMailingList> updatableLists;

	public Collection<IMailingList> getCreatableLists() {
		return this.creatableLists;
	}

	public void setCreatableLists(final Collection<IMailingList> creatableLists) {
		this.creatableLists = creatableLists;
	}

	public Collection<IMailingList> getUpdatableLists() {
		return this.updatableLists;
	}

	public void setUpdatableLists(final Collection<IMailingList> updatableLists) {
		this.updatableLists = updatableLists;
	}

}
