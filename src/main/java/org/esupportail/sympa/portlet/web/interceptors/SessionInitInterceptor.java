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
package org.esupportail.sympa.portlet.web.interceptors;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;

import org.esupportail.sympa.domain.model.UserPreferences;
import org.esupportail.sympa.util.UserInfoService;
import org.springframework.web.portlet.handler.HandlerInterceptorAdapter;


public class SessionInitInterceptor extends HandlerInterceptorAdapter {
	private UserPreferences userPreferences;
	private String userInfoMailAttr;
	private Set<String> availableRoles;
	
	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.handler.HandlerInterceptorAdapter#preHandle(javax.portlet.PortletRequest, javax.portlet.PortletResponse, java.lang.Object)
	 */
	@Override
	protected boolean preHandle(PortletRequest request,
			PortletResponse response, Object handler) throws Exception {
		// ensure we have a portlet session
		@SuppressWarnings("unused")
		PortletSession session = request.getPortletSession(true);
		// retrieve userid
		if ( this.userPreferences.getUserId() == null ) {
			// new bean
			this.userPreferences.setUserId(request.getRemoteUser());
			Set<String> userRoles = new HashSet<String>();
			Map<String,String> userinfo = UserInfoService.getUserInfo(request);
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
		return userPreferences;
	}
	/**
	 * @param userPreferences the userPreferences to set
	 */
	public void setUserPreferences(UserPreferences userPreferences) {
		this.userPreferences = userPreferences;
	}
	/**
	 * @return the userInfoMailAttr
	 */
	public String getUserInfoMailAttr() {
		return userInfoMailAttr;
	}
	/**
	 * @param userInfoMailAttr the userInfoMailAttr to set
	 */
	public void setUserInfoMailAttr(String userInfoMailAttr) {
		this.userInfoMailAttr = userInfoMailAttr;
	}
	/**
	 * @return the availableRoles
	 */
	public Set<String> getAvailableRoles() {
		return availableRoles;
	}
	/**
	 * @param availableRoles the availableRoles to set
	 */
	public void setAvailableRoles(Set<String> availableRoles) {
		this.availableRoles = availableRoles;
	}
	
}
