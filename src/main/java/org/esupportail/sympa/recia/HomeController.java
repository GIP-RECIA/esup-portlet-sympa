package org.esupportail.sympa.recia;

import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.validation.BindException;
import org.springframework.web.portlet.ModelAndView;

public class HomeController extends org.esupportail.sympa.portlet.web.controllers.HomeController {

	@Override
	protected ModelAndView handleRender(RenderRequest request, RenderResponse response, Object command,
			BindException errors) throws Exception {
		// TODO Auto-generated method stub
		ModelAndView res = super.handleRender(request, response, command, errors);
		String name = res.getViewName();
		if ("esupsympaWideView".equals(name)) {
			res.setViewName("esupsympaReciaView");
			//return new ModelAndView("esupsympaReciaView", res.getModel());
		}
		return res;
	}

	
	
}
