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
import org.springframework.util.CollectionUtils;

/**
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class RobotServiceImpl implements IDomainService {

	/** Svuid. */
	private static final long serialVersionUID = -5366410950001896699L;

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(DomainServiceImpl.class);

	/** Robot attached to this service. */
	private SympaRobot attachedRobot;

	/** Sympa server used. */
	private AbstractSympaServer sympaServer;

	private Map<String, AbstractSympaServer> serverList;

	/**
	 * Protected constructor.
	 * 
	 * @param pSympaServer the sympa server attached to this service
	 * @param attachedRobot the robot attached to this service
	 */
	protected RobotServiceImpl(final AbstractSympaServer pSympaServer, final SympaRobot pAttachedRobot) {
		super();
		this.sympaServer = pSympaServer;
		this.attachedRobot = pAttachedRobot;

		this.serverList = new HashMap<String, AbstractSympaServer>();
		this.serverList.put(this.sympaServer.getName(), this.sympaServer);
	}

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

	/** {@inheritDoc} */
	@Override
	public List<UserSympaListWithUrl> getWhich() {
		RobotServiceImpl.LOG.debug(String.format(
				"performing getWhich command on robot [%1$s] ...", this.attachedRobot.getDomainName()));
		List<UserSympaListWithUrl> results = this.sympaServer.getWhich(this.attachedRobot);
		results = this.fillListIfNull(results);
		this.fillListInTestEnvironment(results);
		this.sortResults(results);
		return results;
	}

	/** {@inheritDoc} */
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

		this.sortResults(filteredList);
		return filteredList;
	}

	/** {@inheritDoc} */
	@Override
	public List<UserSympaListWithUrl> getLists() {
		RobotServiceImpl.LOG.debug(String.format(
				"performing getLists command on robot [%1$s] ...", this.attachedRobot.getDomainName()));
		List<UserSympaListWithUrl> results = this.sympaServer.getLists(this.attachedRobot);
		results = this.fillListIfNull(results);
		this.fillListInTestEnvironment(results);
		this.sortResults(results);
		return results;
	}

	/** {@inheritDoc} */
	@Override
	public List<CreateListInfo> getCreateListInfo() {
		List<CreateListInfo> result = new ArrayList<CreateListInfo>();
		CreateListInfo infos = this.sympaServer.getCreateListInfo(this.attachedRobot);
		if ( infos != null ) {
			result.add(infos);
		}

		return result;
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, AbstractSympaServer> getServerList() {
		return this.serverList;
	}

	/** {@inheritDoc} */
	@Override
	public String getHomeUrl() {
		return this.sympaServer.getHomeUrl(this.attachedRobot);
	}

	/**
	 * Fill the result list if it was null.
	 * @param results
	 */
	protected List<UserSympaListWithUrl> fillListIfNull(final List<UserSympaListWithUrl> list) {
		List<UserSympaListWithUrl> result = list;

		if (result == null) {
			result = new ArrayList<UserSympaListWithUrl>(5);
		}

		return result;
	}

	/**
	 * In test environment prevent the sympa web service to return an empty list.
	 * 
	 * @param sympaList
	 */
	protected void fillListInTestEnvironment(final List<UserSympaListWithUrl> sympaList) {
		if (CollectionUtils.isEmpty(sympaList)
				&& "true".equals(System.getProperty("testEnv"))) {
			UserSympaListWithUrl testList1 = new UserSympaListWithUrl();
			testList1.setAddress("eleves400@0450822x.list.netocentre.fr");
			testList1.setSubject("Les eleves de la classe 400");
			testList1.setOwner(true);

			UserSympaListWithUrl testList4= new UserSympaListWithUrl();
			testList4.setAddress("eleves501@0450822x.list.netocentre.fr");
			testList4.setSubject("Les élèves de la classe 501");
			testList4.setSubscriber(true);

			UserSympaListWithUrl testList2 = new UserSympaListWithUrl();
			testList2.setAddress("eleves@0450822x.list.netocentre.fr ");
			testList2.setSubject("Tous les eleves de l'etablissement");
			testList2.setEditor(true);

			UserSympaListWithUrl testList3= new UserSympaListWithUrl();
			testList3.setAddress("profs605@0450822x.list.netocentre.fr ");
			testList3.setSubject("Tous les profs de la classe 605");
			testList3.setSubscriber(true);

			sympaList.add(testList1);
			sympaList.add(testList2);
			sympaList.add(testList3);
			sympaList.add(testList4);
		}
	}

	protected void sortResults(final List<UserSympaListWithUrl> toSort) {
		if (toSort != null) {
			Collections.sort(toSort, new UserSympaListComparator());
		}
	}

	protected boolean matchCriterions(final UserSympaList item, final List<SympaListCriterion> crits,final boolean matchAll) {
		if ( (item == null) || (crits == null) || (crits.size() <= 0) ) {
			return false;
		}
		DirectFieldAccessor accessor = new DirectFieldAccessor(item);
		int results = 0;
		for ( SympaListCriterion c : crits ) {
			try {
				if (accessor.isReadableProperty(c.getFieldName().name())) {
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
					RobotServiceImpl.LOG.debug("");
				}
			} catch ( Exception e) {
				RobotServiceImpl.LOG.error("exception raised while introspecting object ",e);
			}
		}
		if ( matchAll ) {
			return (results == crits.size() ) ? true : false;
		} else {
			return (results > 0 ) ? true : false;
		}
	}

	public SympaRobot getAttachedRobot() {
		return this.attachedRobot;
	}

	public AbstractSympaServer getSympaServer() {
		return this.sympaServer;
	}

}
