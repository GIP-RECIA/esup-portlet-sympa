/**
 * Copyright (C) 2010 INSA LYON http://www.insa-lyon.fr
 * Copyright (C) 2010 Esup Portail http://www.esup-portail.org
 * @Author (C) 2010 Olivier Franco <Olivier.Franco@insa-lyon.fr>
 * @Contributor (C) 2010 Doriane Dusart <Doriane.Dusart@univ-valenciennes.fr>
 * @Contributor (C) 2010 Jean-Pierre Tran <Jean-Pierre.Tran@univ-rouen.fr>
 * @Contributor (C) 2010 Vincent Bonamy <Vincent.Bonamy@univ-rouen.fr>
 *
 * Licensed under the GPL License, (please see the LICENCE file)
 */

package org.esco.sympa.domain.services;

import java.util.List;
import java.util.Map;

import org.esco.sympa.domain.model.Domain;
import org.esco.sympa.domain.model.UAI;
import org.esco.sympa.domain.services.sympa.AbstractEscoSympaServer;
import org.esupportail.sympa.domain.model.CreateListInfo;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.impl.SympaListCriterion;


public abstract interface IEscoDomainService {
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

	/**
	 * 
	 * @param domain The email domain associated with the establishment.  Potentially used to create the url to connect to the sympa server
	 * @param uai The establishment id.
	 * @return
	 */
	public List<UserSympaListWithUrl> getWhich(UAI uai, Domain domain);
	/**
	 * @param domain The email domain associated with the establishment.  Potentially used to create the url to connect to the sympa server
	 * @param uai The establishment id.
	 * @param criterion
	 * @param mathAll
	 * @return
	 */
	public List<UserSympaListWithUrl> getWhich(UAI uai, Domain domain, List<SympaListCriterion>criterion,boolean mathAll);
	/**
	 * @param domain The email domain associated with the establishment.  Potentially used to create the url to connect to the sympa server
	 * @param uai The establishment id.
	 * @return
	 */
	public List<UserSympaListWithUrl> getLists(UAI uai, Domain domain);

	public List<CreateListInfo> getCreateListInfo();
	public Map<String, AbstractEscoSympaServer> getServerList();
	public String getHomeUrl();
}
