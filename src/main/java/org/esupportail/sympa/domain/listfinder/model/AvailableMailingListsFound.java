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
