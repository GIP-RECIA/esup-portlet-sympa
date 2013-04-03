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
/**
 * 
 */
package org.esupportail.sympa.domain.listfinder.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.esupportail.sympa.domain.listfinder.IAvailableListsFinder;
import org.esupportail.sympa.domain.listfinder.IExistingListsFinder;
import org.esupportail.sympa.domain.listfinder.IMailingList;
import org.esupportail.sympa.domain.listfinder.IMailingListModel;
import org.esupportail.sympa.domain.listfinder.model.AvailableMailingListsFound;
import org.esupportail.sympa.domain.listfinder.model.BasicMailingList;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Basic implementation of available list finder.
 * It build a collection of mailing list based on a collection of string configured.
 * 
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class BasicAvailableListFinder implements IAvailableListsFinder, InitializingBean {

	/** Lists already existing in Sympa. */
	private IExistingListsFinder existingListFinder;

	/** Strings representing a basic description of some creatable lists. */
	private Collection<String> creatableLists;

	/** {@inheritDoc} */
	@Override
	public AvailableMailingListsFound getAvailableAndNonExistingLists(final Map<String, String> userInfo,
			final Collection<IMailingListModel> modeles) {
		AvailableMailingListsFound availableLists = new AvailableMailingListsFound();

		// Create an empty Collection of Mailing Lists
		// => all the lists that could be created by the admin
		Collection<IMailingList> creatableLists = new TreeSet<IMailingList>();
		Collection<IMailingList> updatableLists = new TreeSet<IMailingList>();
		availableLists.setCreatableLists(creatableLists);
		availableLists.setUpdatableLists(updatableLists);

		if (!CollectionUtils.isEmpty(modeles)) {
			for (final IMailingListModel modele : modeles) {
				creatableLists.addAll(this.buildMailingListCollection(modele, this.creatableLists));
			}
		}

		// Remove existing lists
		final Collection<String> existingLists = this.existingListFinder.findExistingLists(userInfo);
		if (!CollectionUtils.isEmpty(existingLists)) {
			final Iterator<IMailingList> avListIt = creatableLists.iterator();
			while (avListIt.hasNext()) {
				// Remove all already created lists
				IMailingList creatableList = avListIt.next();

				if (existingLists.contains(creatableList.getName())) {
					avListIt.remove();
					updatableLists.add(creatableList);
				}
			}
		}

		return availableLists;
	}

	/** {@inheritDoc} */
	@Override
	public void setExistingListsFinder(final IExistingListsFinder existingListFinder) {
		this.existingListFinder = existingListFinder;
	}

	/** {@inheritDoc} */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.existingListFinder, "Existing list finder wasn't injected !");
		Assert.notEmpty(this.creatableLists, "No creatable lists configured !");

	}

	/**
	 * Build available mailing list base on the modele and the list of params.
	 * 
	 * @param modele
	 * @param paramsList
	 * @return the available mailing lists for the modele
	 */
	protected Collection<IMailingList> buildMailingListCollection(
			final IMailingListModel modele, final Collection<String> paramsList) {
		final Collection<IMailingList> availableLists = new HashSet<IMailingList>();

		final Pattern pattern = Pattern.compile(modele.getGroupPatternToMatch());

		if (!CollectionUtils.isEmpty(paramsList)) {
			for (String param : paramsList) {
				Matcher m = pattern.matcher(param);
				if (m.find()) {
					availableLists.add(new BasicMailingList(modele, param));
				}
			}
		}

		return availableLists;
	}

	public void setCreatableLists(final Collection<String> creatableLists) {
		this.creatableLists = creatableLists;
	}

}
