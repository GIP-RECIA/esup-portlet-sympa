package org.esupportail.sympa.recia;

import java.util.HashMap;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.sympa.domain.services.sympa.AbstractSympaServer;
import org.esupportail.sympa.domain.services.sympa.ICredentialRetriever;
import org.esupportail.sympa.domain.services.sympa.IUserIdentityRetriever;
import org.esupportail.sympa.domain.services.sympa.SpringCachingSympaServerAxisWsImpl;
import org.springframework.beans.factory.InitializingBean;

import net.sf.ehcache.CacheManager;

public class ServerListMap extends HashMap<String,AbstractSympaServer> implements InitializingBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2957480650779043219L;

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	
	private UserInfoBean userInfoBean;
	private RobotSympaConf robotSympaConf;
	private String connectUrl;
	private ICredentialRetriever credentialRetriever;
	private IUserIdentityRetriever indentityRetriever;
	private int timeout = 5000;
	private CacheManager cacheManager ;
	private Set<String> newListForRoles;
	
	public void afterPropertiesSet() throws Exception {
		if (userInfoBean == null) {
			logger.error("userInfoBean null => serverListMap empty");
			return ;
		}
		
		if (robotSympaConf.isForAllUai()) {
			for (String uai : userInfoBean.getAllUai()) {
				creeSympaServer(uai);
			}
		} else {
			creeSympaServer(userInfoBean.getUai());
		}
	}
	
	private void creeSympaServer(String uai) throws Exception{
		if (uai != null) {	
			RobotSympaInfo rsi = robotSympaConf.getRobotSympaInfoByUai(uai, userInfoBean.getMemberOf());
			if (rsi != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("robotSympaInfo=" + rsi.toString());
				}
				
				SpringCachingSympaServerAxisWsImpl  server = new  SpringCachingSympaServerAxisWsImpl();
				
				server.setAdminUrl(rsi.adminUrl);
				server.setConnectUrl(getConnectUrl());
				server.setName(rsi.nom);
				server.setNewListUrl(rsi.newListUrl);
				server.setHomeUrl(rsi.url);
				server.setEndPointUrl(rsi.soapUrl);
				server.setTimeout(timeout);
				server.setCredentialRetriever(getCredentialRetriever());
				server.setIndentityRetriever(getIndentityRetriever());
				server.setCacheManager(getCacheManager());
				server.setNewListForRoles(getNewListForRoles());
				server.afterPropertiesSet();
				
				this.put(rsi.nom, server);
			}
		}
	}
	
	public UserInfoBean getUserInfoBean() {
		return userInfoBean;
	}
	public void setUserInfoBean(UserInfoBean userInfoBean) {
		this.userInfoBean = userInfoBean;
	}

	public RobotSympaConf getRobotSympaConf() {
		return robotSympaConf;
	}

	public void setRobotSympaConf(RobotSympaConf robotSympaConf) {
		this.robotSympaConf = robotSympaConf;
	}

	public String getConnectUrl() {
		return connectUrl;
	}

	public void setConnectUrl(String connectUrl) {
		this.connectUrl = connectUrl;
	}

	public ICredentialRetriever getCredentialRetriever() {
		return credentialRetriever;
	}

	public void setCredentialRetriever(ICredentialRetriever credentialRetriever) {
		this.credentialRetriever = credentialRetriever;
	}

	public IUserIdentityRetriever getIndentityRetriever() {
		return indentityRetriever;
	}

	public void setIndentityRetriever(IUserIdentityRetriever indentityRetriever) {
		this.indentityRetriever = indentityRetriever;
	}

	public CacheManager getCacheManager() {
		return cacheManager;
	}

	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public Set<String> getNewListForRoles() {
		return newListForRoles;
	}

	public void setNewListForRoles(Set<String> newListForRoles) {
		this.newListForRoles = newListForRoles;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	
}
