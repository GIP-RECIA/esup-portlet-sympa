package org.esco.sympa.domain.groupfinder.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.esco.sympa.util.LdapUtils;
import org.springframework.ldap.core.AttributesMapper;

/**
 * Search in LDAP for Groups in multi-valued attribute.
 * 
 * @author GIP - RECIA Maxime BOSSARD
 *
 */
public class MultiValuedAttributeGroupFinder extends LdapGroupFinder {

	/** Logger. */
	private static final Logger LOG = Logger.getLogger(MultiValuedAttributeGroupFinder.class);

	/** Constructor. */
	public MultiValuedAttributeGroupFinder() {
		super();
	}

	@Override
	public Collection<String> findGroupsOfEtab(final Map<String, String> userInfo) {
		String searchFilter = this.getUserAttributeMapping()
				.substitutePlaceholder(this.getLdapSearchFilter(), userInfo);
		String searchString = this.getUserAttributeMapping()
				.substitutePlaceholder(this.getLdapSearchBaseDN(), userInfo);

		AttributesMapper groupsMapper = new AttributesMapper() {
			@SuppressWarnings("unchecked")
			public Object mapFromAttributes(final Attributes attrs) throws NamingException {
				Attribute groupNameAttr = attrs.get(MultiValuedAttributeGroupFinder.this.getLdapGroupAttribute());

				Collection<String> groups = new HashSet<String>();
				if (groupNameAttr != null) {
					NamingEnumeration<String> values =
							(NamingEnumeration<String>) groupNameAttr.getAll();
					while (values.hasMoreElements()) {
						groups.add(values.nextElement());
					}
				}

				return groups;
			}
		};

		Collection<Collection<String>> groupsList =
				LdapUtils.ldapSearch(this.getLdapTemplate(), searchFilter, searchString, groupsMapper);

		// Merge of all Sets and add the prefix on all groups
		Collection<String> groups = new HashSet<String>();
		StringBuilder groupName = new StringBuilder(128);
		for (Collection<String> groupSet : groupsList) {
			for (String group : groupSet) {
				groupName.setLength(0);
				if (!StringUtils.isEmpty(this.getGroupPrefix())) {
					groupName.append(this.getGroupPrefix());
				}
				groupName.append(group);
				groups.add(groupName.toString());
			}
		}

		if (MultiValuedAttributeGroupFinder.LOG.isInfoEnabled()) {
			if ((groups == null) || (groups.size() == 0)) {
				MultiValuedAttributeGroupFinder.LOG.info(String.format(
						"No multi-valued attribute groups found for filter: [%s]", this.getLdapSearchFilter()));
			} else {
				MultiValuedAttributeGroupFinder.LOG.info(String.format(
						"[%s] Multi-valued attribute groups found for filter: [%s]", groups.size(), this.getLdapSearchFilter()));
			}
		}

		if (MultiValuedAttributeGroupFinder.LOG.isDebugEnabled()) {
			if ((groups != null) && (groups.size() > 0)) {
				for (String group : groups) {
					MultiValuedAttributeGroupFinder.LOG.debug("Multi-valued attribute group: " + group);
				}
			}
		}

		return groups;
	}

}
