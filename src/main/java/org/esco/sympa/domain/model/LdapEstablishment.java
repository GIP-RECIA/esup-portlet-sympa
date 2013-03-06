package org.esco.sympa.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.log4j.Logger;
import org.esco.sympa.util.LdapUtils;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.util.StringUtils;

public class LdapEstablishment {

	/** LOGGER. */
	private static final Logger LOG = Logger.getLogger(LdapEstablishment.class);

	private LdapTemplate ldapTemplate;
	private String estSearchString;
	private String estSearchFilter;

	/** Ldap domain name attribute. */
	private String domainAttribute;

	/** Ldap siren attribute. */
	private String sirenAttribute;

	private String defaultDomain;
	private String domainOverride;

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
	 * @return the domainOverride
	 */
	public String getDomainOverride() {
		return this.domainOverride;
	}

	/**
	 * @param domainOverride the domainOverride to set
	 */
	public void setDomainOverride(final String domainOverride) {
		this.domainOverride = domainOverride;
	}

	/**
	 * @return the defaultDomain
	 */
	public String getDefaultDomain() {
		return this.defaultDomain;
	}

	/**
	 * @param defaultDomain the defaultDomain to set
	 */
	public void setDefaultDomain(final String defaultDomain) {
		this.defaultDomain = defaultDomain;
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
	 * @return the domainAttribute
	 */
	public String getDomainAttribute() {
		return this.domainAttribute;
	}

	/**
	 * @param domainAttribute the domainAttribute to set
	 */
	public void setDomainAttribute(final String domainAttribute) {
		this.domainAttribute = domainAttribute;
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

	public String getMailingListDomain(final String uai) {

		if (StringUtils.hasText(this.domainOverride)) {

			String processedDomain = UAI.replaceUai(this.domainOverride, uai);

			LdapEstablishment.LOG.debug("Domain override is defined, returning [" + processedDomain + "]");

			return processedDomain;
		}

		String searchString = StringUtils.replace(this.estSearchString, "%UAI", uai);
		String searchFilter = StringUtils.replace(this.estSearchFilter, "%UAI", uai);

		LdapEstablishment.LOG.debug("Searching for mailing list domain for establishment ["
				+ uai + "] with searchString " + searchString + " and searchfilter " + searchFilter);

		AttributesMapper domainMapper = new AttributesMapper() {
			public Object mapFromAttributes(final Attributes attrs)
					throws NamingException {
				Attribute attr = attrs.get(LdapEstablishment.this.getDomainAttribute());
				Object result = null;
				if (attr != null) {
					result = attr.get();
				}
				return result;
			} };

			List<String> l = LdapUtils.ldapSearch(this.ldapTemplate, searchFilter,
					searchString, domainMapper);
			if ((l == null) || (l.size() != 1)) {
				LdapEstablishment.LOG.debug("LDAP Establishement domain search "
						+ "did not return anything");
			} else {
				String result = l.iterator().next();
				LdapEstablishment.LOG.debug("LDAP Establishement domain search returned " + result);
				return result;
			}

			return UAI.replaceUai(this.defaultDomain, uai);
	}

	public String getSiren(final String uai) {
		String result = this.defaultSiren;

		String searchString = StringUtils.replace(this.estSearchString, "%UAI", uai);
		String searchFilter = StringUtils.replace(this.estSearchFilter, "%UAI", uai);

		LdapEstablishment.LOG.debug("Searching for siren for establishment ["
				+ uai + "] with searchString " + searchString + " and searchfilter " + searchFilter);

		AttributesMapper domainMapper = new AttributesMapper() {
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