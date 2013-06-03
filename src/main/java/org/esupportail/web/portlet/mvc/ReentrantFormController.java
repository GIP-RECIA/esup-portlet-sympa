package org.esupportail.web.portlet.mvc;

import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.sympa.portlet.services.UserAgentInspector;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.handler.PortletSessionRequiredException;
import org.springframework.web.portlet.mvc.AbstractCommandController;

/**
 * @author ofranco
 * Override referenceData
 * Override initBinderFor Custom data binding
 * Override handleAction
 * Override
 */
public class ReentrantFormController extends AbstractCommandController {

	/** Session key of the placeholder values map. */
	public static final String PLACEHOLDER_VALUES_MAP_SESSION_KEY =
			"UserAttributeMapping.PLACEHOLDER_VALUES_MAP_SESSION_KEY";

	/** Session key of the placeholder values map. */
	public static final String SYMPA_REMOTE_ENDPOINT_URL_SESSION_KEY =
			"SYMPA_REMOTE_ENDPOINT_URL_SESSION_KEY";

	/** Session key of the database Id. */
	public static final String SYMPA_REMOTE_DATABASE_ID_SESSION_KEY =
			"SYMPA_REMOTE_DATABASE_ID_SESSION_KEY";

	private Log logger = LogFactory.getLog(ReentrantFormController.class);

	private String viewName;

	protected UserAgentInspector userAgentInspector;

	private final String ESUPSYMPA_WIDE_VIEW = "esupsympaWideView";
	private final String ESUPSYMPA_NARROW_VIEW = "esupsympaNarrowView";
	private final String ESUPSYMPA_MOBILE_VIEW = "esupsympaMobileView";

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.AbstractCommandController#handleRender(javax.portlet.RenderRequest, javax.portlet.RenderResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected ModelAndView handleRender(final RenderRequest request,
			final RenderResponse response, final Object command, final BindException errors)
					throws Exception {
		// Fetch errors model as starting point, containing form object under
		// "commandName", and corresponding Errors instance under internal key.
		Map model = errors.getModel();

		// Merge reference data into model, if any.
		Map referenceData = this.referenceData(request, errors.getTarget(), errors);
		if (referenceData != null) {
			model.putAll(referenceData);
		}

		final String sympaRemoteEndpointUrl = request.getPreferences().getValue(
				ReentrantFormController.SYMPA_REMOTE_ENDPOINT_URL_SESSION_KEY, "DEFAULT");
		request.getPortletSession().setAttribute(ReentrantFormController.SYMPA_REMOTE_ENDPOINT_URL_SESSION_KEY,
				sympaRemoteEndpointUrl, javax.portlet.PortletSession.APPLICATION_SCOPE);

		boolean isListAdmin = (Boolean) model.get("isListAdmin") ;

		// Merge control attributes into model, if any
		/*.
		if (controlModel != null) {
			model.putAll(controlModel);
		}*/
		// Trigger rendering of the specified view, using the final model.
		//return new ModelAndView(getViewName(), model);

		ModelAndView mv = null;

		if(this.userAgentInspector.isMobile(request)) {
			mv = new ModelAndView(this.ESUPSYMPA_MOBILE_VIEW, model);
		} else {
			WindowState state = request.getWindowState();
			if (WindowState.MAXIMIZED.equals(state)) {
				mv = new ModelAndView(this.ESUPSYMPA_WIDE_VIEW, model);
			} else {
				mv = new ModelAndView(this.ESUPSYMPA_NARROW_VIEW, model);
			}

			mv.addObject("listAdmin", isListAdmin);
		}
		
		return mv;
	}

	@SuppressWarnings("rawtypes")
	public Map referenceData(final PortletRequest request, final Object command, final Errors errors) throws Exception {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.BaseCommandController#getCommand(javax.portlet.PortletRequest)
	 * will be called ONCE in AbstractCommandController
	 */
	@Override
	protected Object getCommand(final PortletRequest request) throws Exception {
		// maybe call super.getCommand for intanciating commanClass !
		//super.getCommand(request);
		if ( this.logger.isDebugEnabled() ) {
			this.logger.debug("entering getCommand ");
		}
		PortletSession session = request.getPortletSession(false);
		if (session == null) {
			throw new PortletSessionRequiredException("Could not obtain portlet session");
		}
		Object command = session.getAttribute(this.getRenderCommandSessionAttributeName());
		if (command != null) {
			if ( this.logger.isDebugEnabled() ) {
				this.logger.debug("having command from session ...");
			}
			return this.alterCommand(command,request);
		}
		if ( this.logger.isDebugEnabled() ) {
			this.logger.debug("generating new command ...");
		}
		return this.newCommand(request);
	}

	public Object alterCommand(final Object command, final PortletRequest request) throws Exception {
		return command;
	}

	public Object newCommand(final PortletRequest request) throws Exception {
		return super.createCommand();
	}
	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.BaseCommandController#initBinder(javax.portlet.PortletRequest, org.springframework.web.portlet.bind.PortletRequestDataBinder)
	 */
	/*
	@Override
	protected void initBinder(PortletRequest request,
			PortletRequestDataBinder binder) throws Exception {
		// register cutome editor form "date"
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat,true,10));
		// TODO ? skip pageNumer and maxRecordPerPage
		//binder.
	}*/

	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.BaseCommandController#getRenderCommandSessionAttributeName()
	 */
	@Override
	protected String getRenderCommandSessionAttributeName() {
		String attr = this.getClass().getName();
		attr += ".command";
		if ( this.logger.isDebugEnabled() ) {
			this.logger.debug("render Command session attribute name = "+attr);
		}
		return attr;
	}
	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.mvc.BaseCommandController#getRenderErrorsSessionAttributeName()
	 */
	@Override
	protected String getRenderErrorsSessionAttributeName() {
		String attr = this.getClass().getName();
		attr += ".errors";
		if ( this.logger.isDebugEnabled() ) {
			this.logger.debug("render Error session attribute name = "+attr);
		}
		return attr;
	}
	/**
	 * @return the viewName
	 */
	public String getViewName() {
		return this.viewName;
	}
	/**
	 * @param viewName the viewName to set
	 */
	public String setViewName(final String viewName) {
		return this.viewName = viewName;
	}

	@Override
	protected void handleAction(final ActionRequest request, final ActionResponse response,
			final Object command, final BindException bindException) throws Exception {
		this.handleActionRequest(request,response,command,bindException);
	}

	public void handleActionRequest(final ActionRequest request, final ActionResponse response,
			final Object command, final BindException bindException) throws Exception {

	}

	public void setUserAgentInspector(final UserAgentInspector userAgentInspector) {
		this.userAgentInspector = userAgentInspector;
	}

}
