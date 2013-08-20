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
package org.esupportail.sympa.domain.services;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.esupportail.sympa.domain.model.CreateListInfo;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.impl.SympaListCriterion;
import org.esupportail.sympa.domain.services.sympa.AbstractSympaServer;


public abstract interface IDomainService extends Serializable {
	public enum SympaListFields {
		address,
		owner,
		editor,
		subscriber
	};

	/**
	 * When the application layer needs to tell implementors of this interface that any results that
	 * may have been cached are now invalid.  This method should be called after data on the sympa servers
	 * could have been changed, such as after creation of a list for example.
	 */
	public void invalidateCache();

	public List<UserSympaListWithUrl> getWhich();
	public List<UserSympaListWithUrl> getWhich(List<SympaListCriterion>criterion,boolean mathAll);
	public List<UserSympaListWithUrl> getLists();
	public List<CreateListInfo> getCreateListInfo();
	public Map<String, AbstractSympaServer> getServerList();
	public String getHomeUrl();
}
