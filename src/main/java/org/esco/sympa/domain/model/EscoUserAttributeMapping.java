package org.esco.sympa.domain.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.sympa.domain.model.UserAttributeMapping;
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
public class EscoUserAttributeMapping extends UserAttributeMapping {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(EscoUserAttributeMapping.class);

	/** Ldap establishment searcher. */
	private LdapEstablishment ldapEstablishment;

	/**
	 * Add informations in user info map.
	 * 
	 * @param userInfo portal user attributes map.
	 * @return an unmodifiable enhanced map.
	 */
	public Map<String, String> enhanceUserInfo(final Map<String, String> userInfo) {
		Map<String, String> result = new HashMap<String, String>(userInfo);

		this.addSirenToUserInfo(result);

		return Collections.unmodifiableMap(result);
	}

	/**
	 * Add the siren to the portal user attributes map.
	 * 
	 * @param userInfo modifiable attribute map.
	 */
	private void addSirenToUserInfo(final Map<String, String> userInfo) {
		String uai = userInfo.get(this.getLdapPerson().getUaiAttribute());

		if (StringUtils.hasText(uai)) {
			String siren = this.ldapEstablishment.getSiren(uai);

			if (StringUtils.hasText(siren)) {
				userInfo.put(UserAttributeMapping.USER_ATTRIBUTE_SIREN_KEY, siren);
			}
		} else {
			EscoUserAttributeMapping.LOG.warn(
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

}