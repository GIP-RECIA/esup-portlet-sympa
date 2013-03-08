package org.esupportail.sympa.domain.listFinder.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.esupportail.sympa.domain.listfinder.services.SympaExistingListFinder;
import org.esupportail.sympa.domain.model.LdapPerson;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.IDomainService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Existing list finder implementation responsible for querying Sympa to find
 * the list already existing.
 * 
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class SympaExistingListFinderTest {

	private static final String UID = "f04523x";

	private static final String LIST1_NAME = "list1";
	private static final String LIST2_NAME = "list2";
	private static final String LIST3_NAME = "list3";

	private static final String LISTS_DOMAIN = "@recia.fr";

	private static final List<UserSympaListWithUrl> SYMPA_LISTS = new ArrayList<UserSympaListWithUrl>();
	private static final UserSympaListWithUrl list1 = new UserSympaListWithUrl();
	private static final UserSympaListWithUrl list2 = new UserSympaListWithUrl();
	private static final UserSympaListWithUrl list3 = new UserSympaListWithUrl();
	static {
		SympaExistingListFinderTest.list1.setAddress(SympaExistingListFinderTest.LIST1_NAME + SympaExistingListFinderTest.LISTS_DOMAIN);
		SympaExistingListFinderTest.list1.setEditor(false);
		SympaExistingListFinderTest.list1.setHomepage("http://recia.fr/sympa");
		SympaExistingListFinderTest.list1.setListAdminUrl("http://recia.fr/sympa/list1/admin");
		SympaExistingListFinderTest.list1.setListArchivesUrl("http://recia.fr/sympa/list1/arc");
		SympaExistingListFinderTest.list1.setListUrl("http://recia.fr/sympa/list1");
		SympaExistingListFinderTest.list1.setOwner(true);
		SympaExistingListFinderTest.list1.setSubject("List nunber 1");
		SympaExistingListFinderTest.list1.setSubscriber(false);

		SympaExistingListFinderTest.list2.setAddress(SympaExistingListFinderTest.LIST2_NAME + SympaExistingListFinderTest.LISTS_DOMAIN);
		SympaExistingListFinderTest.list2.setEditor(true);
		SympaExistingListFinderTest.list2.setHomepage("http://recia.fr/sympa");
		SympaExistingListFinderTest.list2.setListAdminUrl("http://recia.fr/sympa/list2/admin");
		SympaExistingListFinderTest.list2.setListArchivesUrl("http://recia.fr/sympa/list2/arc");
		SympaExistingListFinderTest.list2.setListUrl("http://recia.fr/sympa/list2");
		SympaExistingListFinderTest.list2.setOwner(true);
		SympaExistingListFinderTest.list2.setSubject("List nunber 2");
		SympaExistingListFinderTest.list2.setSubscriber(false);

		SympaExistingListFinderTest.list3.setAddress(SympaExistingListFinderTest.LIST3_NAME + SympaExistingListFinderTest.LISTS_DOMAIN);
		SympaExistingListFinderTest.list3.setEditor(false);
		SympaExistingListFinderTest.list3.setHomepage("http://recia.fr/sympa");
		SympaExistingListFinderTest.list3.setListAdminUrl("http://recia.fr/sympa/list3/admin");
		SympaExistingListFinderTest.list3.setListArchivesUrl("http://recia.fr/sympa/list3/arc");
		SympaExistingListFinderTest.list3.setListUrl("http://recia.fr/sympa/list3");
		SympaExistingListFinderTest.list3.setOwner(false);
		SympaExistingListFinderTest.list3.setSubject("List nunber 3");
		SympaExistingListFinderTest.list3.setSubscriber(true);

		SympaExistingListFinderTest.SYMPA_LISTS.add(SympaExistingListFinderTest.list1);
		SympaExistingListFinderTest.SYMPA_LISTS.add(SympaExistingListFinderTest.list2);
		SympaExistingListFinderTest.SYMPA_LISTS.add(SympaExistingListFinderTest.list3);
	}

	@Mock
	private IDomainService domainService;

	@Mock
	private LdapPerson ldapPerson;

	private SympaExistingListFinder self;

	@Before
	public void initialize() throws Exception {
		// Init mocks
		MockitoAnnotations.initMocks(this);

		Mockito.when(this.ldapPerson.getUidAttribute()).thenReturn(SympaExistingListFinderTest.UID);

		Mockito.when(this.domainService.getLists()).thenReturn(SympaExistingListFinderTest.SYMPA_LISTS);

		// Inint tested object
		this.self = new SympaExistingListFinder();
		this.self.setDomainService(this.domainService);
		this.self.setLdapPerson(this.ldapPerson);
		this.self.afterPropertiesSet();
	}

	@Test
	public void testFindExistingLists() throws Exception {
		Map<String, String> userInfo = new HashMap<String, String>();
		Collection<String> existingLists = this.self.findExistingLists(userInfo);

		Assert.assertNotNull("Null returned !", existingLists);
		Assert.assertEquals("Bad number of lists returned !", 3, existingLists.size());

		Set<String> expectedListNames = new HashSet<String>();
		expectedListNames.add(SympaExistingListFinderTest.LIST1_NAME);
		expectedListNames.add(SympaExistingListFinderTest.LIST2_NAME);
		expectedListNames.add(SympaExistingListFinderTest.LIST3_NAME);

		// Assert the results are exaclty what we need.
		Assert.assertTrue("Returned lists doesn't match with the expected results !", existingLists.containsAll(expectedListNames));
		Assert.assertTrue("Returned lists doesn't match with the expected results !", expectedListNames.containsAll(existingLists));
	}

}