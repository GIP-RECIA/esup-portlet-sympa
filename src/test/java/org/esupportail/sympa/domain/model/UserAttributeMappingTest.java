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
package org.esupportail.sympa.domain.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



/**
 * Test the user attribute configurable mapping.
 * 
 * @author Maxime BOSSARD - GIP RECIA 2012.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:portlet/sympa-portlet.xml")
public class UserAttributeMappingTest {

	/** Properties Key. */
	private static final String UAI_UA_KEY_PROPERTIES_VALUE_1 = "test.config.uai.key";

	private static final String UAI_PH_1 = "%VALUE1";

	private static final String SIREN_UA_KEY = UserAttributeMapping.USER_ATTRIBUTE_SIREN_KEY;

	private static final String SIREN_PH_1 = "%SIREN";

	private static final String UAI = "0450822x";

	private static final String SIREN = "19280036500012";

	private static final String CHAIN_1 = "test:%SIREN:test";

	private static final String CHAIN_1_RESULT = "test:"+UserAttributeMappingTest.SIREN+":test";

	private static final String CHAIN_2 = "test:{SIREN}:test";

	private static final String CHAIN_2_RESULT = "test:"+UserAttributeMappingTest.SIREN+":test";

	@Resource(name="config")
	private Properties properties;

	@Autowired
	private UserAttributeMapping userAttributeMapping;

	@Test
	public void testCorrectPattern() throws IOException {
		String uaiUAKey = this.properties.getProperty(UserAttributeMappingTest.UAI_UA_KEY_PROPERTIES_VALUE_1);
		Assert.assertNotNull("First key not injected by spring !", uaiUAKey);
		String value1 = this.userAttributeMapping.getMapping(UserAttributeMappingTest.UAI_PH_1);
		Assert.assertEquals("Bad value for first entry !", uaiUAKey, value1);

		String value2 = this.userAttributeMapping.getMapping(UserAttributeMappingTest.SIREN_PH_1);
		Assert.assertEquals("Bad value for second entry !", UserAttributeMappingTest.SIREN_UA_KEY, value2);
	}

	@Test
	public void testSubstitutePlaceholder1() {
		String chain = this.userAttributeMapping.substitutePlaceholder(UserAttributeMappingTest.CHAIN_1, this.buildUserInfo());
		Assert.assertEquals("Bad replacement in CHAIN_1 !", UserAttributeMappingTest.CHAIN_1_RESULT, chain);
	}

	@Test
	public void testSubstitutePlaceholder2() {
		String chain = this.userAttributeMapping.substitutePlaceholder(UserAttributeMappingTest.CHAIN_2, this.buildUserInfo());
		Assert.assertEquals("Bad replacement in CHAIN_2 !", UserAttributeMappingTest.CHAIN_2_RESULT, chain);
	}

	private Map<String,String> buildUserInfo() {
		Map<String, String> userInfo = new HashMap<String, String>();
		String uaiUAKey = this.properties.getProperty(UserAttributeMappingTest.UAI_UA_KEY_PROPERTIES_VALUE_1);
		userInfo.put(uaiUAKey, UserAttributeMappingTest.UAI);
		String sirenUAKey = UserAttributeMappingTest.SIREN_UA_KEY;
		userInfo.put(sirenUAKey, UserAttributeMappingTest.SIREN);

		return userInfo;
	}

}
