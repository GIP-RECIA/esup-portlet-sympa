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
package org.esupportail.sympa.domain.services.impl;

import java.util.Comparator;

import org.esupportail.sympa.domain.model.UserSympaList;
import org.esupportail.sympa.domain.services.IDomainService;
import org.esupportail.sympa.domain.services.IDomainService.SympaListFields;

/**
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class UserSympaListComparator implements Comparator<UserSympaList> {

	boolean sortOrder; // true mean ascending
	SympaListFields sortOn;

	public UserSympaListComparator() {
		this.sortOrder = true;
		this.sortOn = SympaListFields.address;
	}

	public UserSympaListComparator(final SympaListFields field,final boolean order) {
		this.sortOrder = order;
		this.sortOn = field;
	}

	@Override
	public int compare(final UserSympaList o1, final UserSympaList o2) {
		int result = 0;
		switch (this.sortOn) {
		case address :
			result = this.compareString(o1.getAddress(), o2.getAddress());
			break;
		case owner :
			result = this.compareBoolean(o1.isOwner(), o2.isOwner());
			break;
		case editor:
			result = this.compareBoolean(o1.isEditor(), o2.isEditor());
			break;
		case subscriber:
			result = this.compareBoolean(o1.isSubscriber(), o2.isSubscriber());
			break;
		}
		return result;
	}
	private int compareBoolean(final boolean b1, final boolean b2) {
		int result = 0;
		if ( (b1 && b2) || (!b1 && !b2) ) {
			return 0;
		}
		if ( this.sortOrder ) {
			result = ( b1 ) ? 1 : -1;
		} else {
			result = ( b1 ) ? -1 : 1;
		}
		return result;
	}
	private int compareString(final String s1, final String s2) {
		if ( (s1 == null) || (s2 == null) ) {
			return 0;
		}
		int result = 0;
		if ( this.sortOrder ) {
			result = s1.compareTo(s2);
		} else {
			result = s2.compareTo(s1);
		}
		return result;
	}

}
