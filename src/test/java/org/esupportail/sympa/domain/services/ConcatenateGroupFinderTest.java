package org.esupportail.sympa.domain.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.esco.sympa.domain.groupfinder.IEtabGroupsFinder;
import org.esco.sympa.domain.groupfinder.impl.ConcatenateGroupFinder;
import org.esco.sympa.domain.groupfinder.impl.LdapGroupFinder;
import org.esco.sympa.domain.groupfinder.impl.MultiValuedAttributeGroupFinder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;



/**
 * Test the teaching group finder.
 * 
 * @author Maxime BOSSARD - GIP RECIA 2012.
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class ConcatenateGroupFinderTest {

	private static final String GROUPZ = "ZZ";

	private static final String GROUPB = "BB";

	private static final String GROUPY = "YY";

	private static final String GROUPX = "XX";

	private static final String GROUPA = "AA";

	/** Properties UAI. */
	@Value(value = "#{configLdap['test.config.uai.key']}")
	private String uai;

	@Autowired
	private MultiValuedAttributeGroupFinder teachingGroupFinder;

	@Mock
	private LdapGroupFinder lgf1;

	@Mock
	private LdapGroupFinder lgf2;

	@Before
	public void initMock() {
		MockitoAnnotations.initMocks(this);

		Mockito.when(this.lgf1.findGroupsOfEtab(Matchers.any(Map.class))).thenReturn(this.getGroupsLgf1());
		Mockito.when(this.lgf2.findGroupsOfEtab(Matchers.any(Map.class))).thenReturn(this.getGroupsLgf2());
	}

	@Test
	public void testConcatenate() throws IOException {
		ConcatenateGroupFinder cgf = new ConcatenateGroupFinder();
		List<IEtabGroupsFinder> gfs = new ArrayList<IEtabGroupsFinder>();
		gfs.add(this.lgf2);
		gfs.add(this.lgf1);

		cgf.setGroupsFinders(gfs);

		Collection<String> groups = cgf.findGroupsOfEtab(null);

		Assert.assertNotNull("No groups returned !", groups);
		Assert.assertFalse("No groups returned !", groups.isEmpty());

		Iterator<String> itG = groups.iterator();

		Assert.assertEquals("Bag 1° group !", ConcatenateGroupFinderTest.GROUPZ, itG.next());
		Assert.assertEquals("Bag 2° group !", ConcatenateGroupFinderTest.GROUPX, itG.next());
		Assert.assertEquals("Bag 3° group !", ConcatenateGroupFinderTest.GROUPA, itG.next());
		Assert.assertEquals("Bag 4° group !", ConcatenateGroupFinderTest.GROUPZ, itG.next());
		Assert.assertEquals("Bag 5° group !", ConcatenateGroupFinderTest.GROUPB, itG.next());
		Assert.assertEquals("Bag 6° group !", ConcatenateGroupFinderTest.GROUPY, itG.next());
	}

	private Collection<String> getGroupsLgf1() {
		Collection<String> groups1 = new ArrayList<String>();
		groups1.add(ConcatenateGroupFinderTest.GROUPZ);
		groups1.add(ConcatenateGroupFinderTest.GROUPB);
		groups1.add(ConcatenateGroupFinderTest.GROUPY);

		return groups1;
	}

	private Collection<String> getGroupsLgf2() {
		Collection<String> groups1 = new ArrayList<String>();
		groups1.add(ConcatenateGroupFinderTest.GROUPZ);
		groups1.add(ConcatenateGroupFinderTest.GROUPX);
		groups1.add(ConcatenateGroupFinderTest.GROUPA);

		return groups1;
	}
}
