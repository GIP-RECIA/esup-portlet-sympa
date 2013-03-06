package org.esupportail.sympa.domain.listfinder.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.sympa.domain.listfinder.IExistingListsFinder;
import org.esupportail.sympa.domain.model.LdapPerson;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.IDomainService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Existing list finder implementation responsible for querying Sympa to find
 * the list already existing.
 * 
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class SympaExistingListFinder implements IExistingListsFinder, InitializingBean {

	private Log log = LogFactory.getLog(SympaExistingListFinder.class);

	private IDomainService domainService;

	private LdapPerson ldapPerson;

	/** {@inheritDoc} */
	public Collection<String> findExistingLists(final Map<String, String> userInfo) {
		this.log.debug("Finding existing lists...");

		List<UserSympaListWithUrl> lists = this.domainService.getLists();

		this.log.debug("Total lists returned : " + lists.size());

		List<String> existingLists = new ArrayList<String>();

		for (UserSympaListWithUrl list : lists) {
			String[] address = list.getAddress().split("@");
			if ((address == null) || (address.length != 2)) {
				this.log.warn("Unexpected address, @ not found : " + list.getAddress());
				continue;
			}

			final String namePart = address[0];
			@SuppressWarnings("unused")
			final String domainPart = address[1];

			if (this.log.isDebugEnabled()) {
				this.log.debug(String.format(
						"Adding existing list %1$s ...", namePart));
			}
			existingLists.add(namePart.toLowerCase());

		}
		return existingLists;

	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.domainService, "No domain service injected !");
		Assert.notNull(this.ldapPerson, "No Ldap Person injected !");
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final IDomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * 
	 * @param ldapPerson the LDAP person
	 */
	public void setLdapPerson(final LdapPerson ldapPerson) {
		this.ldapPerson = ldapPerson;
	}

}