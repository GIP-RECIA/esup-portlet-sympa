/**
 * 
 */
package org.esco.sympa.util;

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;

/**
 * Helper design to retrieve user info.
 * This helper allow the portlet to work in test environment by mocking the user info,
 * if the System property "testEnv" = true.
 * 
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public abstract class UserInfoHelper {

	private static Map<String, String> MOCKED_USER_INFO = null;
	static {
		UserInfoHelper.MOCKED_USER_INFO = new HashMap<String, String>();
		UserInfoHelper.MOCKED_USER_INFO.put("casProxyTicket", "testEnv_casProxyTicket");
		UserInfoHelper.MOCKED_USER_INFO.put("mail", "testEnv_mail@recia.fr");
		UserInfoHelper.MOCKED_USER_INFO.put("isMemberOf", "testEnv_Groupe42");
		UserInfoHelper.MOCKED_USER_INFO.put("uid", "testEnv_UID");
		UserInfoHelper.MOCKED_USER_INFO.put("ESCOUAICourant", "testEnv_UAI");
	}

	private static Map<String, String> ADMIN_USER_INFO = null;
	static {
		UserInfoHelper.ADMIN_USER_INFO = new HashMap<String, String>();
		UserInfoHelper.ADMIN_USER_INFO.put("casProxyTicket", "testEnv_casProxyTicket");
		UserInfoHelper.ADMIN_USER_INFO.put("mail", "CUNAFO@demo-orleans-tours.fr");
		UserInfoHelper.ADMIN_USER_INFO.put("isMemberOf", "esco:admin:Listes_Diffusion:local:FICTIF_0450822X");
		UserInfoHelper.ADMIN_USER_INFO.put("uid", "F1000ugr");
		UserInfoHelper.ADMIN_USER_INFO.put("ESCOUAICourant", "0450822X");
	}

	private static Map<String, String> BASIC_USER_INFO = null;
	static {
		UserInfoHelper.BASIC_USER_INFO = new HashMap<String, String>();
		UserInfoHelper.BASIC_USER_INFO.put("casProxyTicket", "testEnv_casProxyTicket");
		UserInfoHelper.BASIC_USER_INFO.put("mail", "dylan.tusemy@netocentre.fr");
		UserInfoHelper.BASIC_USER_INFO.put("isMemberOf", "");
		UserInfoHelper.BASIC_USER_INFO.put("uid", "F1000u5z");
		UserInfoHelper.BASIC_USER_INFO.put("ESCOUAICourant", "0450822X");
	}

	/**
	 * Retrieve the user info from portlet context, or the Mocked user info
	 * if the system property testEnv = true.
	 * 
	 * @param request the portlet request
	 * @return the user info map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getUserInfo(final PortletRequest request) {
		Map<String, String> userInfo = (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);
		if ((userInfo == null) && "true".equals(System.getProperty("testEnv"))) {
			userInfo = UserInfoHelper.BASIC_USER_INFO;
		}

		return userInfo;
	}

}
