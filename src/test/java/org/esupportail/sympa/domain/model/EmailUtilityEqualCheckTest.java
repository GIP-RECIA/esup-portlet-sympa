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

import java.util.ArrayList;

import org.esco.sympa.domain.model.email.EmailUtilityEqualCheck;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



/**
 * Test the implementation checking patterns with an ingoring case equal.
 * 
 * @author Maxime BOSSARD - GIP RECIA 2012.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:emailUtility/emailContext.xml")
public class EmailUtilityEqualCheckTest {

	private static final String PATTERN_OK_1 = "NATIONAL_3";

	private static final String PATTERN_OK_2 = "NATIONAL_4";

	private static final String PATTERN_OK_3 = "NATIONAL_5";

	private static final String PATTERN_KO_1 = "NATIONAL_1";

	@Autowired
	private EmailUtilityEqualCheck emailUtility;

	@Test
	public void testCorrectPattern() {
		Assert.assertTrue("Pattern ok 1 should match !", this.emailUtility.isCorrectEmailUtility(EmailUtilityEqualCheckTest.PATTERN_OK_1));
		Assert.assertTrue("Pattern ok 2 should match !", this.emailUtility.isCorrectEmailUtility(EmailUtilityEqualCheckTest.PATTERN_OK_2));
		Assert.assertTrue("Pattern ok 3 should match !", this.emailUtility.isCorrectEmailUtility(EmailUtilityEqualCheckTest.PATTERN_OK_3));
	}

	@Test
	public void testBadPattern() {
		Assert.assertFalse("Pattern ko 1 shouldn't match !", this.emailUtility.isCorrectEmailUtility(EmailUtilityEqualCheckTest.PATTERN_KO_1));
	}

	@Test
	public void testNull() {
		Assert.assertFalse("Pattern null shouldn't match !", this.emailUtility.isCorrectEmailUtility(null));

		EmailUtilityEqualCheck testNull1 = new EmailUtilityEqualCheck();
		testNull1.setPatterns(null);
		Assert.assertFalse("Pattern ok 1 shouldn't match !", testNull1.isCorrectEmailUtility(EmailUtilityEqualCheckTest.PATTERN_OK_1));

		EmailUtilityEqualCheck testNull2 = new EmailUtilityEqualCheck();
		testNull2.setPatterns(new ArrayList<String>());
		Assert.assertFalse("Pattern ko 1 shouldn't match !", testNull2.isCorrectEmailUtility(EmailUtilityEqualCheckTest.PATTERN_OK_1));

	}

}
