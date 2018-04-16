package org.esupportail.sympa.recia;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserInfoBean {
	

	private String uai;
	private List<String> allUai ;
	private List<String> memberOf;
	private String displayName;
	private String mail;
	private String mailForDebug;
	private Map<String, List<Object>> initMap;
	
	public void init(Map<String, List<Object>> initMap, IUserInfoAttr attr) {
		this.initMap = initMap;
		uai = first(attr.getAttrUai());
		allUai = all(attr.getAttrAllUai());
		memberOf = all(attr.getAttrMemberOf());
		mail = first(attr.getAttrMail());
		displayName = first(attr.getAttrName());
		
	}
	
	private String first(String param){
		if (initMap != null) {
			List<Object> l = initMap.get(param);
			if (l != null) {
				Object o = l.get(0);
				return o != null ? o.toString() : "";
			}
		}
		return "";
	}
	
	private List<String> all(String param) {
		List<String> res = new ArrayList<String>();
		if (initMap != null) {
			List<Object> l = initMap.get(param);
			if (l != null) {
				for (Object o : l) {
					if (o != null) {
					res.add(o.toString());
					}
				}
			}
		}
		return res;
	}
	
	
	
	public String getUai() {
		return uai;
	}
	
	public void setUai(String uai) {
		this.uai = uai;
	}
	
	public List<String> getAllUai() {
		return allUai;
	}
	public void setAllUai(List<String> allUai) {
		this.allUai = allUai;
	}
	public List<String> getMemberOf() {
		return memberOf;
	}
	public void setMemberOf(List<String> memberOf) {
		this.memberOf = memberOf;
	}
	public boolean isNotInit() {
		return initMap == null;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMailForDebug() {
		return mailForDebug;
	}

	public void setMailForDebug(String mailForDebug) {
		this.mailForDebug = mailForDebug;
	}
	
	
}
