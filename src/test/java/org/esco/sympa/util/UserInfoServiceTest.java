/**
 * 
 */
package org.esco.sympa.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
@RunWith(BlockJUnit4ClassRunner.class)
public class UserInfoServiceTest {

	static {
		// Init
		UserInfoService uis = new UserInfoService();
		uis.setPortalMailAttribute("mail");
		uis.setPortalUaiAttribute("ESCOUAICourant");
		uis.setPortalUidAttribute("uid");
		uis.setPortalCasProxyTicketAttribute("casProxyTicket");
		uis.setPortalIsMemberOfAttribute("isMemberOf");
		uis.setAdminSwitchEtabRegExUai("[^:]+:admin:Listes_Diffusion:local:[^:]+_([0-9]{7}[a-zA-Z])");
		uis.setAdminSwitchEtabRegExLabel("[^:]+:admin:Listes_Diffusion:local:([^:]+)_[0-9]{7}[a-zA-Z]");
		
		try {
			uis.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testParseIsMemberOfMap() throws Exception {
		final List<String> isMemberOfValues = new ArrayList<String>(16);
		isMemberOfValues.add("esco:admin:toto:local:FICTIF_0450822x");
		isMemberOfValues.add("esco:admin:tata:local:FICTIF_0450822x");
		isMemberOfValues.add("esco:admin:tutu:FICTIF_0450822x");
		isMemberOfValues.add("esco:admin:titi");
		isMemberOfValues.add("esco:admin:Listes_Diffusion:local:FICTIF_0450822x");
		isMemberOfValues.add("clg37:admin:toto:local:FICTIF_CLG37_0370822z");
		isMemberOfValues.add("clg37:admin:tata:local:FICTIF_CLG37_0370822z");
		isMemberOfValues.add("clg37:admin:tutu:FICTIF_CLG37_0370822z");
		isMemberOfValues.add("clg37:admin:titi");
		isMemberOfValues.add("clg37:admin:Listes_Diffusion:local:FICTIF_CLG37_0370822z");
		
		final Map<String, String> map = UserInfoService.parseIsMemberOfMap(isMemberOfValues);
		
		Assert.assertEquals("Some etabs are missing !", 2, map.size());
		Assert.assertTrue("One uai missing !", map.containsKey("0450822x"));
		Assert.assertTrue("One uai missing !", map.containsKey("0370822z"));
		Assert.assertEquals("Bad etab label !", "FICTIF", map.get("0450822x"));
		Assert.assertEquals("Bad etab label !", "FICTIF_CLG37", map.get("0370822z"));
	}
	
}
