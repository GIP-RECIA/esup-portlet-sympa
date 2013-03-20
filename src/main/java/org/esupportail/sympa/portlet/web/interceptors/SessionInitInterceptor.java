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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;

import org.esco.sympa.util.UserInfoService;
import org.esupportail.sympa.domain.model.UserPreferences;
import org.springframework.web.portlet.handler.HandlerInterceptorAdapter;


public class SessionInitInterceptor extends HandlerInterceptorAdapter {
	private UserPreferences userPreferences;
	private String userInfoMailAttr;
	private Set<String> availableRoles;

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.handler.HandlerInterceptorAdapter#preHandle(javax.portlet.PortletRequest, javax.portlet.PortletResponse, java.lang.Object)
	 */
	@Override
	protected boolean preHandle(final PortletRequest request,
			final PortletResponse response, final Object handler) throws Exception {
		// ensure we have a portlet session
		@SuppressWarnings("unused")
		PortletSession session = request.getPortletSession(true);
		// retrieve userid
		if ( this.userPreferences.getUserId() == null ) {
			// new bean
			this.userPreferences.setUserId(request.getRemoteUser());
			Set<String> userRoles = new HashSet<String>();
			Map<String,String> userinfo = UserInfoService.getInstance().getUserInfo(request);
			String mail = userinfo.get(this.getUserInfoMailAttr());
			if ( mail != null ) {
				mail = mail.trim();
			}
			if ( mail.length() > 0 ) {
				this.userPreferences.setMail(mail);
			}
			// retrieve roles
			for ( String r : this.availableRoles ) {
				if ( request.isUserInRole(r) ) {
					userRoles.add(r);
				}
			}
			this.userPreferences.setUserRoles(userRoles);
		}

		return true;
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
	 * @return the userInfoMailAttr
	 */
	public String getUserInfoMailAttr() {
		return this.userInfoMailAttr;
	}
	/**
	 * @param userInfoMailAttr the userInfoMailAttr to set
	 */
	public void setUserInfoMailAttr(final String userInfoMailAttr) {
		this.userInfoMailAttr = userInfoMailAttr;
	}
	/**
	 * @return the availableRoles
	 */
	public Set<String> getAvailableRoles() {
		return this.availableRoles;
	}
	/**
	 * @param availableRoles the availableRoles to set
	 */
	public void setAvailableRoles(final Set<String> availableRoles) {
		this.availableRoles = availableRoles;
	}

}
