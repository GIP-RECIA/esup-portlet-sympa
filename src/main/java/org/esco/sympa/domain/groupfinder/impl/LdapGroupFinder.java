package org.esco.sympa.domain.groupfinder.impl;

import java.util.Collection;
import java.util.Map;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.esco.sympa.domain.groupfinder.IEtabGroupsFinder;
import org.esco.sympa.util.LdapUtils;
import org.esupportail.sympa.domain.model.UserAttributeMapping;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class LdapGroupFinder implements IEtabGroupsFinder {

	/** Logger. */
	private static final Logger LOG = Logger.getLogger(LdapGroupFinder.class);

	/** configured via spring with connection info and baseDN.*/
	private LdapTemplate ldapTemplate;

	/** for example, (ou=groups);. */
	private String ldapSearchFilter;

	private String ldapSearchBaseDN;

	/** for example, cn. */
	private String ldapGroupAttribute;

	/** Prefix to add to all groups. */
	private String groupPrefix;

	/** User attributes mapping. */
	private UserAttributeMapping userAttributeMapping;

	public Collection<String> findGroupsOfEtab(final Map<String, String> userInfo) {

		String searchFilter = this.getUserAttributeMapping()
				.substitutePlaceholder(this.ldapSearchFilter, userInfo);
		String searchString = this.getUserAttributeMapping()
				.substitutePlaceholder(this.ldapSearchBaseDN, userInfo);

		LdapGroupFinder.LOG.debug("Searching for ldap groups for user attributes ["
				+ userInfo.toString() + "] with searchString " + searchString
				+ " and searchfilter " + searchFilter);

		AttributesMapper groupsMapper = new AttributesMapper() {
			public Object mapFromAttributes(final Attributes attrs) throws NamingException {
				Attribute groupNameAttr = attrs.get(LdapGroupFinder.this.getLdapGroupAttribute());

				StringBuilder groupName = new StringBuilder();
				if (groupNameAttr != null) {
					if (!StringUtils.isEmpty(LdapGroupFinder.this.groupPrefix)) {
						groupName.append(LdapGroupFinder.this.groupPrefix);
					}
					groupName.append(groupNameAttr.get());
				}
				return groupName.toString();
			}
		};

		return LdapUtils.ldapSearch(this.ldapTemplate, searchFilter, searchString, groupsMapper);
	}


	public String getLdapSearchBaseDN() {
		return this.ldapSearchBaseDN;
	}

	public void setLdapSearchBaseDN(final String ldapSearchBaseDN) {
		this.ldapSearchBaseDN = ldapSearchBaseDN;
	}

	public String getLdapGroupAttribute() {
		return this.ldapGroupAttribute;
	}

	public void setLdapGroupAttribute(final String ldapGroupAttribute) {
		this.ldapGroupAttribute = ldapGroupAttribute;
	}

	public LdapTemplate getLdapTemplate() {
		return this.ldapTemplate;
	}

	public void setLdapTemplate(final LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	public String getLdapSearchFilter() {
		return this.ldapSearchFilter;
	}

	public void setLdapSearchFilter(final String ldapSearchFilter) {
		this.ldapSearchFilter = ldapSearchFilter;
	}

	public String getGroupPrefix() {
		return this.groupPrefix;
	}

	public void setGroupPrefix(final String groupPrefix) {
		this.groupPrefix = groupPrefix;
	}

	public UserAttributeMapping getUserAttributeMapping() {
		return this.userAttributeMapping;
	}

	public void setUserAttributeMapping(final UserAttributeMapping userAttributeMapping) {
		this.userAttributeMapping = userAttributeMapping;
	}

}
