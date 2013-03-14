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

package org.esupportail.sympa.portlet.web.interceptors;

import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.sympa.domain.model.UserPreferences;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.portlet.cas.ICASProxyTicketService;
import org.springframework.web.portlet.handler.HandlerInterceptorAdapter;


public class CasProxyInitializationService extends HandlerInterceptorAdapter {
	protected final Log logger = LogFactory.getLog(this.getClass());
	private UserPreferences userPreferences;

	private int sessionLength = 60*60*2;
	private ICASProxyTicketService proxyTicketService;

	/**
	 * this CasProxyInitializationService is used only if user in this roles (or if usedForRoles is null)
	 */
	private Set<String> usedForRoles;

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.handler.HandlerInterceptorAdapter#preHandle(javax.portlet.PortletRequest, javax.portlet.PortletResponse, java.lang.Object)
	 */
	@Override
	protected boolean preHandle(final PortletRequest request,
			final PortletResponse response, final Object handler) throws Exception {

		if(!this.shouldBeUsed()) {
			return true;
		}

		PortletSession session = request.getPortletSession();
		String ticket = this.proxyTicketService.haveProxyTicket(request);
		boolean doReceipt = false;

		if ( ticket != null ) {
			this.logger.debug("having a proxy ticket : "+ticket);
			if ( this.userPreferences.getCasReceipt() == null ) {
				this.logger.debug("first time PT; doing the work ...");
				doReceipt = true;
			} else {
				this.logger.debug("having a new ticket ? new = "+ticket+": old="+this.userPreferences.getCasPT());
				if ( !ticket.equals(this.userPreferences.getCasPT()) ) {
					this.logger.debug("tickets are differents; new receipt ...");
					doReceipt = true;
				} else {
					this.logger.debug("same ticket; nothing to be done ...");
				}
			}
		}

		// MBD: ajout pour testEnv
		if ( doReceipt && !"true".equals(System.getProperty("testEnv"))) {
			Assertion receipt = this.proxyTicketService.getProxyTicket(request);
			if ( receipt != null ) {
				this.logger.debug("having CAS receipt for : "+receipt.getPrincipal().getName());
				this.userPreferences.setCasReceipt(receipt);
				this.userPreferences.setCasPT(ticket);
				// increment session
				session.setMaxInactiveInterval(this.sessionLength);
			}
		} else if ("true".equals(System.getProperty("testEnv"))) {
			this.userPreferences.setCasReceipt(null);
			this.userPreferences.setCasPT("testEnv_PT");
		} else {
			this.logger.debug("not PT received from the portal");
		}

		return true;
	}

	/**
	 * @return true if this server should be used for the current user (depending of usedForRoles)
	 */
	public boolean shouldBeUsed() {
		if(this.getUsedForRoles() == null) {
			return true;
		}

		Set<String> userRoles = this.userPreferences.getUserRoles();
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

	/**
	 * @return the sessionLength
	 */
	public int getSessionLength() {
		return this.sessionLength;
	}

	/**
	 * @param sessionLength the sessionLength to set
	 */
	public void setSessionLength(final int sessionLength) {
		this.sessionLength = sessionLength;
	}

	/**
	 * @return the proxyTicketService
	 */
	public ICASProxyTicketService getProxyTicketService() {
		return this.proxyTicketService;
	}

	/**
	 * @param proxyTicketService the proxyTicketService to set
	 */
	public void setProxyTicketService(final ICASProxyTicketService proxyTicketService) {
		this.proxyTicketService = proxyTicketService;
	}

	/**
	 * @return the userPreferences
	 */
	public UserPreferences getUserPreferences() {
		return this.userPreferences;
	}

	/**
	 * @param userPreferences the userPreferences to set
	 */
	public void setUserPreferences(final UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
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
