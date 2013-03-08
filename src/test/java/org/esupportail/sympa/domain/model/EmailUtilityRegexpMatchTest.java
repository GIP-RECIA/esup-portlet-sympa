package org.esupportail.sympa.domain.model;

import java.util.ArrayList;

import org.esco.sympa.domain.model.email.EmailUtilityRegexpMatch;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * Test the implementation matching pattern with a regexp.
 * 
 * @author Maxime BOSSARD - GIP RECIA 2012.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:emailUtility/emailContext.xml")
public class EmailUtilityRegexpMatchTest {

	private static final String pattern_ok_1 = "test@netocentre.fr";

	private static final String pattern_ok_2 = "foo@ac-orleans-tours.fr";

	private static final String pattern_ko_1 = "bar@hotmail.com";

	@Autowired
	private EmailUtilityRegexpMatch emailUtility;

	@Test
	public void testCorrectPattern() {
		Assert.assertTrue("Pattern ok 1 should match !", this.emailUtility.isCorrectEmailUtility(EmailUtilityRegexpMatchTest.pattern_ok_1));
		Assert.assertTrue("Pattern ok 2 should match !", this.emailUtility.isCorrectEmailUtility(EmailUtilityRegexpMatchTest.pattern_ok_2));
	}

	@Test
	public void testBadPattern() {
		Assert.assertFalse("Pattern ko 1 shouldn't match !", this.emailUtility.isCorrectEmailUtility(EmailUtilityRegexpMatchTest.pattern_ko_1));
	}

	@Test
	public void testNull() throws Exception {
		Assert.assertFalse("Pattern null shouldn't match !", this.emailUtility.isCorrectEmailUtility(null));

		EmailUtilityRegexpMatch testNull1 = new EmailUtilityRegexpMatch();
		testNull1.setPatterns(null);
		testNull1.afterPropertiesSet();
		Assert.assertFalse("Pattern ok 1 shouldn't match !", testNull1.isCorrectEmailUtility(EmailUtilityRegexpMatchTest.pattern_ok_1));

		EmailUtilityRegexpMatch testNull2 = new EmailUtilityRegexpMatch();
		testNull2.setPatterns(new ArrayList<String>());
		testNull2.afterPropertiesSet();
		Assert.assertFalse("Pattern ok 1 shouldn't match !", testNull2.isCorrectEmailUtility(EmailUtilityRegexpMatchTest.pattern_ok_1));

	}

}
