/**
 * 
 */
package org.esco.sympa.portlet.web.interceptors;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.springframework.web.portlet.handler.HandlerInterceptorAdapter;

/**
 * Interceptor which put the portlet namespace in a request attribute for later use. 
 * 
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class PortletNamespaceHandlerInterceptor extends HandlerInterceptorAdapter {

	/** Key of portlet namespace. */
	public static final String PORTLET_NAMESPACE_KEY = "portletNamespace";

	protected void processPortletNamespace(final RenderRequest request, final RenderResponse response) {
		// The response namespace allow us to distinguish the portlet instance used.
		final String namespace = response.getNamespace();
		// Put the response namespace in the request to be able to identify the request
		request.setAttribute(PORTLET_NAMESPACE_KEY, namespace);
	}

	@Override
	public boolean preHandleRender(RenderRequest request, RenderResponse response, Object handler) throws Exception {
		this.processPortletNamespace(request, response);
		return true;
	}

}
