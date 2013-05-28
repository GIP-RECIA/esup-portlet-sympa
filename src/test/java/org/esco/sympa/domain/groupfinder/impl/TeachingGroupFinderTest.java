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
package org.esco.sympa.domain.groupfinder.impl;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

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
@ContextConfiguration(locations = {"classpath:portlet/sympa-portlet.xml"})
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