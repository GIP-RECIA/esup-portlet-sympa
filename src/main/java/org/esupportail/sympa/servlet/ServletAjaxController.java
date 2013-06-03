/**
 * Copyright (C) 2010 INSA LYON http://www.insa-lyon.fr
 * Copyright (C) 2010 Esup Portail http://www.esup-portail.org
 * @Author (C) 2010 Olivier Franco <Olivier.Franco@insa-lyon.fr>
 * @Contributor (C) 2010 Doriane Dusart <Doriane.Dusart@univ-valenciennes.fr>
 * @Contributor (C) 2010 Jean-Pierre Tran <Jean-Pierre.Tran@univ-rouen.fr>
 * @Contributor (C) 2010 Vincent Bonamy <Vincent.Bonamy@univ-rouen.fr>
 * @Contributor (C) 2013 Maxime BOSSARD (GIP-RECIA) <mxbossard@gmail.com>
 *
 * Licensed under the GPL License, (please see the LICENCE file)
 */
package org.esupportail.sympa.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.esco.sympa.domain.groupfinder.IEtabGroupsFinder;
import org.esupportail.commons.services.smtp.SimpleSmtpServiceImpl;
import org.esupportail.commons.services.smtp.SmtpService;
import org.esupportail.commons.utils.Assert;
import org.esupportail.sympa.domain.listfinder.IDaoService;
import org.esupportail.sympa.domain.listfinder.model.Model;
import org.esupportail.sympa.domain.listfinder.model.ModelRequest;
import org.esupportail.sympa.domain.listfinder.model.ModelSubscribers;
import org.esupportail.sympa.domain.listfinder.model.PreparedRequest;
import org.esupportail.web.portlet.mvc.ReentrantFormController;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
@Scope("session")
public class ServletAjaxController implements InitializingBean {

	/** Base of error messages for list creation. */
	private static final String CREATE_ERROR_MSG_BASE = "esupsympaCreateList";

	/** Base of error messages for list modification. */
	private static final String UPDATE_ERROR_MSG_BASE = "esupsympaUpdateList";

	/** Base of error messages for list closing. */
	private static final String CLOSE_ERROR_MSG_BASE = "esupsympaCloseList";

	private final Logger log = Logger.getLogger(ServletAjaxController.class);

	@Autowired
	protected ApplicationContext context;

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected IDaoService daoService;

	@Autowired
	protected SmtpService smtp;

	/** Autowire the bean with Id jsTreeGroupsFinder. */
	@Autowired
	@Qualifier("jsTreeGroupsFinder")
	protected IEtabGroupsFinder jsTreeGroupFinder;

	@Resource(name="config")
	private Properties properties;

	private String defaultSympaRemoteEndpointUrl;

	private String defaultSympaRemoteDatabaseId;

	protected Locale locale;

	private Pattern operationPattern = Pattern.compile(".*operation=([^&]*).*");

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.context, "No app context injected !");
		Assert.notNull(this.request, "No HTTP request injected !");
		Assert.notNull(this.daoService, "No DAO service injected !");
		Assert.notNull(this.smtp, "No SMTP service injected !");
		Assert.notNull(this.jsTreeGroupFinder, "No JS tree group finder injected !");
		Assert.notNull(this.properties, "No properties injected !");

		this.request.setCharacterEncoding("UTF-8");
		this.locale = RequestContextUtils.getLocale(this.request);
	}

	/**
	 * @param establishementId the UAI, id of the establishment
	 * @param modelId The database id of the model
	 * @param listDescription Contains the model's subject/description with any option model parameter filled in
	 * @param modelParam The parameter of the model
	 * @param request http request
	 * @return Spring MVC ModelAndView
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/loadCreateList")
	public ModelAndView loadCreateList(final String establishementId,
			final String modelId,
			final String listDescription,
			final String modelParam,
			final HttpServletRequest request) {

		ModelMap modelMap = new ModelMap();

		if ((modelId == null) || (listDescription == null)) {
			//TODO the error page has an error...
			this.log.error("Model id or list description is null");
			//return new ModelAndView("error", modelMap);
		}

		Model model = this.daoService.getModel(new BigInteger(modelId));

		modelMap.put("listDescription", listDescription);

		ModelSubscribers modelSubscribers = this.daoService.getModelSubscriber(model);
		this.log.debug("Additional groups filter is " + modelSubscribers.getGroupFilter());
		modelMap.put("subscribersGroup", modelSubscribers.getGroupFilter());

		List<JsCreateListRow> editorsAliases = new ArrayList<JsCreateListRow>();

		List<PreparedRequest> listPreparedRequest = this.daoService.getAllPreparedRequests();

		for (PreparedRequest preparedRequest : listPreparedRequest) {
			JsCreateListRow row = new JsCreateListRow();
			ModelRequest modelRequest = this.daoService.getModelRequest(preparedRequest, model);
			switch(modelRequest.getCategoryAsEnum()) {
			case CHECKED:
				row.setChecked(true);
				row.setEditable(true);
				break;
			case UNCHECKED:
				row.setChecked(false);
				row.setEditable(true);
				break;
			case MANDATORY:
				row.setChecked(true);
				row.setEditable(false);
				break;
			}
			row.setName(preparedRequest.getDisplayName());
			row.setIdRequest(modelRequest.getIdRequest().toString());

			editorsAliases.add(row);
		}

		modelMap.put("editorsAliases", editorsAliases);
		modelMap.put("createListURLBase", this.defaultSympaRemoteEndpointUrl);
		modelMap.put("type", model.getModelName());

		Pattern p = Pattern.compile("\\{((?!UAI).*)\\}");
		Matcher m = p.matcher(model.getListname());

		if (m.find()) {
			modelMap.put("typeParamName", m.group(1));
			modelMap.put("typeParam", modelParam);
		}

		modelMap.put("uai", establishementId);

		StringBuilder userAttributes = new StringBuilder(128);
		Map<String, String> placeholderValuesMap = (Map<String, String>)
				request.getSession().getAttribute(ReentrantFormController.PLACEHOLDER_VALUES_MAP_SESSION_KEY);
		for (Entry<String, String> userAttribute : placeholderValuesMap.entrySet()) {
			userAttributes.append("&");
			userAttributes.append(userAttribute.getKey());
			userAttributes.append("=");
			userAttributes.append(userAttribute.getValue());
		}
		modelMap.put("userAttributes", userAttributes.toString());

		if (this.log.isDebugEnabled()) {
			this.log.debug("Model map content: " + modelMap.toString());
		}

		request.getSession().setAttribute("createListAdditionalGroupsCache", new HashMap<String, List<String>>());

		return new ModelAndView("admin/createMailingListForm", modelMap);
	}

	@RequestMapping("/doCreateList")
	public @ResponseBody String doCreateList(final String queryString, final HttpServletRequest request,
			final HttpServletResponse response) {

		try {
			// Get SympaRemote database Id
			final String sympaRemoteDatabaseId = this.retrieveSympaRemoteDatabaseId(request);
			final String queryStringWithDbId =  queryString + "&databaseId=" + sympaRemoteDatabaseId;
			 
			// Get SympaRemote endpoint URL
			final String sympaRemoteEndpointUrl = this.retrieveSympaRemoteEndpointUrl(request);
			this.log.debug("Connecting to SympaRemote with the url [" + sympaRemoteEndpointUrl + "]");
			URL uri = new URL(sympaRemoteEndpointUrl);

			URLConnection urlConnection = uri.openConnection();

			//Use POST to hit the SympaRemote web application
			urlConnection.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
			this.log.debug("Posting querystring [" + queryStringWithDbId + "]");
			//Send the queryString
			wr.write(queryStringWithDbId);
			wr.flush();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							urlConnection.getInputStream()));
			StringBuffer input = new StringBuffer();
			String inputLine;

			this.log.debug("create List response: ");
			while ((inputLine = in.readLine()) != null) {
				this.log.debug(inputLine);
				input.append(inputLine);
			}

			in.close();
			String errorCode = input.toString();

			//Match a regular expression to determine if this is an error code in the
			//form Digit,CODE
			Pattern p = Pattern.compile("(\\d),(.*)");
			Matcher m = p.matcher(errorCode);
			if (m.matches()) {
				String errorCodeNumber = m.group(1);
				String errorCodeText = m.group(2).toLowerCase();

				//***Remove any (s) from the error code as ( ) are not valid characters in a resource key***
				errorCodeText = errorCodeText.replaceAll(Pattern.quote("(s)"), "");
				String message = errorCodeText;

				final String baseErrorMsg = this.findErrorMessageBase(queryStringWithDbId);
				if (StringUtils.isNotBlank(baseErrorMsg)) {
					//Build a resource key in order to display a translated message
					String errorMessageKey = baseErrorMsg + ".failure."
							+ errorCodeNumber + "." + errorCodeText;

					message = this.context.getMessage(errorMessageKey, null, this.locale);
				}

				//0 means success, anything else, return an error code to let the ajax handler know something is amiss
				if (!errorCodeNumber.equals("0")) {
					response.setStatus(500);
				}

				return message;
			}


		} catch (MalformedURLException ex) {
			this.log.error("URL exception", ex);
		}  catch (IOException ex) {
			this.log.error("URL exception", ex);
		}

		return "Error";
	}

	@RequestMapping("/doCloseList")
	public @ResponseBody String doCloseList(final String queryString, final HttpServletRequest request, final HttpServletResponse response) {

		try {
			final String sympaRemoteEndpointUrl = this.retrieveSympaRemoteEndpointUrl(request);
			this.log.debug("Connecting to SympaRemote with the url [" + sympaRemoteEndpointUrl + "]");
			URL uri = new URL(sympaRemoteEndpointUrl);

			URLConnection urlConnection = uri.openConnection();

			//Use POST to hit the SympaRemote web application
			urlConnection.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
			this.log.debug("Posting querystring [" + queryString + "]");
			//Send the queryString
			wr.write(queryString);
			wr.flush();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							urlConnection.getInputStream()));
			StringBuffer input = new StringBuffer();
			String inputLine;

			this.log.debug("create List response: ");
			while ((inputLine = in.readLine()) != null) {
				this.log.debug(inputLine);
				input.append(inputLine);
			}

			in.close();
			String errorCode = input.toString();

			//Match a regular expression to determine if this is an error code in the
			//form Digit,CODE
			Pattern p = Pattern.compile("(\\d),(.*)");
			Matcher m = p.matcher(errorCode);
			if (m.matches()) {
				String errorCodeNumber = m.group(1);
				String errorCodeText = m.group(2).toLowerCase();

				//***Remove any (s) from the error code as ( ) are not valid characters in a resource key***
				errorCodeText = errorCodeText.replaceAll(Pattern.quote("(s)"), "");

				//Build a resource key in order to display a translated message
				String errorMessageKey = ServletAjaxController.CLOSE_ERROR_MSG_BASE + ".failure."
						+ errorCodeNumber + "." + errorCodeText;

				String message = this.context.getMessage(errorMessageKey, null, this.locale);

				//0 means success, anything else, return an error code to let the ajax handler know something is amiss
				if (!errorCodeNumber.equals("0")) {
					response.setStatus(500);
				}

				return message;
			}

		} catch (MalformedURLException ex) {
			this.log.error("URL exception", ex);
		}  catch (IOException ex) {
			this.log.error("URL exception", ex);
		}

		return "Error";
	}

	/**
	 * Send an email.
	 * This email is BCC sended to the sender.
	 * 
	 * @param fromAddress sender
	 * @param toAddress recipient
	 * @param subject subject
	 * @param message message content
	 * @param request HTTP request
	 * @param response HTTP rersponse
	 */
	@RequestMapping("/sendEmail")
	public void sendEmail(final String fromAddress, final String toAddress,
			final String subject, final String message, final HttpServletRequest request,
			final HttpServletResponse response) {
		// MBD: Ajout du choix du sujet du mail
		try {
			this.sendEmail(fromAddress, toAddress, subject, message);

			// MBD: Envoi d'un second mail au from pour qu'il est une trace dans sa boite mail.
			this.sendEmail(toAddress, fromAddress, subject, message);
		} catch (UnsupportedEncodingException ex) {
			this.log.warn(ex);
			response.setStatus(403);
			return;
		}

		//set status ok
		response.setStatus(200);
	}

	protected void sendEmail(final String fromAddress, final String toAddress, final String subject,
			final String message) throws UnsupportedEncodingException {
		InternetAddress[] tos = new InternetAddress[1];
		tos[0] = new InternetAddress(toAddress,  toAddress);

		InternetAddress from = new InternetAddress(fromAddress, fromAddress);
		InternetAddress[] bccs = new InternetAddress[0];

		if (this.smtp instanceof SimpleSmtpServiceImpl) {
			SimpleSmtpServiceImpl impl = (SimpleSmtpServiceImpl) this.smtp;
			impl.setFromAddress(from);
		}

		//smtp.send(tos, "-", "", message);

		if (StringUtils.isEmpty(subject)) {
			this.smtp.sendtocc(tos, null, bccs, "-", null, message, null);
		} else {
			this.smtp.sendtocc(tos, null, bccs, subject, null, message, null);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("/jstreeData")
	public @ResponseBody List<JsList> jstreeData(final String establishementId,
			@RequestParam(value = "selectedGroups", required = false) final String[] selectedGroups,
			final HttpServletRequest request) {

		List<String> additionalGroups = null;
		Map<String, List<String>> createListAdditionalGroupsCache = null;

		try {
			//First check if we have results cached
			createListAdditionalGroupsCache = (Map<String, List<String>>)
					request.getSession().getAttribute("createListAdditionalGroupsCache");

		} catch (Exception ex) {
			this.log.error(ex);
		}

		if ((createListAdditionalGroupsCache != null)
				&& createListAdditionalGroupsCache.containsKey(establishementId)) {
			additionalGroups = createListAdditionalGroupsCache.get(establishementId);
			this.log.debug("Fetched additional groups from cache, size: " + additionalGroups.size());
		}

		//if the list was not in the cache, then fetch them
		if (additionalGroups == null) {
			//Fetch the list of available lists

			// Construct user info map to call the groups finder.
			Map<String, String> userInfo = new HashMap<String, String>();
			String uaiUserPropertyKey = this.properties.getProperty("portal.attribute.uai");
			userInfo.put(uaiUserPropertyKey, establishementId);

			Collection<String> additionalGroupsColl = this.jsTreeGroupFinder.findGroupsOfEtab(userInfo );

			additionalGroups = new ArrayList<String>(additionalGroupsColl);
			Collections.sort(additionalGroups);

			//Stock in cache for later retrieval
			if (createListAdditionalGroupsCache != null) {
				createListAdditionalGroupsCache.put(establishementId, additionalGroups);
			}
		}

		//Convert strings in additionalGroups into a tree structure for the jsTree in the UI
		List<JsList> listsToCreate = new ArrayList<JsList>();

		for (String groupStr : additionalGroups) {

			if (ArrayUtils.contains(selectedGroups, groupStr)) {
				continue;
			}
			String[] levels = groupStr.split(":");
			int lastLevel = levels.length - 1;

			List<JsList> nodesForLevel = listsToCreate;

			for (int i = 0; i <= lastLevel; ++i) {
				String level = levels[i];
				JsList nodeForLevel = JsList.getMatchingNode(nodesForLevel, level);

				if (nodeForLevel == null) {
					nodeForLevel = new JsList();
					nodeForLevel.setData(level);
					nodesForLevel.add(nodeForLevel);
				}

				if (i == lastLevel) {
					nodeForLevel.getMetadata().put("groupName", groupStr);
					nodeForLevel.getAttr().put("rel", "group");
				} else {
					nodeForLevel.getAttr().put("rel", "folder");
				}

				//Set the html id of the nodes.  This is needed in order for the JSTree to work properly.  Must not contain special characters as jsTree does not handle them well.
				//As such, a hashcode is used which is unique enough and doesn't use special characters
				nodeForLevel.getAttr().put("id", "nodeId" + Integer.toString( (groupStr + i).hashCode() ));

				nodesForLevel = nodeForLevel.getChildren();
			}
		}

		return listsToCreate;
	}

	/**
	 * Retrieve the Sympa Remote endpoint URL from the HTTP session.
	 */
	protected String retrieveSympaRemoteEndpointUrl(final HttpServletRequest request) {
		String sympaRemoteEndpointUrl = (String) request.getSession().getAttribute(
				ReentrantFormController.SYMPA_REMOTE_ENDPOINT_URL_SESSION_KEY);

		if (StringUtils.isBlank(sympaRemoteEndpointUrl) ||
				"DEFAULT".equals(sympaRemoteEndpointUrl)) {
			sympaRemoteEndpointUrl = this.defaultSympaRemoteEndpointUrl;
		}

		return sympaRemoteEndpointUrl;
	}

	/**
	 * Retrieve the Sympa Remote endpoint URL from the HTTP session.
	 */
	protected String retrieveSympaRemoteDatabaseId(final HttpServletRequest request) {
		String sympaRemoteDatabaseId = (String) request.getSession().getAttribute(
				ReentrantFormController.SYMPA_REMOTE_DATABASE_ID_SESSION_KEY);

		if (StringUtils.isBlank(sympaRemoteDatabaseId) ||
				"DEFAULT".equals(sympaRemoteDatabaseId)) {
			sympaRemoteDatabaseId = this.defaultSympaRemoteDatabaseId;
		}

		return sympaRemoteDatabaseId;
	}

	protected String findErrorMessageBase(final String queryString) {
		String baseErrorMsg = null;
		Matcher opMatcher = this.operationPattern.matcher(queryString);
		if (opMatcher.find()) {
			final String operation = opMatcher.group(1);
			if ("CREATE".equals(operation)) {
				baseErrorMsg = ServletAjaxController.CREATE_ERROR_MSG_BASE;
			} else if ("UPDATE".equals(operation)) {
				baseErrorMsg = ServletAjaxController.UPDATE_ERROR_MSG_BASE;
			} else if ("CLOSE".equals(operation)) {
				baseErrorMsg = ServletAjaxController.CLOSE_ERROR_MSG_BASE;
			}
		}

		return baseErrorMsg;
	}

	/**
	 * @return the createListURLBase
	 */
	public String getDefaultSympaRemoteEndpointUrl() {
		return this.defaultSympaRemoteEndpointUrl;
	}

	/**
	 * @param createListURLBase the createListURLBase to set
	 */
	@Value("#{config['sympaRemote.defaultEndpointUrl']}")
	public void setDefaultSympaRemoteEndpointUrl(final String url) {
		this.defaultSympaRemoteEndpointUrl = url;
	}

	public String getDefaultSympaRemoteDatabaseId() {
		return defaultSympaRemoteDatabaseId;
	}

	@Value("#{config['sympaRemote.defaultDatabaseId']}")
	public void setDefaultSympaRemoteDatabaseId(String defaultSympaRemoteDatabaseId) {
		this.defaultSympaRemoteDatabaseId = defaultSympaRemoteDatabaseId;
	}

}
