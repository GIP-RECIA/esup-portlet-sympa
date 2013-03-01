package org.esupportail.sympa.domain.listFinder.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.sympa.domain.model.Domain;
import org.esco.sympa.domain.model.UAI;
import org.esupportail.sympa.domain.listFinder.IExistingListsFinder;
import org.esupportail.sympa.domain.model.LdapPerson;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.IDomainService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

public class SympaExistingListFinder implements IExistingListsFinder, InitializingBean {

	private Log log = LogFactory.getLog(SympaExistingListFinder.class);

	private IDomainService domainService;

	private LdapPerson ldapPerson;

	/** {@inheritDoc} */
	public Collection<String> findExistingLists(final Map<String, String> userInfo) {

		//Fetch bean in order to have the ldap attribute config and to query for isMemberOf
		LdapPerson ldapPerson = (LdapPerson) this.applicationContext.getBean("ldapPerson");
		String rne = userInfo.get(ldapPerson.getUidAttribute());
		this.log.debug("Finding existing lists with UID [" + rne + "] and domain [" + domain + "]");

		List<UserSympaListWithUrl> lists = this.domainService.getLists(new UAI(rne), new Domain(domain));

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
			if (domainPart.equals(domain)) {
				this.log.debug("Domain [" + domainPart + "] matches, adding existing list " + addressPart);
				existingLists.add(addressPart.toLowerCase());
			} else {
				this.log.debug("Domain [" + domainPart + "] does not match current domain [" + domain + "], not adding existing list " + addressPart);
			}
		}
		return existingLists;

	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.domainService, "No domain service injected !");
		Assert.notNull(this.ldapPerson, "No Ldap Person injected !");
	}

	/**
	 * @return the domainService
	 */
	public IDomainService getDomainService() {
		return this.domainService;
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final IDomainService domainService) {
		this.domainService = domainService;
	}

}