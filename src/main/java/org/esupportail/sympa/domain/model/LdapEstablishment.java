package org.esupportail.sympa.domain.model;


import java.util.ArrayList;



import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.log4j.Logger;
import org.esupportail.sympa.util.LdapUtils;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.util.StringUtils;

/**
 * @author chaou
 *
 */
public class LdapEstablishment {

	/** LOGGER. */
	private static final Logger LOG = Logger.getLogger(LdapEstablishment.class);

	private LdapTemplate ldapTemplate;
	private String estSearchString;
	private String estSearchFilter;

	/** Ldap siren attribute. */
	private String sirenAttribute;

	private String defaultSiren;

	/**
	 * Default siren getter.
	 * 
	 * @return the default siren.
	 */
	public String getDefaultSiren() {
		return this.defaultSiren;
	}

	/**
	 * Default siren setter.
	 * 
	 * @param defaultSiren the default siren
	 */
	public void setDefaultSiren(final String defaultSiren) {
		this.defaultSiren = defaultSiren;
	}

	/**
	 * @return the estSearchString
	 */
	public String getEstSearchString() {
		return this.estSearchString;
	}

	/**
	 * @param estSearchString the estSearchString to set
	 */
	public void setEstSearchString(final String estSearchString) {
		this.estSearchString = estSearchString;
	}

	/**
	 * @return the estSearchFilter
	 */
	public String getEstSearchFilter() {
		return this.estSearchFilter;
	}

	/**
	 * @param estSearchFilter the estSearchFilter to set
	 */
	public void setEstSearchFilter(final String estSearchFilter) {
		this.estSearchFilter = estSearchFilter;
	}

	/**
	 * Getter.
	 * @return the ldap siren attribute.
	 */
	public String getSirenAttribute() {
		return this.sirenAttribute;
	}

	/**
	 * Setter.
	 * @param sirenAttribute the ldap siren attribute.
	 */
	public void setSirenAttribute(final String sirenAttribute) {
		this.sirenAttribute = sirenAttribute;
	}

	/**
	 * @return the ldapTemplate
	 */
	public LdapTemplate getLdapTemplate() {
		return this.ldapTemplate;
	}

	public void setLdapTemplate(final LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	public static class Person {
		private List<String> memberOf = new ArrayList<String>();
		private List<String> profile = new ArrayList<String>();


		public List<String> getProfile() {
			return this.profile;
		}

		public void setProfile(final List<String> profile) {
			this.profile = profile;
		}

		public List<String> getMemberOf() {
			return this.memberOf;
		}

		public void setMemberOf(final List<String> memberOf) {
			this.memberOf = memberOf;
		}

	}

	public String getSiren(final String uai) {
		String result = this.defaultSiren;

		String searchString = StringUtils.replace(this.estSearchString, "%UAI", uai);
		String searchFilter = StringUtils.replace(this.estSearchFilter, "%UAI", uai);

		LdapEstablishment.LOG.debug("Searching for siren for establishment ["
				+ uai + "] with searchString " + searchString + " and searchfilter " + searchFilter);

		AttributesMapper domainMapper = new AttributesMapper() {
			@Override
			public Object mapFromAttributes(final Attributes attrs)
					throws NamingException {
				Attribute attr = attrs.get(LdapEstablishment.this.getSirenAttribute());
				Object result = null;
				if (attr != null) {
					result = attr.get();
				}
				return result;
			} };

			List<String> l = LdapUtils.ldapSearch(this.ldapTemplate, searchFilter,
					searchString, domainMapper);
			if ((l == null) || (l.size() != 1)) {
				LdapEstablishment.LOG.debug("LDAP Establishement siren search "
						+ "did not return anything");
			} else {
				result = l.iterator().next();
				LdapEstablishment.LOG.debug("LDAP Establishement siren search returned " + result);
			}

			return result;
	}

}