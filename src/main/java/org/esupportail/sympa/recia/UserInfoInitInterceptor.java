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


public class UserInfoInitInterceptor extends HandlerInterceptorAdapter {
	protected final Log logger = LogFactory.getLog(this.getClass());
	
	private UserInfoBean userInfo;
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
				userInfo.init(mvUserInfo);
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
}
