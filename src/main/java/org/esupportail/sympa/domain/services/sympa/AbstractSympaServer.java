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
	protected Log logger = LogFactory.getLog(this.getClass());
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
	 * archives url (%l) will be replaced by list name
	 */
	private String archivesUrl;
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
		return this.name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(final String name) {
		this.name = name;
	}
	/**
	 * @return the homeUrl
	 */
	public String getHomeUrl() {
		return this.homeUrl;
	}
	/**
	 * @param homeUrl the homeUrl to set
	 */
	public void setHomeUrl(final String homeUrl) {
		this.homeUrl = homeUrl;
	}
	/**
	 * @return the connectUrl
	 */
	public String getConnectUrl() {
		return this.connectUrl;
	}
	/**
	 * @param connectUrl the connectUrl to set
	 */
	public void setConnectUrl(final String connectUrl) {
		this.connectUrl = connectUrl;
	}
	/**
	 * @return the adminUrl
	 */
	public String getAdminUrl() {
		return this.adminUrl;
	}
	/**
	 * @param adminUrl the adminUrl to set
	 */
	public void setAdminUrl(final String adminUrl) {
		this.adminUrl = adminUrl;
	}
	/**
	 * @return the archivesUrl
	 */
	public String getArchivesUrl() {
		return this.archivesUrl;
	}
	/**
	 * @param archivesUrl the archivesUrl to set
	 */
	public void setArchivesUrl(final String archivesUrl) {
		this.archivesUrl = archivesUrl;
	}
	/**
	 * @return the newListUrl
	 */
	public String getNewListUrl() {
		return this.newListUrl;
	}
	/**
	 * @param newListUrl the newListUrl to set
	 */
	public void setNewListUrl(final String newListUrl) {
		this.newListUrl = newListUrl;
	}

	public abstract List<UserSympaListWithUrl> getWhich();

	public abstract List<UserSympaListWithUrl> getLists();

	public CreateListInfo getCreateListInfo() {
		// no new list url; no createListInfo
		if ( this.getNewListUrl() == null ) {
			return null;
		}
		// having roles ?
		boolean canReturn = false;
		if ( (this.getNewListForRoles() != null) && (this.getNewListForRoles().size() > 0) ) {
			Set<String> userRoles = this.getIndentityRetriever().getRoles();
			if ( (userRoles != null) && (userRoles.size() > 0) ) {
				if ( this.logger.isDebugEnabled() ) {
					this.logger.debug("user have roles");
				}
				int match = 0;
				for ( String r : userRoles ) {
					if ( this.logger.isDebugEnabled() ) {
						this.logger.debug("having role : "+r);
					}
					if ( this.getNewListForRoles().contains(r) ) {
						match++;
						break;
					}
				}
				if ( match > 0 ) {
					canReturn = true;
				}
			}
		} else {
			if ( this.logger.isDebugEnabled() ) {
				this.logger.debug("role set is empty; everybody allowed !");
			}
			canReturn = true;
		}
		CreateListInfo infos = null;
		if ( canReturn ) {
			infos = new CreateListInfo();
			infos.setServerName(this.getName());
			infos.setAccessUrl(this.generateConnectUrl(this.getNewListUrl()));
		}
		return infos;
	}

	/**
	 * @return true if this server should be used for the current user (depending of usedForRoles)
	 */
	public boolean shouldBeUsed() {
		if(this.getUsedForRoles() == null) {
			return true;
		}

		Set<String> userRoles = this.getIndentityRetriever().getRoles();
		if ( (userRoles != null) && (userRoles.size() > 0) ) {
			this.logger.debug("user have roles");
			for (String r : userRoles) {
				this.logger.debug("having role : "+r);
				if (this.getUsedForRoles().contains(r)) {
					return true;
				}
			}
		}
		return false;
	}

	protected String generateListUrl(final String listHomepage) {
		return this.generateConnectUrl(listHomepage);
	}
	protected String generateConnectUrl(final String url) {
		String tmpConnectUrl = this.getConnectUrl();
		if ( (tmpConnectUrl == null) || (tmpConnectUrl.trim().length() <= 0) ) {
			return url;
		}
		String tmpUrl = url;
		try {
			tmpUrl = URLEncoder.encode(tmpUrl,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			this.logger.error("unable to urlencode",e);
		}
		String strTmp = tmpConnectUrl.replaceFirst("%s", tmpUrl);
		return strTmp;
	}
	protected String generateListAdminUrl(final String listAddress) {
		String strListName = listAddress;
		if ( (listAddress != null) && (listAddress.length() > 0) ) {
			int atIdx = listAddress.indexOf("@");
			if ( atIdx > 0) {
				strListName = listAddress.substring(0, atIdx);
			}
		}
		String tmpUrl = this.getAdminUrl();
		return this.generateConnectUrl(tmpUrl.replaceFirst("%l", strListName));
	}

	protected String generateListArchivesUrl(final String listAddress) {
		String strListName = listAddress;
		if ( (listAddress != null) && (listAddress.length() > 0) ) {
			int atIdx = listAddress.indexOf("@");
			if ( atIdx > 0) {
				strListName = listAddress.substring(0, atIdx);
			}
		}
		String tmpUrl = this.getArchivesUrl();
		return this.generateConnectUrl(tmpUrl.replaceFirst("%l", strListName));
	}
	/**
	 * @return the credentialRetriever
	 */
	public ICredentialRetriever getCredentialRetriever() {
		return this.credentialRetriever;
	}
	/**
	 * @param credentialRetriever the credentialRetriever to set
	 */
	public void setCredentialRetriever(final ICredentialRetriever credentialRetriever) {
		this.credentialRetriever = credentialRetriever;
	}
	/**
	 * @return the indentityRetriever
	 */
	public IUserIdentityRetriever getIndentityRetriever() {
		return this.indentityRetriever;
	}
	/**
	 * @param indentityRetriever the indentityRetriever to set
	 */
	public void setIndentityRetriever(final IUserIdentityRetriever indentityRetriever) {
		this.indentityRetriever = indentityRetriever;
	}
	/**
	 * @return the newListForRoles
	 */
	public Set<String> getNewListForRoles() {
		return this.newListForRoles;
	}
	/**
	 * @param newListForRoles the newListForRoles to set
	 */
	public void setNewListForRoles(final Set<String> newListForRoles) {
		this.newListForRoles = newListForRoles;
	}
	/**
	 * @return the usedForRoles
	 */
	public Set<String> getUsedForRoles() {
		return this.usedForRoles;
	}
	/**
	 * @param usedForRoles the usedForRoles to set
	 */
	public void setUsedForRoles(final Set<String> usedForRoles) {
		this.usedForRoles = usedForRoles;
	}

}
