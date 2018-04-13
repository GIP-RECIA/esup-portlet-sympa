package org.esupportail.sympa.recia;

import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;

public class HomeController extends org.esupportail.sympa.portlet.web.controllers.HomeController {

	private String sendMailUrl;
	private UserInfoBean userInfo;
	
	@Override
	protected ModelAndView handleRender(RenderRequest request, RenderResponse response, Object command,
			BindException errors) throws Exception {
		
		ModelAndView res = super.handleRender(request, response, command, errors);
		String name = res.getViewName();
		if ("esupsympaWideView".equals(name)) {
			ModelMap map = res.getModelMap();
			map.put("sendMailUrl", sendMailUrl);
			map.put("userName", userInfo.getDisplayName());
			map.put("mail", userInfo.getMail());
			res.setViewName("esupsympaReciaView");
			
		}
		return res;
	}

	public String getSendMailUrl() {
		return sendMailUrl;
	}

	public void setSendMailUrl(String sendMailUrl) {
		this.sendMailUrl = sendMailUrl;
	}

	public UserInfoBean getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfoBean userInfo) {
		this.userInfo = userInfo;
	}

	
	
}
