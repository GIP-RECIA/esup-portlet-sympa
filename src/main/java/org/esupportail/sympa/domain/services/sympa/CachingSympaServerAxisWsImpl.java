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
package org.esupportail.sympa.domain.services.sympa;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.impl.SympaRobot;
import org.springframework.util.CollectionUtils;

public class CachingSympaServerAxisWsImpl extends SympaServerAxisWsImpl {

	/** Svuid. */
	private static final long serialVersionUID = 8300731178095561612L;

	private CacheManager cacheManager = null;

	private Cache cache = null;

	private String cacheName = CachingSympaServerAxisWsImpl.class.getName();

	public CachingSympaServerAxisWsImpl() {
		super();
	}

	protected synchronized void initCache() {
		this.logger.debug("creation cache with name : "+this.cacheName);
		if ( !this.cacheManager.cacheExists(this.cacheName) ) {
			this.cacheManager.addCache(this.cacheName);
		}
		this.cache = this.cacheManager.getCache(this.cacheName);
	}

	public void invalidateCache() {
		this.logger.debug("Invalidating cache");
		this.cache.removeAll();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserSympaListWithUrl> getWhich(final SympaRobot robot) {
		// cacheKey = serverInstance/methodName/useridentifier
		String cacheKey = String.format("%1$s;%2$s;%3$s;%4$s", robot.getDomainName(), this.getName(),"getWhich",this.getIndentityRetriever().getId());
		if ( this.logger.isDebugEnabled() ) {
			this.logger.debug("cache key = "+cacheKey);
		}
		Object cached = this.getCachedValue(cacheKey);
		if (cached != null) {
			return (List<UserSympaListWithUrl>)cached;
		}
		List<UserSympaListWithUrl> result = super.getWhich(robot);
		if (!CollectionUtils.isEmpty(result)) {
			this.setCachedValue(cacheKey, result);
		}
		return result;
	}

	private void setCachedValue(final String cacheKey, final Object toCache) {
		if ( this.logger.isDebugEnabled() ) {
			this.logger.debug(String.format("Caching element: [%1$s] for key: [%2$s].", toCache.toString(), cacheKey));
		}

		this.cache.put(new Element(cacheKey, toCache));
	}

	private Object getCachedValue(final String cacheKey) {
		Element e = this.cache.get(cacheKey);
		if ( e == null ) {
			this.logger.debug("no cache value for key "+cacheKey);
			return null;
		}
		this.logger.debug("having cached value for key "+cacheKey+"=>cTime="+e.getCreationTime()+",eTime="+e.getExpirationTime());
		Object result = e.getObjectValue();
		return result;
	}

	/**
	 * @return the cacheManager
	 */
	public CacheManager getCacheManager() {
		return this.cacheManager;
	}

	/**
	 * @param cacheManager the cacheManager to set
	 */
	public void setCacheManager(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * @return the cacheName
	 */
	public String getCacheName() {
		return this.cacheName;
	}

	/**
	 * @param cacheName the cacheName to set
	 */
	public void setCacheName(final String cacheName) {
		this.cacheName = cacheName;
	}
}
