package org.esupportail.sympa.domain.services;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.esco.sympa.domain.groupfinder.impl.MultiValuedAttributeGroupFinder;
import org.esupportail.sympa.domain.model.UserAttributeMapping;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



/**
 * Test the teaching group finder.
 * 
 * @author Maxime BOSSARD - GIP RECIA 2012.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:ldapContext.xml", "classpath:portlet/sympa-portlet.xml"})
public class TeachingGroupFinderTest {

	private static final String SIREN_TEST_VALUE = "19371122300019";

	@Resource(name="myTeachingGroupsFinder")
	private MultiValuedAttributeGroupFinder teachingGroupFinder;

	@Test
	public void testCorrectPattern() throws IOException {
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.put(UserAttributeMapping.USER_ATTRIBUTE_SIREN_KEY, TeachingGroupFinderTest.SIREN_TEST_VALUE);

		Collection<String> groups = this.teachingGroupFinder.findGroupsOfEtab(userInfo);

		Assert.assertNotNull("Teaching groups list is null !", groups);
		Assert.assertTrue("Teaching groups list is empty !", groups.size() > 0);
	}

}
