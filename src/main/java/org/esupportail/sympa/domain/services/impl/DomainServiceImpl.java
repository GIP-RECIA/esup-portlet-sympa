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
package org.esupportail.sympa.domain.services.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.sympa.domain.model.CreateListInfo;
import org.esupportail.sympa.domain.model.UserSympaList;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.IDomainService;
import org.esupportail.sympa.domain.services.sympa.AbstractSympaServer;
import org.esupportail.sympa.domain.services.sympa.CachingSympaServerAxisWsImpl;
import org.springframework.beans.DirectFieldAccessor;


public class DomainServiceImpl implements IDomainService {
	private Map <String,AbstractSympaServer> serverList;
	private static Log logger = LogFactory.getLog(DomainServiceImpl.class);

	/** {@inheritDoc} */
	@Override
	public void invalidateCache() {
		Collection<AbstractSympaServer> srvList = this.getServerList().values();
		for ( AbstractSympaServer s : srvList ) {
			if (s instanceof CachingSympaServerAxisWsImpl) {
				CachingSympaServerAxisWsImpl cachedServer =
						(CachingSympaServerAxisWsImpl) s;
				cachedServer.invalidateCache();
			}

		}
	}

	@Override
	public List<UserSympaListWithUrl> getWhich() {
		// watchout; user centric ...
		Collection<AbstractSympaServer> srvList = this.getServerList().values();
		List<UserSympaListWithUrl> result = new ArrayList<UserSympaListWithUrl>();
		for ( AbstractSympaServer s : srvList ) {
			List<UserSympaListWithUrl> srvResult = s.getWhich(SympaRobot.getDefaultRobot());
			if ( (srvResult != null) && (srvResult.size() > 0) ) {
				result.addAll(srvResult);
			}
		}
		// default sort on list address
		this.sortResults(result);
		return result;
	}

	@Override
	public List<UserSympaListWithUrl> getWhich(final List<SympaListCriterion> criterions, final boolean matchAll) {
		List<UserSympaListWithUrl> sympaList = this.getWhich();
		if ( (criterions == null) || (criterions.size() <= 0) ) {
			return sympaList;
		}
		List<UserSympaListWithUrl> filteredList = new ArrayList<UserSympaListWithUrl>();
		for ( UserSympaListWithUrl item : sympaList ) {
			if ( this.matchCriterions(item, criterions, matchAll) ) {
				filteredList.add(item);
			}
		}
		return filteredList;
	}

	@Override
	public List<UserSympaListWithUrl> getLists() {
		Collection<AbstractSympaServer> srvList = this.getServerList().values();
		List<UserSympaListWithUrl> result = new ArrayList<UserSympaListWithUrl>();
		for ( AbstractSympaServer s : srvList ) {
			List<UserSympaListWithUrl> srvResult = s.getLists(SympaRobot.getDefaultRobot());
			if ( (srvResult != null) && (srvResult.size() > 0) ) {
				result.addAll(srvResult);
			}
		}
		// default sort on list address
		this.sortResults(result);
		return result;
	}

	@Override
	public List<CreateListInfo> getCreateListInfo() {
		Collection<AbstractSympaServer> srvList = this.getServerList().values();
		List<CreateListInfo> result = new ArrayList<CreateListInfo>();
		for ( AbstractSympaServer s : srvList ) {
			CreateListInfo infos = s.getCreateListInfo(SympaRobot.getDefaultRobot());
			if ( infos != null ) {
				result.add(infos);
			}
		}
		return result;
	}

	private boolean matchCriterions(final UserSympaList item, final List<SympaListCriterion> crits,final boolean matchAll) {
		if ( (item == null) || (crits == null) || (crits.size() <= 0) ) {
			return false;
		}
		DirectFieldAccessor accessor = new DirectFieldAccessor(item);
		int results = 0;
		for ( SympaListCriterion c : crits ) {
			try {
				if ( accessor.isReadableProperty(c.getFieldName().name()) ) {
					Object o = accessor.getPropertyValue(c.getFieldName().name());
					if ( o == null ) {
						// case compare to null object
						if ( c.getCompareTo() == null ) {
							results++;
						}
					} else {
						if ( o.equals(c.getCompareTo()) ) {
							results++;
						}
					}
				} else {
					DomainServiceImpl.logger.debug("");
				}
			} catch ( Exception e) {
				DomainServiceImpl.logger.error("exception raised while introspecting object ",e);
			}
		}
		if ( matchAll ) {
			return (results == crits.size() ) ? true : false;
		} else {
			return (results > 0 ) ? true : false;
		}
	}
	//protected boolean have
	// sorting
	private void sortResults(final List<UserSympaListWithUrl> toSort) {
		Collections.sort(toSort, new UserSympaListComparator());
	}

	/**
	 * @return the serverList
	 */
	@Override
	public Map<String, AbstractSympaServer> getServerList() {
		Map<String, AbstractSympaServer> serverListToUse = new HashMap<String, AbstractSympaServer>();
		for(String serverKey: this.serverList.keySet()) {
			if(this.serverList.get(serverKey).shouldBeUsed()) {
				DomainServiceImpl.logger.debug("Add this server to the list for the current user : " + serverKey);
				serverListToUse.put(serverKey, this.serverList.get(serverKey));
			}
		}
		return serverListToUse;
	}

	@Override
	public String getHomeUrl() {
		String homeUrl="#";
		for(String serverKey: this.serverList.keySet()) {
			if(this.serverList.get(serverKey).shouldBeUsed()) {
				homeUrl=this.serverList.get(serverKey).getHomeUrl(SympaRobot.getDefaultRobot());
			}
		}
		return homeUrl;
	}

	/**
	 * @param serverList the serverList to set
	 */
	public void setServerList(final Map<String, AbstractSympaServer> serverList) {
		this.serverList = serverList;
	}



}
