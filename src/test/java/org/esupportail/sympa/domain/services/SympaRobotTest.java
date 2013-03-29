/**
 * 
 */
package org.esupportail.sympa.domain.services;

import junit.framework.Assert;

import org.esupportail.sympa.domain.services.impl.SympaRobot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class SympaRobotTest {

	@Test
	public void testTransformRobotUrl() throws Exception {
		final SympaRobot robot = new SympaRobot("test.recia.fr");

		String url = robot.transformRobotUrl("http://sympa.fr");
		Assert.assertEquals("Bad transformation !", "http://test.recia.fr", url);

		url = robot.transformRobotUrl("http://sympa.fr/");
		Assert.assertEquals("Bad transformation !", "http://test.recia.fr/", url);

		url = robot.transformRobotUrl("https://sympa.fr/foo");
		Assert.assertEquals("Bad transformation !", "https://test.recia.fr/foo", url);

		url = robot.transformRobotUrl("http://sympa.fr?bar");
		Assert.assertEquals("Bad transformation !", "http://test.recia.fr?bar", url);

		url = robot.transformRobotUrl("https://sympa.fr/foo/?bar");
		Assert.assertEquals("Bad transformation !", "https://test.recia.fr/foo/?bar", url);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNullTransformRobotUrl() throws Exception {
		new SympaRobot(null);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testEmptyTransformRobotUrl() throws Exception {
		new SympaRobot("");
	}

	@Test
	public void testDefaultSympaRobot() throws Exception {
		SympaRobot defaultRobot1 = SympaRobot.getDefaultRobot();
		SympaRobot defaultRobot2 = SympaRobot.getDefaultRobot();

		Assert.assertTrue("Default Robot not static !", defaultRobot1 == defaultRobot2);
	}
}
