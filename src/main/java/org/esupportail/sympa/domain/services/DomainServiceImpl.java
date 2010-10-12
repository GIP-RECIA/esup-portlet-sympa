/**
 * Copyright (C) 2010 INSA LYON http://www.insa-lyon.fr
 * Copyright (C) 2010 Esup Portail http://www.esup-portail.org
 * @Author (C) 2010 Olivier Franco <Olivier.Franco@insa-lyon.fr>
 * @Contributor (C) 2010 Doriane Dusart <Doriane.Dusart@univ-valenciennes.fr>
 * @Contributor (C) 2010 Vincent Bonamy <Vincent.Bonamy@univ-rouen.fr>
 *
 * Licensed under the GPL License, (please see the LICENCE file)
 */

package org.esupportail.sympa.domain.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.sympa.domain.model.CreateListInfo;
import org.esupportail.sympa.domain.model.UserSympaList;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.sympa.AbstractSympaServer;
import org.springframework.beans.DirectFieldAccessor;


public class DomainServiceImpl implements IDomainService {
	private Map <String,AbstractSympaServer> serverList;
	private static Log logger = LogFactory.getLog(DomainServiceImpl.class);
	
	public List<UserSympaListWithUrl> getWhich() {
		// watchout; user centric ...
		// TODO : show if user in role to view server
		Collection<AbstractSympaServer> srvList = serverList.values();
		List<UserSympaListWithUrl> result = new ArrayList<UserSympaListWithUrl>();
		for ( AbstractSympaServer s : srvList ) {
			List<UserSympaListWithUrl> srvResult = s.getWhich();
			if ( srvResult != null && srvResult.size() > 0 ) {
				result.addAll(srvResult);
			}
		}
		// default sort on list address
		sortResults(result);
		return result;
	}

	public List<UserSympaListWithUrl> getWhich(List<SympaListCriterion> criterions, boolean matchAll) {
		List<UserSympaListWithUrl> sympaList = getWhich();
		if ( criterions == null || criterions.size() <= 0 ) return sympaList;
		List<UserSympaListWithUrl> filteredList = new ArrayList<UserSympaListWithUrl>();
		for ( UserSympaListWithUrl item : sympaList ) {
			if ( matchCriterions(item, criterions, matchAll) ) {
				filteredList.add(item);
			}
		}
		return filteredList;
	}
	

	public List<CreateListInfo> getCreateListInfo() {
		Collection<AbstractSympaServer> srvList = serverList.values();
		List<CreateListInfo> result = new ArrayList<CreateListInfo>();
		for ( AbstractSympaServer s : srvList ) {
			CreateListInfo infos = s.getCreateListInfo();
			if ( infos != null )
				result.add(infos);
		}
		return result;
	}

	private boolean matchCriterions(UserSympaList item, List<SympaListCriterion> crits,boolean matchAll) {
		if ( item == null || crits == null || crits.size() <= 0 ) return false;
		DirectFieldAccessor accessor = new DirectFieldAccessor(item);
		int results = 0;
		for ( SympaListCriterion c : crits ) {
			try {
				if ( accessor.isReadableProperty(c.getFieldName().name()) ) {
					Object o = accessor.getPropertyValue(c.getFieldName().name());
					if ( o == null ) {
						// case compare to null object 
						if ( c.getCompareTo() == null ) results++;
					} else {
						if ( o.equals(c.getCompareTo()) ) results++;
					}
				} else {
					logger.debug("");
				}
			} catch ( Exception e) {
				logger.error("exception raised while introspecting object ",e);
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
	private void sortResults(List<UserSympaListWithUrl> toSort) {
		Collections.sort(toSort, new UserSympaListComparator());
	}
	
	class UserSympaListComparator implements Comparator<UserSympaList> {
		boolean sortOrder; // true mean ascending
		SympaListFields sortOn;
		public UserSympaListComparator() {
			this.sortOrder = true;
			this.sortOn = SympaListFields.address;
		}
		public UserSympaListComparator(SympaListFields field,boolean order) {
			this.sortOrder = order;
			this.sortOn = field;
		}

		public int compare(UserSympaList o1, UserSympaList o2) {
			int result = 0;
			switch (sortOn) {
			case address :
				result = compareString(o1.getAddress(), o2.getAddress());
				break;
			case owner :
				result = compareBoolean(o1.isOwner(), o2.isOwner());
				break;
			case editor:
				result = compareBoolean(o1.isEditor(), o2.isEditor());
				break;
			case subscriber:
				result = compareBoolean(o1.isSubscriber(), o2.isSubscriber());
				break;
			}
			return result;
		}
		private int compareBoolean(boolean b1, boolean b2) {
			int result = 0;
			if ( (b1 && b2) || (!b1 && !b2) ) return 0;
			if ( sortOrder ) {
				result = ( b1 ) ? 1 : -1;
			} else {
				result = ( b1 ) ? -1 : 1; 
			}
			return result;
		}
		private int compareString(String s1, String s2) {
			if ( s1 == null || s2 == null ) return 0;
			int result = 0;
			if ( sortOrder ) {
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
	public Map<String, AbstractSympaServer> getServerList() {
		return serverList;
	}

	/**
	 * @param serverList the serverList to set
	 */
	public void setServerList(Map<String, AbstractSympaServer> serverList) {
		this.serverList = serverList;
	}

	

}
