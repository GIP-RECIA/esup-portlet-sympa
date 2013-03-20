/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.cas;

import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.sympa.util.UserInfoService;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.TicketValidationException;
import org.jasig.cas.client.validation.TicketValidator;

public class CASProxyTicketServiceUserInfoImpl implements ICASProxyTicketService {

	protected final Log log = LogFactory.getLog(this.getClass());

	private String serviceUrl;

	public void setServiceUrl(final String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	private TicketValidator ticketValidator;

	public void setTicketValidator(final TicketValidator ticketValidator) {
		this.ticketValidator = ticketValidator;
	}

	@Override
	public String haveProxyTicket(final PortletRequest request) {
		// retrieve the CAS ticket from the UserInfo map
		Map<String,String> userinfo = UserInfoService.getInstance().getUserInfo(request);
		final String ticket = userinfo.get("casProxyTicket");

		return ticket;
	}
	/*
	 * (non-Javadoc)
	 * @see org.jasig.portlet.cas.ICASProxyTicketService#getProxyTicket(javax.portlet.PortletRequest)
	 */
	@Override
	public Assertion getProxyTicket(final PortletRequest request) {

		// retrieve the CAS ticket from the UserInfo map
		/*@SuppressWarnings("unchecked")
		Map<String,String> userinfo = (Map<String,String>) request.getAttribute(PortletRequest.USER_INFO);
		String ticket = (String) userinfo.get("casProxyTicket");*/
		String ticket = this.haveProxyTicket(request);

		if (ticket == null) {
			this.log.debug("No CAS ticket found in the UserInfo map");
			return null;
		}

		this.log.debug("serviceURL: " + this.serviceUrl + ", ticket: " + ticket);

		/* contact CAS and validate */

		try {
			Assertion assertion = this.ticketValidator.validate(ticket, this.serviceUrl);
			return assertion;
		} catch (TicketValidationException e) {
			this.log.warn("Failed to validate proxy ticket", e);
			return null;
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.jasig.portlet.cas.ICASProxyTicketService#getCasServiceToken(edu.yale.its.tp.cas.client.CASReceipt, java.lang.String)
	 */
	@Override
	public String getCasServiceToken(final Assertion assertion, final String target) {
		final String proxyTicket = assertion.getPrincipal().getProxyTicketFor(target);
		if (proxyTicket == null){
			this.log.error("Failed to retrieve proxy ticket for assertion [" + assertion.toString() + "].  Is the PGT still valid?");
			return null;
		}
		if (this.log.isTraceEnabled()) {
			this.log.trace("returning from getCasServiceToken(), returning proxy ticket ["
					+ proxyTicket + "]");
		}
		return proxyTicket;
	}

}
