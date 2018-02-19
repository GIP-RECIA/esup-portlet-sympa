package org.esupportail.sympa.domain.model;


import java.util.Collections;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.utils.Assert;
import org.esupportail.sympa.domain.model.UserAttributeMapping;
import org.esupportail.sympa.util.UserInfoService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * PortletUserAttributeMapping describe the mapping between the portlet users attributes and
 * the placeholders used in the configuration.
 * For example, the establishment identifier %UAI to replace correspond to the portal user attribute ESCOUAICourant.
 * The portal user attributes are defined in the portlet.xml file.
 * 
 * @author GIP - RECIA Maxime BOSSARD 2012
 *
 */
public class UserAttribute extends UserAttributeMapping implements InitializingBean {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(UserAttribute.class);

	/** Ldap establishment searcher. */
	private LdapEstablishment ldapEstablishment;

	/** Ldap Person searcher. */
	private LdapPerson ldapPerson;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.ldapEstablishment, "No LdapEstablishment injected !");
		Assert.notNull(this.ldapPerson, "No LdapPerson injected !");
	}

	/**
	 * Add informations in user info map.
	 * 
	 * @param userInfo portal user attributes map.
	 * @return an unmodifiable enhanced map.
	 */
	public Map<String, String> enhanceUserInfo(final Map<String, String> userInfo) {
		LOG.debug("Add informations in user info map");
		Map<String, String> result = new HashMap<String, String>(userInfo);

	//	this.addSirenToUserInfo(result);
		LOG.debug("End to add informations in user info map");
		return Collections.unmodifiableMap(result);
	}

	/**
	 * Add the siren to the portal user attributes map.
	 * 
	 * @param userInfo modifiable attribute map.
	 */
	private void addSirenToUserInfo(final Map<String, String> userInfo) {
		String uai = userInfo.get(UserInfoService.getPortalUaiAttribute());

		if (StringUtils.hasText(uai)) {
			String siren = this.ldapEstablishment.getSiren(uai);

			if (StringUtils.hasText(siren)) {
				userInfo.put(UserAttributeMapping.USER_ATTRIBUTE_SIREN_KEY, siren);
			}
		} else {
			UserAttribute.LOG.warn(
					"No UAI attribute found in portal context !");
		}

	}

	/**
	 * Mapping setter.
	 * 
	 * @param ldapEstablishment the ldapEstablishment
	 */
	public void setLdapEstablishment(final LdapEstablishment ldapEstablishment) {
		this.ldapEstablishment = ldapEstablishment;
	}

	public LdapEstablishment getLdapEstablishment() {
		return this.ldapEstablishment;
	}

	public LdapPerson getLdapPerson() {
		return this.ldapPerson;
	}

	public void setLdapPerson(final LdapPerson ldapPerson) {
		this.ldapPerson = ldapPerson;
	}

}
