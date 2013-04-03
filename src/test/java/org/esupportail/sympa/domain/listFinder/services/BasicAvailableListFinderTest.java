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
package org.esupportail.sympa.domain.listFinder.services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.esupportail.sympa.domain.listfinder.IExistingListsFinder;
import org.esupportail.sympa.domain.listfinder.IMailingList;
import org.esupportail.sympa.domain.listfinder.IMailingListModel;
import org.esupportail.sympa.domain.listfinder.model.AvailableMailingListsFound;
import org.esupportail.sympa.domain.listfinder.model.BasicMailingListModel;
import org.esupportail.sympa.domain.listfinder.services.BasicAvailableListFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class BasicAvailableListFinderTest {

	private static final Collection<String> EXISTING_LISTS = new HashSet<String>();
	static {
		BasicAvailableListFinderTest.EXISTING_LISTS.add("prefix_liste1_suffix");
		BasicAvailableListFinderTest.EXISTING_LISTS.add("prefix_liste2_suffix");
		BasicAvailableListFinderTest.EXISTING_LISTS.add("prefix_zyx-liste3_suffix");
		BasicAvailableListFinderTest.EXISTING_LISTS.add("prefix_abc-liste3_suffix");
	}

	/** Strings representing a basic description of some creatable lists. */
	private static final Collection<String> CREATABLE_LISTS = new HashSet<String>();
	static {
		BasicAvailableListFinderTest.CREATABLE_LISTS.add("liste1");
		BasicAvailableListFinderTest.CREATABLE_LISTS.add("liste2");
		BasicAvailableListFinderTest.CREATABLE_LISTS.add("zyx-liste3");
		BasicAvailableListFinderTest.CREATABLE_LISTS.add("abc-liste3");
		BasicAvailableListFinderTest.CREATABLE_LISTS.add("liste4");
		BasicAvailableListFinderTest.CREATABLE_LISTS.add("liste5");
		BasicAvailableListFinderTest.CREATABLE_LISTS.add("zyx-liste6");
		BasicAvailableListFinderTest.CREATABLE_LISTS.add("abc-liste7");
	}

	/** Strings representing a basic description of some creatable lists. */
	private static final Collection<IMailingListModel> LIST_MODELS = new HashSet<IMailingListModel>();
	static {
		IMailingListModel model1 = new BasicMailingListModel("recia", "prefix_{NOM}_suffix", "(.*)", "listes du recia");
		BasicAvailableListFinderTest.LIST_MODELS.add(model1);
	}

	/** Lists already existing in Sympa. */
	@Mock
	private IExistingListsFinder existingListFinder;

	/** Tested object. */
	private BasicAvailableListFinder balf;

	@Before
	@SuppressWarnings("unchecked")
	public void initialize() throws Exception {
		// Init mocks
		MockitoAnnotations.initMocks(this);
		Mockito.when(this.existingListFinder.findExistingLists(Matchers.anyMap())).thenReturn(BasicAvailableListFinderTest.EXISTING_LISTS);

		// Inint tested object
		this.balf = new BasicAvailableListFinder();
		this.balf.setCreatableLists(BasicAvailableListFinderTest.CREATABLE_LISTS);
		this.balf.setExistingListsFinder(this.existingListFinder);
		this.balf.afterPropertiesSet();
	}

	@Test
	public void testGetAvailableAndNonExistingLists() throws Exception {
		Map<String, String> userInfo = new HashMap<String, String>();
		AvailableMailingListsFound avLists = this.balf.getAvailableAndNonExistingLists(userInfo, BasicAvailableListFinderTest.LIST_MODELS);
		Collection<IMailingList> mailingLists = avLists.getCreatableLists();
		Assert.assertNotNull("Mailing lists returned should not be null !", mailingLists);
		Assert.assertEquals("4 Mailing lists should be creatable !", 4, mailingLists.size());

		Collection<IMailingList> clone = new HashSet<IMailingList>(mailingLists);
		clone.removeAll(BasicAvailableListFinderTest.EXISTING_LISTS);
		Assert.assertEquals("The creatable mailing lists contain some already created lists !", 4, clone.size());
	}

}
