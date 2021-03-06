/**
 * Licensed to ESUP-Portail under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * ESUP-Portail licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.esupportail.sympa.domain.services.sympa;

import java.util.List;

import org.esupportail.sympa.domain.model.UserSympaListWithUrl;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class CachingSympaServerAxisWsImpl extends SympaServerAxisWsImpl {

	private CacheManager cacheManager = null;
	
	private Cache cache = null;
	
	private String cacheName = CachingSympaServerAxisWsImpl.class.getName();
	
	public CachingSympaServerAxisWsImpl() {
		super();
	}
	
	protected synchronized void initCache() {
		logger.debug("creation cache with name : "+cacheName);
		if ( !cacheManager.cacheExists(cacheName) )
			cacheManager.addCache(cacheName);
		this.cache = cacheManager.getCache(cacheName);
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<UserSympaListWithUrl> getWhich() {
		// cacheKey = serverInstance/methodName/useridentifier
		String cacheKey = String.format("%1$s;%2$s;%3$s", getName(),"getWhich",getIndentityRetriever().getId());
		if ( logger.isDebugEnabled() ) logger.debug("cache key = "+cacheKey);
		Object cached = getCachedValue(cacheKey);
		if (cached != null) return (List<UserSympaListWithUrl>)cached;
		List<UserSympaListWithUrl> result = super.getWhich();
		setCachedValue(cacheKey,result);
		return result;
	}

	private void setCachedValue(String cacheKey, Object toCache) {
		cache.put(new Element(cacheKey,toCache));
	}
	private Object getCachedValue(final String cacheKey) {
		Element e = cache.get(cacheKey);
		if ( e == null ) {
			logger.debug("no cache value for key "+cacheKey);
			return null;
		}
		logger.debug("having cached value for key "+cacheKey+"=>cTime="+e.getCreationTime()+",eTime="+e.getExpirationTime());
		Object result = e.getObjectValue();
		return result;
	}
	
	/**
	 * @return the cacheManager
	 */
	public CacheManager getCacheManager() {
		return cacheManager;
	}

	/**
	 * @param cacheManager the cacheManager to set
	 */
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	/**
	 * @return the cacheName
	 */
	public String getCacheName() {
		return cacheName;
	}

	/**
	 * @param cacheName the cacheName to set
	 */
	public void setCacheName(String cacheName) {
		this.cacheName = cacheName;
	}
}
