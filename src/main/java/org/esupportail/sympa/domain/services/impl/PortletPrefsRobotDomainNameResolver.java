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

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.sympa.domain.services.IRobotDomainNameResolver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.portlet.context.PortletRequestAttributes;

/**
 * Resolve the domain name of a robot from PortletRequest.
 * The robot is specified in the request as follows :
 * # The domain name of the robot is contained in Portlet Preferences.
 * # The domain name may contain variables wich will be processed by a RobotDomainNameResolver
 * 
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class PortletPrefsRobotDomainNameResolver implements IRobotDomainNameResolver, InitializingBean {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(PortletPrefsRobotDomainNameResolver.class);

	/** Portlet pref Key containing the robot domain name. */
	public static final String ROBOT_DOMAIN_NAME_PREF = "robotDomainName";

	/** Default domain name. */
	private String defaultDomainName;

	@Override
	public String resolveRobotDomainName() {
		String domainName = null;

		final PortletRequest request = this.retrievePortletRequest();
		if (request != null) {
			domainName = request.getPreferences().getValue(
					PortletPrefsRobotDomainNameResolver.ROBOT_DOMAIN_NAME_PREF, this.defaultDomainName);
		}

		if (!StringUtils.hasText(domainName) || "DEFAULT".equals(domainName)) {
			domainName = this.defaultDomainName;
		}

		return domainName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(this.defaultDomainName, "No default domain name injected !");
	}

	/**
	 * Retrieve the PortletRequest.
	 * 
	 * @return the PortletRequest
	 * @throws IllegalAccessError if PortletRequest cannot be found
	 */
	protected PortletRequest retrievePortletRequest() {
		PortletRequest request = null;

		final RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
		if ((requestAttributes != null) && (requestAttributes instanceof PortletRequestAttributes)) {
			request = ((PortletRequestAttributes) requestAttributes).getRequest();
		} else {
			final String message = "PortletRequest cannot be found in context !";
			PortletPrefsRobotDomainNameResolver.LOG.error(message);
			throw new IllegalAccessError(message);
		}

		return request;
	}

	public String getDefaultDomainName() {
		return this.defaultDomainName;
	}

	public void setDefaultDomainName(final String defaultDomainName) {
		this.defaultDomainName = defaultDomainName;
	}

}
