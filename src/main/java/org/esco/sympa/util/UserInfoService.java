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
public class UserInfoService {

	private static final UserInfoService INSTANCE = new UserInfoService();

	private static Map<String, String> MOCKED_USER_INFO = null;
	static {
		UserInfoService.MOCKED_USER_INFO = new HashMap<String, String>();
		UserInfoService.MOCKED_USER_INFO.put("casProxyTicket", "testEnv_casProxyTicket");
		UserInfoService.MOCKED_USER_INFO.put("mail", "testEnv_mail@recia.fr");
		UserInfoService.MOCKED_USER_INFO.put("isMemberOf", "testEnv_Groupe42");
		UserInfoService.MOCKED_USER_INFO.put("uid", "testEnv_UID");
		UserInfoService.MOCKED_USER_INFO.put("ESCOUAICourant", "testEnv_UAI");
	}

	private static Map<String, String> ADMIN_USER_INFO = null;
	static {
		UserInfoService.ADMIN_USER_INFO = new HashMap<String, String>();
		UserInfoService.ADMIN_USER_INFO.put("casProxyTicket", "testEnv_casProxyTicket");
		UserInfoService.ADMIN_USER_INFO.put("mail", "mxbossard@gmail.com"); // maxime.bossard@recia.fr  CUNAFO@demo-orleans-tours.fr
		UserInfoService.ADMIN_USER_INFO.put("isMemberOf", "esco:admin:Listes_Diffusion:local:FICTIF_0450822X");
		UserInfoService.ADMIN_USER_INFO.put("uid", "F1000ugr");
		UserInfoService.ADMIN_USER_INFO.put("ESCOUAICourant", "0450822X");
	}

	private static Map<String, String> BASIC_USER_INFO = null;
	static {
		UserInfoService.BASIC_USER_INFO = new HashMap<String, String>();
		UserInfoService.BASIC_USER_INFO.put("casProxyTicket", "testEnv_casProxyTicket");
		UserInfoService.BASIC_USER_INFO.put("mail", "dylan.tusemy@netocentre.fr");
		UserInfoService.BASIC_USER_INFO.put("isMemberOf", "");
		UserInfoService.BASIC_USER_INFO.put("uid", "F1000u5z");
		UserInfoService.BASIC_USER_INFO.put("ESCOUAICourant", "0450822X");
	}

	/** Hidden constructor. */
	private UserInfoService() {
		super();
	}

	public static UserInfoService getInstance() {
		return UserInfoService.INSTANCE;
	}

	/**
	 * Retrieve the user info from portlet context, or the Mocked user info
	 * if the system property testEnv = true.
	 * 
	 * @param request the portlet request
	 * @return the user info map
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getUserInfo(final PortletRequest request) {
		Map<String, String> userInfo = (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);
		if ((userInfo == null) && "true".equals(System.getProperty("testEnv"))) {
			userInfo = UserInfoService.ADMIN_USER_INFO;
		}

		return userInfo;
	}

}
