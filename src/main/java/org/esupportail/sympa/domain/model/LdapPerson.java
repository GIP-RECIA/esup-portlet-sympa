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
package org.esupportail.sympa.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;

public class LdapPerson {

	private Logger log = Logger.getLogger(LdapPerson.class);

	private LdapTemplate ldapTemplate;

	private String webmailProfileAttribute;
	private String memberAttribute;

	private String personSearchString;
	private String personSearchFilter;

	private String adminRegex;

	/**
	 * @return the adminRegex
	 */
	public String getAdminRegex() {
		return this.adminRegex;
	}

	/**
	 * @param adminRegex the adminRegex to set
	 */
	public void setAdminRegex(final String adminRegex) {
		this.adminRegex = adminRegex;
	}

	public String getPersonSearchString() {
		return this.personSearchString;
	}

	public void setPersonSearchString(final String personSearchString) {
		this.personSearchString = StringUtils.trim(personSearchString);
	}

	public String getPersonSearchFilter() {
		return this.personSearchFilter;
	}

	public void setPersonSearchFilter(final String personSearchFilter) {
		this.personSearchFilter = StringUtils.trim(personSearchFilter);
	}

	public String getMemberAttribute() {
		return this.memberAttribute;
	}

	public void setMemberAttribute(final String memberAttribute) {
		this.memberAttribute = StringUtils.trim(memberAttribute);
	}

	public String getWebmailProfileAttribute() {
		return this.webmailProfileAttribute;
	}

	public void setWebmailProfileAttribute(final String webmailProfileAttribute) {
		this.webmailProfileAttribute = StringUtils.trim(webmailProfileAttribute);
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

	@SuppressWarnings("unchecked")
	public LdapPerson.Person getPerson(final String uid) {

		String searchString = this.personSearchString.replace("%UID", uid);

		try {
			List<LdapPerson.Person> l = this.ldapTemplate.search(
					searchString, this.personSearchFilter,
					new AttributesMapper() {
						@Override
						public Object mapFromAttributes(final Attributes attrs)
								throws NamingException {
							Person p = new Person();
							NamingEnumeration<?> o = attrs.get(LdapPerson.this.getMemberAttribute())
									.getAll();
							while (o.hasMoreElements()) {
								p.getMemberOf().add(o.next().toString());
							}

							Attribute profiles = attrs.get(LdapPerson.this.getWebmailProfileAttribute());
							if (profiles != null) {
								o = profiles.getAll();
								if (o!=null) {
									while (o.hasMoreElements()) {
										p.getProfile().add(o.next().toString());
									}
								}

							}

							return p;
						}
					});
			return l.size() == 1 ? l.get(0) : null;

		} catch (Exception ex) {
			this.log.error("Error during ldap requesting !", ex);
			return null;
		}
	}

}