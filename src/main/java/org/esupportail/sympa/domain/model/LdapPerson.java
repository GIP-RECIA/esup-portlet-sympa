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

	private LdapTemplate ldapTemplate;

	private String uidAttribute;
	private String webmailProfileAttribute;
	private String mailAttribute;
	private String memberAttribute;
	private String uaiAttribute;
	private String domainNameAttribute;

	private String personSearchString;
	private String personSearchFilter;

	private String adminRegex;

	private Logger log = Logger.getLogger(LdapPerson.class);

	/**
	 * @return the mailAttribute
	 */
	public String getMailAttribute() {
		return this.mailAttribute;
	}

	/**
	 * @param mailAttribute the mailAttribute to set
	 */
	public void setMailAttribute(final String mailAttribute) {
		this.mailAttribute = StringUtils.trim(mailAttribute);
	}

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

	/**
	 * @return the uaiAttribute
	 */
	public String getUaiAttribute() {
		return this.uaiAttribute;
	}

	/**
	 * @param uaiAttribute the uaiAttribute to set
	 */
	public void setUaiAttribute(final String uaiAttribute) {
		this.uaiAttribute = StringUtils.trim(uaiAttribute);
	}

	public String getDomainNameAttribute() {
		return this.domainNameAttribute;
	}

	public void setDomainNameAttribute(final String domainNameAttribute) {
		this.domainNameAttribute = domainNameAttribute;
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

	public String getUidAttribute() {
		return this.uidAttribute;
	}

	public void setUidAttribute(final String uidAttribute) {
		this.uidAttribute = StringUtils.trim(uidAttribute);
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