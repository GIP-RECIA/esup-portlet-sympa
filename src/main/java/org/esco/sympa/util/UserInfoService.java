/**
 * 
 */
package org.esco.sympa.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.ReadOnlyException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.utils.Assert;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;

/**
 * Helper design to retrieve user info.
 * This helper allow the portlet to work in test environment by mocking the user info,
 * if the System property "testEnv" = true.
 * 
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class UserInfoService implements InitializingBean {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(UserInfoService.class);

	/** Multivalued user info map request attribute key. */
	private static final String MULTIVALUED_USER_INFO_ATTR_KEY = "org.jasig.portlet.USER_INFO_MULTIVALUED";

	private static Map<String, String> MOCKED_USER_INFO = null;
	static {
		UserInfoService.MOCKED_USER_INFO = new HashMap<String, String>();
		UserInfoService.MOCKED_USER_INFO.put("casProxyTicket", "testEnv_casProxyTicket");
		UserInfoService.MOCKED_USER_INFO.put("mail", "testEnv_mail@recia.fr");
		//UserInfoService.MOCKED_USER_INFO.put("isMemberOf", "testEnv_Groupe42");
		UserInfoService.MOCKED_USER_INFO.put("uid", "testEnv_UID");
		UserInfoService.MOCKED_USER_INFO.put("ESCOUAICourant", "testEnv_UAI");
	}

	private static Map<String, String> ADMIN_USER_INFO = null;
	static {
		UserInfoService.ADMIN_USER_INFO = new HashMap<String, String>();
		UserInfoService.ADMIN_USER_INFO.put("casProxyTicket", "testEnv_casProxyTicket");
		UserInfoService.ADMIN_USER_INFO.put("mail", "CUNAFO@demo-orleans-tours.fr"); // maxime.bossard@recia.fr   mxbossard@gmail.com
		//UserInfoService.ADMIN_USER_INFO.put("isMemberOf", "esco:admin:Listes_Diffusion:local:FICTIF_0450822X");
		UserInfoService.ADMIN_USER_INFO.put("uid", "F1000ugr");
		UserInfoService.ADMIN_USER_INFO.put("ESCOUAICourant", "0450822X");
	}
	
	private static Map<String, List<String>> ADMIN_MV_USER_INFO = null;
	static {
		final Map<String, List<String>> map = new HashMap<String, List<String>>();
		final List<String> isMemberOfValues = new ArrayList<String>(8);
		isMemberOfValues.add("esco:admin:toto:local:FICTIF_0450822X");
		isMemberOfValues.add("esco:admin:tata:local:FICTIF_0450822X");
		isMemberOfValues.add("esco:admin:tutu:FICTIF_0450822X");
		isMemberOfValues.add("esco:admin:titi");
		isMemberOfValues.add("esco:admin:Listes_Diffusion:local:FICTIF_0450822X");
		isMemberOfValues.add("clg37:admin:toto:local:CLG-FICTIF37-ac-ORL._TOURS_0377777U");
		isMemberOfValues.add("clg37:admin:tata:local:CLG-FICTIF37-ac-ORL._TOURS_0377777U");
		isMemberOfValues.add("clg37:admin:tutu:CLG-FICTIF37-ac-ORL._TOURS_0377777U");
		isMemberOfValues.add("clg37:admin:titi");
		isMemberOfValues.add("clg37:admin:Listes_Diffusion:local:CLG-FICTIF37-ac-ORL._TOURS_0377777U");
		map.put("isMemberOf", isMemberOfValues);
		UserInfoService.ADMIN_MV_USER_INFO = map;
	}
	
	private static Map<String, String> BASIC_USER_INFO = null;
	static {
		UserInfoService.BASIC_USER_INFO = new HashMap<String, String>();
		UserInfoService.BASIC_USER_INFO.put("casProxyTicket", "testEnv_casProxyTicket");
		UserInfoService.BASIC_USER_INFO.put("mail", "dylan.tusemy@netocentre.fr");
		//UserInfoService.BASIC_USER_INFO.put("isMemberOf", "");
		UserInfoService.BASIC_USER_INFO.put("uid", "F1000u5z");
		UserInfoService.BASIC_USER_INFO.put("ESCOUAICourant", "0450822X");
	}

	
	/** Param key to store the the UAI which override the one in user infos map. */
	private static final String OVERRIDING_UAI_PREF_KEY = "overridingUai";
	
	private static String portalMailAttribute;

	private static String portalUaiAttribute;

	private static String portalUidAttribute;
	
	private static String portalIsMemberOfAttribute;

	private static String portalCasProxyTicketAttribute;

	private static String adminSwitchEtabRegExUai;
	private static String adminSwitchEtabRegExLabel;
	
	private static Pattern adminSwitchEtabUaiPattern;
	private static Pattern adminSwitchEtabLabelPattern;
	
	/**
	 * Retrieve the user info from portlet context, or the Mocked user info
	 * if the system property testEnv = true.
	 * 
	 * @param request the portlet request
	 * @return the user info map
	 */
	
	public static Map<String, String> getUserInfo(final PortletRequest request) {
		final Map<String, String> userInfo = getUserInfoInternal(request);

		// MBD: Add support for UAI switching inside portlet.
		// See RT #63211 https://rt.giprecia.fr/rt/Ticket/Display.html?id=63211
		UserInfoService.checkForReplacedUai(request, userInfo);
		
		return userInfo;
	}

	/**
	 * Find the map of Etab UAI => Etab label the Admin can switch on.
	 * 
	 * @param request
	 * @return
	 */
	public static Map<String, String> findAdminSwitchEtabMap(final PortletRequest request) {
		final Map<String, List<String>> userInfo = getMultiValuedUserInfoInternal(request);
		final List<String> isMemberOfValues = userInfo.get(portalIsMemberOfAttribute);
		
		final Map<String, String> etabMap = parseIsMemberOfMap(isMemberOfValues);
		
		return etabMap;
	}

	/**
	 * Replace the current UAI which is stored in User Infos map.
	 * MBD: Add support for UAI switching inside portlet.
	 * See RT #63211 https://rt.giprecia.fr/rt/Ticket/Display.html?id=63211
	 * 
	 * @param request
	 * @param newUai
	 */
	public static void replaceCurrentUai(final PortletRequest request, final String newUai) {
		if (StringUtils.hasText(newUai)) {
			final PortletPreferences prefs = request.getPreferences();
			try {
				prefs.setValue(OVERRIDING_UAI_PREF_KEY, newUai);
			} catch (final ReadOnlyException e) {
				LOG.warn("Unable to write preference with key: " + OVERRIDING_UAI_PREF_KEY, e);
			}
			//request.getPortletSession().setAttribute(OVERRIDE_UAI_PREF_KEY, newUai);
		}
	}

	@SuppressWarnings("unchecked")
	protected static Map<String, String> getUserInfoInternal(final PortletRequest request) {
		Map<String, String> userInfo = (Map<String, String>) request.getAttribute(PortletRequest.USER_INFO);
		if ((userInfo == null) && "true".equals(System.getProperty("testEnv"))) {
			userInfo = UserInfoService.ADMIN_USER_INFO;
		}
		return userInfo;
	}

	@SuppressWarnings("unchecked")
	protected static Map<String, List<String>> getMultiValuedUserInfoInternal(final PortletRequest request) {
		Map<String, List<String>> mvUserInfo =
				(Map<String, List<String>>) request.getAttribute(MULTIVALUED_USER_INFO_ATTR_KEY);
		if ((mvUserInfo == null) && "true".equals(System.getProperty("testEnv"))) {
			mvUserInfo = UserInfoService.ADMIN_MV_USER_INFO;
		}
		return mvUserInfo;
	}

	protected static Map<String, String> parseIsMemberOfMap(final List<String> isMemberOfValues) {
		final Map<String, String> etabMap = new HashMap<String, String>(8);
		
		if (isMemberOfValues != null) {
			for (final String isMemberOfVal : isMemberOfValues) {
				final Matcher matcherUai = adminSwitchEtabUaiPattern.matcher(isMemberOfVal);
				
				if (matcherUai.find()) {
					final Matcher matcherLabel = adminSwitchEtabLabelPattern.matcher(isMemberOfVal);
					matcherLabel.find();
					
					final String uai = matcherUai.group(1);
					final String label = matcherLabel.group(1);
					if (StringUtils.hasText(uai) && StringUtils.hasText(label)) {
						etabMap.put(uai, label);
					}
					
				}
			}
		}
		
		return etabMap;
	}
	
	/**
	 * Check if the current UAI need to be replaced.
	 * MBD: Add support for UAI switching inside portlet.
	 * See RT #63211 https://rt.giprecia.fr/rt/Ticket/Display.html?id=63211
	 * 
	 * @param request
	 * @param userInfo
	 */
	protected static void checkForReplacedUai(PortletRequest request, Map<String, String> userInfo) {
		final PortletPreferences prefs = request.getPreferences();
		final String overridingUai = prefs.getValue(OVERRIDING_UAI_PREF_KEY, null);
		if (StringUtils.hasText(overridingUai)) {
			userInfo.put(portalUaiAttribute, overridingUai);
		}
		
		
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(UserInfoService.portalMailAttribute, "No portal mail attribute configured !");
		Assert.hasText(UserInfoService.portalUaiAttribute, "No portal UAI attribute configured !");
		Assert.hasText(UserInfoService.portalUidAttribute, "No portal uid attribute configured !");
		Assert.hasText(UserInfoService.portalIsMemberOfAttribute, "No portal isMemberOf attribute configured !");
		
		Assert.hasText(UserInfoService.adminSwitchEtabRegExUai, "No adminSwitchEtabRegExUai configured !");
		Assert.hasText(UserInfoService.adminSwitchEtabRegExLabel, "No adminSwitchEtabRegExLabel configured !");
		
		UserInfoService.adminSwitchEtabUaiPattern = Pattern.compile(UserInfoService.adminSwitchEtabRegExUai);
		UserInfoService.adminSwitchEtabLabelPattern = Pattern.compile(UserInfoService.adminSwitchEtabRegExLabel);
		
		Assert.hasText(UserInfoService.portalCasProxyTicketAttribute, "No portal CAS proxy ticket attribute configured !");
	}

	public static String getPortalMailAttribute() {
		return UserInfoService.portalMailAttribute;
	}

	public void setPortalMailAttribute(final String portalMailAttribute) {
		UserInfoService.portalMailAttribute = portalMailAttribute;
	}

	public static String getPortalUaiAttribute() {
		return UserInfoService.portalUaiAttribute;
	}

	public void setPortalUaiAttribute(final String portalUaiAttribute) {
		UserInfoService.portalUaiAttribute = portalUaiAttribute;
	}

	public static String getPortalUidAttribute() {
		return UserInfoService.portalUidAttribute;
	}

	public void setPortalUidAttribute(final String portalUidAttribute) {
		UserInfoService.portalUidAttribute = portalUidAttribute;
	}

	public static String getPortalCasProxyTicketAttribute() {
		return UserInfoService.portalCasProxyTicketAttribute;
	}

	public void setPortalCasProxyTicketAttribute(final String portalCasProxyTicketAttribute) {
		UserInfoService.portalCasProxyTicketAttribute = portalCasProxyTicketAttribute;
	}

	/**
	 * Getter of portalIsMemberOfAttribute.
	 *
	 * @return the portalIsMemberOfAttribute
	 */
	public static String getPortalIsMemberOfAttribute() {
		return portalIsMemberOfAttribute;
	}

	/**
	 * Setter of portalIsMemberOfAttribute.
	 *
	 * @param portalIsMemberOfAttribute the portalIsMemberOfAttribute to set
	 */
	public void setPortalIsMemberOfAttribute(String portalIsMemberOfAttribute) {
		UserInfoService.portalIsMemberOfAttribute = portalIsMemberOfAttribute;
	}

	/**
	 * Getter of adminSwitchEtabRegExUai.
	 *
	 * @return the adminSwitchEtabRegExUai
	 */
	public static String getAdminSwitchEtabRegExUai() {
		return adminSwitchEtabRegExUai;
	}

	/**
	 * Setter of adminSwitchEtabRegExUai.
	 *
	 * @param adminSwitchEtabRegExUai the adminSwitchEtabRegExUai to set
	 */
	public void setAdminSwitchEtabRegExUai(String adminSwitchEtabRegExUai) {
		UserInfoService.adminSwitchEtabRegExUai = adminSwitchEtabRegExUai;
	}

	/**
	 * Getter of adminSwitchEtabRegExLabel.
	 *
	 * @return the adminSwitchEtabRegExLabel
	 */
	public static String getAdminSwitchEtabRegExLabel() {
		return adminSwitchEtabRegExLabel;
	}

	/**
	 * Setter of adminSwitchEtabRegExLabel.
	 *
	 * @param adminSwitchEtabRegExLabel the adminSwitchEtabRegExLabel to set
	 */
	public void setAdminSwitchEtabRegExLabel(String adminSwitchEtabRegExLabel) {
		UserInfoService.adminSwitchEtabRegExLabel = adminSwitchEtabRegExLabel;
	}


}
