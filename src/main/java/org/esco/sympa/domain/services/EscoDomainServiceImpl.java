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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.sympa.domain.model.Domain;
import org.esco.sympa.domain.model.UAI;
import org.esco.sympa.domain.services.sympa.AbstractEscoSympaServer;
import org.esco.sympa.domain.services.sympa.EscoCachingSympaServerAxisWsImpl;
import org.esupportail.sympa.domain.model.CreateListInfo;
import org.esupportail.sympa.domain.model.UserSympaList;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.SympaListCriterion;
import org.springframework.beans.DirectFieldAccessor;


public class EscoDomainServiceImpl implements IEscoDomainService {
	private Map <String,AbstractEscoSympaServer> serverList;
	private static Log logger = LogFactory.getLog(EscoDomainServiceImpl.class);

	public List<UserSympaListWithUrl> getWhich(final UAI uai, final Domain domain) {
		// watchout; user centric ...
		Collection<AbstractEscoSympaServer> srvList = this.getServerList().values();
		List<UserSympaListWithUrl> result = new ArrayList<UserSympaListWithUrl>();
		for ( AbstractEscoSympaServer s : srvList ) {
			List<UserSympaListWithUrl> srvResult = s.getWhich(uai, domain);
			if ( (srvResult != null) && (srvResult.size() > 0) ) {
				result.addAll(srvResult);
			}
		}
		// default sort on list address
		this.sortResults(result);
		return result;
	}

	public List<UserSympaListWithUrl> getWhich(final UAI uai, final Domain domain, final List<SympaListCriterion> criterions, final boolean matchAll) {
		List<UserSympaListWithUrl> sympaList = this.getWhich(uai, domain);
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

	/* (non-Javadoc)
	 * Invalidate cache for all sympa servers
	 * @see org.esupportail.sympa.domain.services.IDomainService#invalidateCache()
	 */
	public void invalidateCache() {
		Collection<AbstractEscoSympaServer> srvList = this.getServerList().values();
		for ( AbstractEscoSympaServer s : srvList ) {
			if (s instanceof EscoCachingSympaServerAxisWsImpl) {
				EscoCachingSympaServerAxisWsImpl cachedServer =
						(EscoCachingSympaServerAxisWsImpl) s;
				cachedServer.invalidateCache();
			}

		}
	}

	public List<UserSympaListWithUrl> getLists(final UAI uai, final Domain domain) {
		Collection<AbstractEscoSympaServer> srvList = this.getServerList().values();
		List<UserSympaListWithUrl> result = new ArrayList<UserSympaListWithUrl>();
		for ( AbstractEscoSympaServer s : srvList ) {
			List<UserSympaListWithUrl> srvResult = s.getLists(uai, domain);
			if ( (srvResult != null) && (srvResult.size() > 0) ) {
				result.addAll(srvResult);
			}
		}
		// default sort on list address
		this.sortResults(result);
		return result;
	}

	public List<CreateListInfo> getCreateListInfo() {
		Collection<AbstractEscoSympaServer> srvList = this.getServerList().values();
		List<CreateListInfo> result = new ArrayList<CreateListInfo>();
		for ( AbstractEscoSympaServer s : srvList ) {
			CreateListInfo infos = s.getCreateListInfo();
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
					EscoDomainServiceImpl.logger.debug("");
				}
			} catch ( Exception e) {
				EscoDomainServiceImpl.logger.error("exception raised while introspecting object ",e);
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

	class UserSympaListComparator implements Comparator<UserSympaList> {
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
	/**
	 * @return the serverList
	 */
	public Map<String, AbstractEscoSympaServer> getServerList() {
		Map<String, AbstractEscoSympaServer> serverListToUse = new HashMap<String, AbstractEscoSympaServer>();
		for(String serverKey: this.serverList.keySet()) {
			if(this.serverList.get(serverKey).shouldBeUsed()) {
				EscoDomainServiceImpl.logger.debug("Add this server to the list for the current user : " + serverKey);
				serverListToUse.put(serverKey, this.serverList.get(serverKey));
			}
		}
		return serverListToUse;
	}

	public String getHomeUrl() {
		String homeUrl="#";
		for(String serverKey: this.serverList.keySet()) {
			if(this.serverList.get(serverKey).shouldBeUsed()) {
				homeUrl=this.serverList.get(serverKey).getHomeUrl();
			}
		}
		return homeUrl;
	}

	/**
	 * @param serverList the serverList to set
	 */
	public void setServerList(final Map<String, AbstractEscoSympaServer> serverList) {
		this.serverList = serverList;
	}



}
