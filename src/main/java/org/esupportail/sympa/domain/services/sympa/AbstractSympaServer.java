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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.sympa.domain.model.CreateListInfo;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;


public abstract class AbstractSympaServer {
	protected Log logger = LogFactory.getLog(getClass());
	/**
	 * name of the sympa server
	 */
	private String name;
	/**
	 * root url of the sympa server
	 */
	private String homeUrl;
	/**
	 * wrapper url (%s) will be replaced by various service url (userfull for cas)
	 */
	private String connectUrl;
	/**
	 * administrative url (%l) will be replaced by list name
	 */
	private String adminUrl;
	/**
	 * new list url
	 */
	private String newListUrl;
	/**
	 * createListInfos available on if user in this roles
	 */
	private Set<String> newListForRoles;
	/**
	 * this server is used only if user in this roles (or if usedForRoles is null)
	 */
	private Set<String> usedForRoles;
	
	
	private ICredentialRetriever credentialRetriever;
	private IUserIdentityRetriever indentityRetriever;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the homeUrl
	 */
	public String getHomeUrl() {
		return homeUrl;
	}
	/**
	 * @param homeUrl the homeUrl to set
	 */
	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}
	/**
	 * @return the connectUrl
	 */
	public String getConnectUrl() {
		return connectUrl;
	}
	/**
	 * @param connectUrl the connectUrl to set
	 */
	public void setConnectUrl(String connectUrl) {
		this.connectUrl = connectUrl;
	}
	/**
	 * @return the adminUrl
	 */
	public String getAdminUrl() {
		return adminUrl;
	}
	/**
	 * @param adminUrl the adminUrl to set
	 */
	public void setAdminUrl(String adminUrl) {
		this.adminUrl = adminUrl;
	}
	/**
	 * @return the newListUrl
	 */
	public String getNewListUrl() {
		return newListUrl;
	}
	/**
	 * @param newListUrl the newListUrl to set
	 */
	public void setNewListUrl(String newListUrl) {
		this.newListUrl = newListUrl;
	}
	
	public abstract List<UserSympaListWithUrl> getWhich();
	
	public CreateListInfo getCreateListInfo() {
		// no new list url; no createListInfo
		if ( getNewListUrl() == null ) return null;
		// having roles ?
		boolean canReturn = false;
		if ( getNewListForRoles() != null && getNewListForRoles().size() > 0 ) {
			Set<String> userRoles = getIndentityRetriever().getRoles();
			if ( userRoles != null && userRoles.size() > 0 ) {
				if ( logger.isDebugEnabled() )
					logger.debug("user have roles");
				int match = 0;
				for ( String r : userRoles ) {
					if ( logger.isDebugEnabled() )
						logger.debug("having role : "+r);
					if ( getNewListForRoles().contains(r) ) {
						match++;
						break;
					}
				}
				if ( match > 0 ) canReturn = true;
			} 
		} else {
			if ( logger.isDebugEnabled() )
				logger.debug("role set is empty; everybody allowed !");
			canReturn = true;
		}
		CreateListInfo infos = null;
		if ( canReturn ) {
			infos = new CreateListInfo();
			infos.setServerName(getName());
			infos.setAccessUrl(generateConnectUrl(getNewListUrl()));
		}
		return infos;
	}
	
	/**
	 * @return true if this server should be used for the current user (depending of usedForRoles) 
	 */
	public boolean shouldBeUsed() {
		if(getUsedForRoles() == null)
			return true;
		
		Set<String> userRoles = getIndentityRetriever().getRoles();
		if ( userRoles != null && userRoles.size() > 0 ) {
			logger.debug("user have roles");
			for (String r : userRoles) {
				logger.debug("having role : "+r);
				if (getUsedForRoles().contains(r)) {
					return true;
				}
			}
		}
		return false;
	}
	
	protected String generateListUrl(String listHomepage) {
		return generateConnectUrl(listHomepage);
	}
	protected String generateConnectUrl(String url) {
		String tmpConnectUrl = getConnectUrl();
		if ( tmpConnectUrl == null || tmpConnectUrl.trim().length() <= 0 ) return url;
		String tmpUrl = url;
		try {
			tmpUrl = URLEncoder.encode(tmpUrl,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("unable to urlencode",e);
		}
		String strTmp = tmpConnectUrl.replaceFirst("%s", tmpUrl);
		return strTmp;
	}
	protected String generateListAdminUrl(String listAddress) {
		String strListName = listAddress;
		if ( listAddress != null && listAddress.length() > 0 ) {
			int atIdx = listAddress.indexOf("@");
			if ( atIdx > 0) {
				strListName = listAddress.substring(0, atIdx);
			}
		}
		String tmpUrl = getAdminUrl();
		return generateConnectUrl(tmpUrl.replaceFirst("%l", strListName));
	}
	/**
	 * @return the credentialRetriever
	 */
	public ICredentialRetriever getCredentialRetriever() {
		return credentialRetriever;
	}
	/**
	 * @param credentialRetriever the credentialRetriever to set
	 */
	public void setCredentialRetriever(ICredentialRetriever credentialRetriever) {
		this.credentialRetriever = credentialRetriever;
	}
	/**
	 * @return the indentityRetriever
	 */
	public IUserIdentityRetriever getIndentityRetriever() {
		return indentityRetriever;
	}
	/**
	 * @param indentityRetriever the indentityRetriever to set
	 */
	public void setIndentityRetriever(IUserIdentityRetriever indentityRetriever) {
		this.indentityRetriever = indentityRetriever;
	}
	/**
	 * @return the newListForRoles
	 */
	public Set<String> getNewListForRoles() {
		return newListForRoles;
	}
	/**
	 * @param newListForRoles the newListForRoles to set
	 */
	public void setNewListForRoles(Set<String> newListForRoles) {
		this.newListForRoles = newListForRoles;
	}
	/**
	 * @return the usedForRoles
	 */
	public Set<String> getUsedForRoles() {
		return usedForRoles;
	}
	/**
	 * @param usedForRoles the usedForRoles to set
	 */
	public void setUsedForRoles(Set<String> usedForRoles) {
		this.usedForRoles = usedForRoles;
	}
	
}
