
package org.esupportail.sympa.recia;
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

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.esupportail.commons.services.smtp.SimpleSmtpServiceImpl;
import org.esupportail.commons.services.smtp.SmtpService;
import org.esupportail.commons.utils.Assert;
//import org.esupportail.sympa.util.UserInfoService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
@Scope("session")
public class ServletAjaxController implements InitializingBean, Serializable {

	/** Svuid. */
	private static final long serialVersionUID = -6975100227000700771L;

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
	protected SmtpService smtp;

	
	@Resource(name = "config")
	private Properties properties;

	private String defaultSympaRemoteEndpointUrl;

	private String defaultSympaRemoteDatabaseId;

	protected Locale locale;

	private Pattern operationPattern = Pattern.compile(".*operation=([^&]*).*");


	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.context, "No app context injected !");
		Assert.notNull(this.request, "No HTTP request injected !");
		Assert.notNull(this.smtp, "No SMTP service injected !");
		Assert.notNull(this.properties, "No properties injected !");

		this.request.setCharacterEncoding("UTF-8");
		this.locale = RequestContextUtils.getLocale(this.request);
	}

	/**
	 * Send an email. This email is BCC sended to the sender.
	 * 
	 * @param fromAddress
	 *            sender
	 * @param toAddress
	 *            recipient
	 * @param subject
	 *            subject
	 * @param message
	 *            message content
	 * @param request
	 *            HTTP request
	 * @param response
	 *            HTTP rersponse
	 */
	@RequestMapping(value = {"/sendEmail" }, method = RequestMethod.POST)
	@ResponseBody
	public String sendEmail(String fromAddress, final String fromName,  final String toAddress, final String subject, final String message,
				final HttpServletRequest request, final HttpServletResponse response) {
		
		log.debug("fromAddress " + fromAddress);
		log.debug("fromName " + fromName);
		log.debug("toAddress " +  toAddress);
		log.debug("subject " + subject);
		log.debug("message " + message); 
		
		if (log.isDebugEnabled()) {
			fromAddress = "pierre.legay@recia.fr";
		}
		
			try {
				this.sendEmail(fromAddress, fromName, toAddress, subject, message);
			} catch (UnsupportedEncodingException ex) {
				this.log.warn(ex);
				response.setStatus(403);
				return "";
			}
		
		// set status ok
		response.setStatus(200);
		return "";
	}
	
	
	protected void sendEmail(final String fromAddress, final String fromName, final String toAddress, final String subject,
			final String message) throws UnsupportedEncodingException {
		
		String sujet = subject != null ? subject.trim() : "";
		
		InternetAddress[] tos = new InternetAddress[1];
		tos[0] = new InternetAddress(toAddress, toAddress);
		
		
		InternetAddress from = new InternetAddress(fromAddress, fromName);
		InternetAddress[] bccs = new InternetAddress[1];
		bccs[0] = from;

		if (this.smtp instanceof SimpleSmtpServiceImpl) {
			SimpleSmtpServiceImpl impl = (SimpleSmtpServiceImpl) this.smtp;
			impl.setFromAddress(from);
		}

		// smtp.send(tos, "-", "", message);

		
		if ("".equals(sujet)) {
			sujet = "-";
		}
	
		this.smtp.sendtocc(tos, null, bccs, sujet, null, message, null);
		
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
	 * @param createListURLBase
	 *            the createListURLBase to set
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
