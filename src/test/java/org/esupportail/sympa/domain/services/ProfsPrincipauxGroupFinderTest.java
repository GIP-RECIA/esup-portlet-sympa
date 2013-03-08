package org.esupportail.sympa.domain.services;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.esco.sympa.domain.groupfinder.impl.MultiValuedAttributeGroupFinder;
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
public class ProfsPrincipauxGroupFinderTest {

	private static final String UAI_TEST_VALUE = "0371122U";

	@Resource(name="profsPrincipauxGroupsFinder")
	private MultiValuedAttributeGroupFinder profsPrincipauxGroupFinder;

	@Test
	public void testCorrectPattern() throws IOException {
		Map<String, String> userInfo = new HashMap<String, String>();
		userInfo.put("UAI", ProfsPrincipauxGroupFinderTest.UAI_TEST_VALUE);

		Collection<String> groups = this.profsPrincipauxGroupFinder.findGroupsOfEtab(userInfo);

		Assert.assertNotNull("Profs principaux groups list is null !", groups);
		Assert.assertTrue("Profs principaux list is empty !", groups.size() > 0);
	}

}
