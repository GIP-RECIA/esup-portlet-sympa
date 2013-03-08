package org.esco.sympa.util;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

/**
 * LDAP requesting utils class.
 * 
 * @author GIP - RECIA 2012 Maxime BOSSARD
 *
 */
public class LdapUtils {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(LdapUtils.class);

	/** Hidden constructor. */
	private LdapUtils() {

	}

	/**
	 * Search in LDAP.
	 * 
	 * @param ldapTemplate the template to search into.
	 * @param searchFilter the search filter.
	 * @param searchString the search base.
	 * @param mapper the Attributes mapper to return data.
	 * @return the search result.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T ldapSearch(final LdapTemplate ldapTemplate, final String searchFilter,
			final String searchString, final AttributesMapper mapper) {
		try {
			List<String> l = ldapTemplate.search(searchString, searchFilter, mapper);

			LdapUtils.LOG.debug(StringUtils.join(new Object[]{"Number of item found", l.size()}, " "));
			return (T) l;

		} catch (Exception ex) {
			LdapUtils.LOG.error("Error during LDAP search !", ex);
			return null;
		}
	}

}
