package org.esupportail.sympa.recia;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.portlet.handler.HandlerInterceptorAdapter;


public class UserInfoInitInterceptor extends HandlerInterceptorAdapter implements IUserInfoAttr {
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private UserInfoBean userInfo;
	
	private String attrMail;
	private String attrUai;
	private String attrAllUai;
	private String attrName;
	private String attrMemberOf;
	private String mailForDebug;
	
	@Override
	protected boolean preHandle(
				PortletRequest request,
				PortletResponse response, Object handler) throws Exception {
		
		@SuppressWarnings("unused")
		PortletSession session = request.getPortletSession(true);
		
		if (userInfo.isNotInit()) {
			@SuppressWarnings("unchecked")
			Map<String, List<Object>> mvUserInfo = (Map<String, List<Object>>) request.getAttribute("org.jasig.portlet.USER_INFO_MULTIVALUED");
			
			if (mvUserInfo != null) {
				userInfo.init(mvUserInfo, this);
			}
			if (logger.isDebugEnabled()) {
				for (Entry<String, List<Object>> entry : mvUserInfo.entrySet()) {
					logger.debug("infos key: "+ entry.getKey());
					for (Object o : entry.getValue()) {
						logger.debug("infos values "+ o.toString());
					}
				}
			}
		}
		return true;
	}
	
	public UserInfoBean getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfoBean userInfo) {
		this.userInfo = userInfo;
	}

	public String getAttrMail() {
		return attrMail;
	}

	public void setAttrMail(String attrMail) {
		this.attrMail = attrMail;
	}

	public String getAttrUai() {
		return attrUai;
	}

	public void setAttrUai(String attrUai) {
		this.attrUai = attrUai;
	}

	public String getAttrAllUai() {
		return attrAllUai;
	}

	public void setAttrAllUai(String attrAllUai) {
		this.attrAllUai = attrAllUai;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrMemberOf() {
		return attrMemberOf;
	}

	public void setAttrMemberOf(String attrMemberOf) {
		this.attrMemberOf = attrMemberOf;
	}

	public String getMailForDebug() {
		return mailForDebug;
	}

	public void setMailForDebug(String mailForDebug) {
		this.mailForDebug = mailForDebug;
	}


}
