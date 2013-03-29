package org.esco.sympa.domain.listfinder.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.sympa.domain.model.LdapEstablishment;
import org.esco.sympa.util.UserInfoService;
import org.esupportail.sympa.domain.listfinder.IExistingListsFinder;
import org.esupportail.sympa.domain.model.LdapPerson;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.IDomainService;
import org.esupportail.sympa.domain.services.IRobotDomainNameResolver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class EscoSympaExistingListFinder implements IExistingListsFinder, InitializingBean {

	private Log log = LogFactory.getLog(EscoSympaExistingListFinder.class);

	private IDomainService domainService;

	private LdapPerson ldapPerson;

	private LdapEstablishment ldapEstablishment;

	private IRobotDomainNameResolver robotDomainNameResolver;

	/** {@inheritDoc} */
	@Override
	public Collection<String> findExistingLists(final Map<String, String> userInfo) {
		final String uai = userInfo.get(UserInfoService.getPortalUaiAttribute());
		final String domainName = this.robotDomainNameResolver.resolveRobotDomainName();
		Assert.hasText(uai, "No RNE provided in user info !");
		Assert.hasText(domainName, "Unable to resolve the domain name !");

		List<UserSympaListWithUrl> lists = this.domainService.getLists();

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

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.domainService, "No domain service injected !");
		Assert.notNull(this.ldapPerson, "No Ldap Person injected !");
		Assert.notNull(this.ldapEstablishment, "No Ldap Establishment injected !");
		Assert.notNull(this.robotDomainNameResolver, "No IRobotDomainNameResolver injected !");
	}

	public IDomainService getDomainService() {
		return this.domainService;
	}

	public void setDomainService(final IDomainService domainService) {
		this.domainService = domainService;
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

	public IRobotDomainNameResolver getRobotDomainNameResolver() {
		return this.robotDomainNameResolver;
	}

	public void setRobotDomainNameResolver(final IRobotDomainNameResolver listeDomainNameResolver) {
		this.robotDomainNameResolver = listeDomainNameResolver;
	}

}