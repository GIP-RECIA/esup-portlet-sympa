package org.esupportail.sympa.recia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

public class RobotSympaConf implements InitializingBean {
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	// toutes les inits sont la à titre d'exemple, ils peuvent être surchargé par les setters.
	private boolean forAllUai=false;
	private String formatUrl = "http://%s/sympa";
	private String formatSoapUrl = "http://%s/sympasoap";
	private String formatAdminUrl = "http://%s/admin/%%l";
	private String formatNewListUrl = "http://%s/create_list_request";
	
	
	
	/** 
	 * Donne le domaine d'une liste en fonction du prefix d'un groupe de la branche groupe
	 * par exmple esco -> list.netocentre.fr
	 * et 		clg37 -> list.touraine-eschool.fr
	 */
	private Map<String, String> stem2domaine = new HashMap<String, String>();
	
	private Map<String, String> stem2PortletAdmin = new HashMap<String, String>();

	/**
	 * donne les regexs pour trouver les dommaine par les groupes de l'utilisateur.
	 */
	private List<String> regexFormatByUai = new ArrayList<String>();
	private List<String> regexFormatAdminByUai = new ArrayList<String>();
	
/*	{	// exemple  externalisé par le setter:
		// pour l'exemple le grp1 sera la clef de stem2domaine et %s remplace par l'uai de l'utilisateur
		regexFormatByUai.add("([^:]+):Applications:Listes_Diffusion:[^:_]+_%s");
		
		regexFormatAdminByUai.add("([^:]+):admin:Listes_Diffusion:local:[^:_]+_%s");
		
		stem2domaine.put("esco", "list.netocentre.fr");
		stem2domaine.put("clg37", "list.touraine-eschool.fr");
		
		stem2PortletAdmin.put("esco", "/portail/p/listesdiffusion");
		stem2PortletAdmin.put("clg37", "/portail/p/listesdiffusionclg37");
	}
*/	
	
	public void afterPropertiesSet() throws Exception {
		if (logger.isInfoEnabled()) {
			logger.info(this.toString());
		}
		
	}

	private Matcher findGrpMatcher(String uai, List<String> userGrps, List<String> regexFormatList) {
		if (uai == null || userGrps == null ||  regexFormatList == null) {
			return null;
		}
		for (String rgxFormat : regexFormatList) {
			if (rgxFormat != null) {
				try {
					String regex = String.format(rgxFormat, uai);
					Pattern pattern = Pattern.compile(regex);
					for (String grp : userGrps) {
						try {
							Matcher matcher = pattern.matcher(grp);
							if (matcher.matches()) {
								return matcher;
							}
						} catch (Exception e) {
							logger.error(String.format("group user = %s erreur=%s", grp,  e.toString()));
						}
					}
				} catch (Exception e) {
					logger.error(String.format("error=%d uai=%s rgxFormat=%s erreur=%s", uai, rgxFormat, e.toString() ));
				}
			}
		}
		return null;
	}
	/**
	 * Donne les infos du robot correspondant a l'uai, si les groupes utilisateur le permetent.
	 * sinon renvoie null; 
	 * @param uai
	 * @param userGrps
	 * @return RobotSympaInfo :les info du robot ou null
	 */
	public RobotSympaInfo getRobotSympaInfoByUai(String uai, List<String> userGrps) {
		Matcher matcher = findGrpMatcher(uai, userGrps, regexFormatByUai);
		if (matcher != null) {
			String stem = matcher.group(1);
			String domaine = stem2domaine.get(stem);
			if (domaine != null) {
				RobotSympaInfo rsi = new RobotSympaInfo();
				try {
					rsi.dom = domaine;
					rsi.nom = String.format("%s.%s", uai.toLowerCase(), domaine);
					rsi.uai = uai;
					rsi.url = String.format(formatUrl, rsi.nom);
					rsi.soapUrl = String.format(formatSoapUrl, rsi.nom);
					rsi.adminUrl = String.format(formatAdminUrl, rsi.nom);
					rsi.adminPortletUrl = stem2PortletAdmin.get(stem);
					rsi.newListUrl =String.format(formatNewListUrl, rsi.nom);
					return rsi;
				} catch (Exception e) {
					logger.error(String.format(" robotInfo = %s \n  erreur=%s", rsi.toString(),  e.toString()));
				}
			}
		}	
		return null;
	}
	/**
	 * indique si les groupes permetent l'adminitration des listes du robot donné par l'uai
	 * @param uai
	 * @param userGrps
	 * @return
	 */
	public boolean isAdminRobotSympaByUai(String uai, List<String> userGrps) {
		Matcher matcher = findGrpMatcher(uai, userGrps, regexFormatAdminByUai);
		return  (matcher != null) ;
		
	}
	
	public Map<String, String> getStem2domaine() {
		return stem2domaine;
	}


	public void setStem2domaine(Map<String, String> stem2domaine) {
		this.stem2domaine = stem2domaine;
	}


	public List<String> getRegexFormatByUai() {
		return regexFormatByUai;
	}


	public void setRegexFormatByUai(List<String> regexFormatByUai) {
		this.regexFormatByUai = regexFormatByUai;
	}

	public String getFormatUrl() {
		return formatUrl;
	}

	public void setFormatUrl(String formatUrl) {
		this.formatUrl = formatUrl;
	}

	public String getFormatSoapUrl() {
		return formatSoapUrl;
	}

	public void setFormatSoapUrl(String formatSoapUrl) {
		this.formatSoapUrl = formatSoapUrl;
	}

	public String getFormatAdminUrl() {
		return formatAdminUrl;
	}

	public void setFormatAdminUrl(String formatAdminUrl) {
		this.formatAdminUrl = formatAdminUrl;
	}

	public String getFormatNewListUrl() {
		return formatNewListUrl;
	}

	public void setFormatNewListUrl(String formatNewListUrl) {
		this.formatNewListUrl = formatNewListUrl;
	}

	
	public boolean isForAllUai() {
		return forAllUai;
	}

	public void setForAllUai(boolean forAllUai) {
		this.forAllUai = forAllUai;
	}

	public Map<String, String> getStem2PortletAdmin() {
		return stem2PortletAdmin;
	}

	public void setStem2PortletAdmin(Map<String, String> stem2PortletAdmin) {
		this.stem2PortletAdmin = stem2PortletAdmin;
	}

	public List<String> getRegexFormatAdminByUai() {
		return regexFormatAdminByUai;
	}

	public void setRegexFormatAdminByUai(List<String> regexFormatAdminByUai) {
		this.regexFormatAdminByUai = regexFormatAdminByUai;
	}

	@Override
	public String toString() {
		return "RobotSympaConf [forAllUai=" + forAllUai + ", formatUrl=" + formatUrl + ", formatSoapUrl="
				+ formatSoapUrl + ", formatAdminUrl=" + formatAdminUrl + ", formatNewListUrl=" + formatNewListUrl
				+ ", stem2domaine=" + stem2domaine + ", stem2PortletAdmin=" + stem2PortletAdmin + ", regexFormatByUai="
				+ regexFormatByUai + ", regexFormatAdminByUai=" + regexFormatAdminByUai + "]";
	}
	
	


}
