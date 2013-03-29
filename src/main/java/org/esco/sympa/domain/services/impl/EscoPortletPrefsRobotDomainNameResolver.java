/**
 * 
 */
package org.esco.sympa.domain.services.impl;

import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.sympa.domain.model.UAI;
import org.esco.sympa.util.UserInfoService;
import org.esupportail.sympa.domain.model.LdapPerson;
import org.esupportail.sympa.domain.services.impl.PortletPrefsRobotDomainNameResolver;
import org.springframework.util.Assert;

/**
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class EscoPortletPrefsRobotDomainNameResolver extends PortletPrefsRobotDomainNameResolver {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(EscoPortletPrefsRobotDomainNameResolver.class);

	private LdapPerson ldapPerson;

	@Override
	public String resolveRobotDomainName() {
		String domainName = super.resolveRobotDomainName();

		final PortletRequest request = this.retrievePortletRequest();
		Map<String, String> userInfo = UserInfoService.getUserInfo(request);

		final String uai = userInfo.get(UserInfoService.getPortalUaiAttribute());

		domainName = UAI.replaceUai(domainName, uai);

		if (EscoPortletPrefsRobotDomainNameResolver.LOG.isDebugEnabled()) {
			EscoPortletPrefsRobotDomainNameResolver.LOG.debug(
					String.format("Esco enhanced robot domain name: [%1$s].", domainName));
		}

		return domainName;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		Assert.notNull(this.ldapPerson, "No LdapPerson injected !");
	}

	public LdapPerson getLdapPerson() {
		return this.ldapPerson;
	}

	public void setLdapPerson(final LdapPerson ldapPerson) {
		this.ldapPerson = ldapPerson;
	}


}
