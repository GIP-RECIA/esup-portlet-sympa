
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

import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.esupportail.commons.services.smtp.SimpleSmtpServiceImpl;
import org.esupportail.commons.services.smtp.SmtpService;
import org.esupportail.commons.utils.Assert;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Scope("session")
public class ServletAjaxController implements  InitializingBean, Serializable {

	/** Svuid. */
	private static final long serialVersionUID = -6975100227000700771L;


	private final Logger log = Logger.getLogger(ServletAjaxController.class);


	@Autowired
	protected SmtpService smtp;

	@Autowired
	protected UserInfoBean userInfoBean;

	public void afterPropertiesSet() throws Exception {

		Assert.notNull(this.smtp, "No SMTP service injected !");

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
		
		UserInfoBean uib = getUserInfoBean();
		
		if (uib == null || fromAddress == null || ! fromAddress.equals(uib.getMail())) {
			response.setStatus(403);
			this.log.warn("sendmail expéditeur invalide ");
			if (uib != null) {
				this.log.warn("               mail user =" + uib.getMail() );
				this.log.warn("                   From  =" + fromAddress );
			} else {
				this.log.warn("               userInfoBean = null " );
				this.log.warn("               mail From  =" + fromAddress );
			}
			return "session invalide : expéditeur invalide";
		}
		
		if (log.isDebugEnabled() ) {
			 
			log.debug("fromAddress " + fromAddress);
			log.debug("fromName " + fromName);
			log.debug("toAddress " +  toAddress);
			log.debug("subject " + subject);
			log.debug("message " + message); 
			
			String fromForDebug = uib.getMailForDebug() ;
			
			if (fromForDebug != null && !"".equals(fromForDebug.trim())) {
				fromAddress = fromForDebug;
				log.debug("FromAddress changé par " + fromAddress);
			
			} 
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

	public UserInfoBean getUserInfoBean() {
		return userInfoBean;
	}

	public void setUserInfoBean(UserInfoBean userInfoBean) {
		this.userInfoBean = userInfoBean;
	}
}
