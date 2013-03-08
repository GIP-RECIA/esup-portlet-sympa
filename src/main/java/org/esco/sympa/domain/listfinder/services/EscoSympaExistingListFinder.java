package org.esco.sympa.domain.listfinder.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.sympa.domain.model.Domain;
import org.esco.sympa.domain.model.LdapEstablishment;
import org.esco.sympa.domain.model.UAI;
import org.esco.sympa.domain.services.IEscoDomainService;
import org.esupportail.sympa.domain.listfinder.IExistingListsFinder;
import org.esupportail.sympa.domain.model.LdapPerson;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class EscoSympaExistingListFinder implements IExistingListsFinder, InitializingBean {

	private Log log = LogFactory.getLog(EscoSympaExistingListFinder.class);

	private IEscoDomainService escoDomainService;

	private LdapPerson ldapPerson;

	private LdapEstablishment ldapEstablishment;

	/** {@inheritDoc} */
	public Collection<String> findExistingLists(final Map<String, String> userInfo) {
		final String uai = userInfo.get(this.ldapPerson.getUaiAttribute());
		final String domainName = userInfo.get(this.ldapEstablishment.getDomainAttribute());

		Assert.hasText(uai, "No RNE provided in user info !");
		Assert.hasText(domainName, "No domain name provided in user info !");

		if (this.log.isDebugEnabled()) {
			this.log.debug(String.format(
					"Finding existing lists with UAI [%1$s] and domain [%2$s]",
					uai, domainName));
		}

		List<UserSympaListWithUrl> lists = this.escoDomainService.getLists(new UAI(uai), new Domain(domainName));

		this.log.debug("Total lists returned : " + lists.size());

		List<String> existingLists = new ArrayList<String>();

		for (UserSympaListWithUrl list : lists) {
			String[] address = list.getAddress().split("@");
			if ((address == null) || (address.length != 2)) {
				this.log.warn("Unexpected address, @ not found : " + list.getAddress());
				continue;
			}
			String domainPart = address[1];
			String addressPart = address[0];
			if (domainPart.equals(domainName)) {
				if (this.log.isDebugEnabled()) {
					this.log.debug(String.format(
							"Domain [%1$s] matches, adding existing list %2$s",
							domainPart, addressPart));
				}
				existingLists.add(addressPart.toLowerCase());
			} else {
				if (this.log.isDebugEnabled()) {
					this.log.debug(String.format(
							"Domain [%1$s] does not match current domain [%2$s], not adding existing list %3$s"
							,domainPart, domainName, addressPart));
				}
			}
		}
		return existingLists;

	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.escoDomainService, "No domain service injected !");
		Assert.notNull(this.ldapPerson, "No Ldap Person injected !");
		Assert.notNull(this.ldapEstablishment, "No Ldap Establishment injected !");
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setEscoDomainService(final IEscoDomainService domainService) {
		this.escoDomainService = domainService;
	}

	public IEscoDomainService getEscoDomainService() {
		return this.escoDomainService;
	}

	public LdapPerson getLdapPerson() {
		return this.ldapPerson;
	}

	public void setLdapPerson(final LdapPerson ldapPerson) {
		this.ldapPerson = ldapPerson;
	}

	public LdapEstablishment getLdapEstablishment() {
		return this.ldapEstablishment;
	}

	public void setLdapEstablishment(final LdapEstablishment ldapEstablishment) {
		this.ldapEstablishment = ldapEstablishment;
	}

}