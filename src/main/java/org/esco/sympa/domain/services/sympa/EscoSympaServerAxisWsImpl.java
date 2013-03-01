/**
 * Copyright (C) 2010 INSA LYON http://www.insa-lyon.fr
 * Copyright (C) 2010 Esup Portail http://www.esup-portail.org
 * @Author (C) 2010 Olivier Franco <Olivier.Franco@insa-lyon.fr>
 * @Contributor (C) 2010 Doriane Dusart <Doriane.Dusart@univ-valenciennes.fr>
 * @Contributor (C) 2010 Jean-Pierre Tran <Jean-Pierre.Tran@univ-rouen.fr>
 * @Contributor (C) 2010 Vincent Bonamy <Vincent.Bonamy@univ-rouen.fr>
 *
 * Licensed under the GPL License, (please see the LICENCE file)
 */

package org.esco.sympa.domain.services.sympa;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.rpc.ServiceException;

import org.apache.axis.transport.http.HTTPConstants;
import org.esco.sympa.domain.model.Domain;
import org.esco.sympa.domain.model.UAI;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.sympa.ICredentialRetriever.TYPE;
import org.esupportail.sympa.domain.services.sympa.SympaCredential;
import org.sympa.client.ws.axis.v544.SOAPStub;
import org.sympa.client.ws.axis.v544.SympaPort_PortType;
import org.sympa.client.ws.axis.v544.SympaSOAP;
import org.sympa.client.ws.axis.v544.SympaSOAPLocator;

public class EscoSympaServerAxisWsImpl extends AbstractEscoSympaServer {

	private int timeout = 5000;

	private String endPointUrl;

	// must be session scope so the bean of type SympaServerAxisWsImpl is session scope
	private SympaPort_PortType port = null;

	@Override
	public List<UserSympaListWithUrl> getWhich(final UAI uai, final Domain domain) {
		// first of all; get a fresh new port if needed
		if(this.port!=null) {
			try {
				String checkCookie = this.port.checkCookie();
				if((checkCookie == null) || "nobody".equals(checkCookie)) {
					this.port = null;
				}
			} catch (RemoteException e) {
				this.logger.debug("port is no more usable, we reinitate it",e);
				this.port = null;
			}
		}
		if(this.port == null) {
			try {
				this.port = this.getPort(uai, domain);
			} catch (MalformedURLException e) {
				this.logger.error("unable to get a new SympaPort_PortType",e);
				return null;
			} catch (ServiceException e) {
				this.logger.error("unable to get a new SympaPort_PortType",e);
				return null;
			} catch (RemoteException e) {
				this.logger.error("unable to get a new SympaPort_PortType",e);
				return null;
			}
		}
		if (this.port == null ) {
			this.logger.error("unable to get a new SympaPort_PortType");
			return null;
		}
		// do the which
		//ListType[] whichList = null;
		String[] whichList = null;
		try {
			/* BUG
			 *    """org.xml.sax.SAXException:  No deserializer for {http://www.w3.org/2001/XMLSchema}anyType"""
			 * with Axis
			 * so we use port.which() ...
			whichList = SympaPort_PortType.complexWhich();
			 */
			whichList = this.port.which();
		} catch (RemoteException e) {
			this.logger.error("complexWhich() failed !",e);
			return null;
		}
		List<UserSympaListWithUrl> result = new ArrayList<UserSympaListWithUrl>();
		if ( whichList != null ) {
			for (String l : whichList) {
				Map<String, String> listeInfos = EscoSympaServerAxisWsImpl.stringToMap(l);
				UserSympaListWithUrl item = new UserSympaListWithUrl();
				item.setEditor(listeInfos.get("isEditor").equals("1"));
				item.setOwner(listeInfos.get("isOwner").equals("1"));
				item.setSubscriber(listeInfos.get("isSubscriber").equals("1"));
				item.setAddress(listeInfos.get("listAddress"));
				item.setHomepage(listeInfos.get("homepage"));
				item.setSubject(listeInfos.get("subject"));
				//  append various urls


				item.setListUrl(this.generateListUrl(item.getHomepage()));
				item.setListAdminUrl(this.generateListAdminUrl(item.getAddress(), uai, domain));
				item.setListArchivesUrl(this.generateListArchivesUrl(item.getAddress(), uai, domain));
				result.add(item);
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see org.esupportail.sympa.domain.services.sympa.AbstractSympaServer#getLists(org.esupportail.sympa.domain.services.sympa.UAI, org.esupportail.sympa.domain.services.sympa.Domain)
	 */
	@Override
	public List<UserSympaListWithUrl> getLists(final UAI uai, final Domain domain) {
		// first of all; get a fresh new port if needed
		if(this.port!=null) {
			try {
				String checkCookie = this.port.checkCookie();
				if((checkCookie == null) || "nobody".equals(checkCookie)) {
					this.port = null;
				}
			} catch (RemoteException e) {
				this.logger.debug("port is no more usable, we reinitate it",e);
				this.port = null;
			}
		}
		if(this.port == null) {
			try {
				this.port = this.getPort(uai, domain);
			} catch (MalformedURLException e) {
				this.logger.error("unable to get a new SympaPort_PortType",e);
				return null;
			} catch (ServiceException e) {
				this.logger.error("unable to get a new SympaPort_PortType",e);
				return null;
			} catch (RemoteException e) {
				this.logger.error("unable to get a new SympaPort_PortType",e);
				return null;
			}
		}
		if (this.port == null ) {
			this.logger.error("unable to get a new SympaPort_PortType");
			return null;
		}
		// do the which
		//ListType[] whichList = null;
		String[] lists = null;
		try {
			/* BUG
			 *    """org.xml.sax.SAXException:  No deserializer for {http://www.w3.org/2001/XMLSchema}anyType"""
			 * with Axis
			 * so we use port.which() ...
			whichList = SympaPort_PortType.complexWhich();
			 */
			lists = this.port.lists("", "");
		} catch (RemoteException e) {
			this.logger.error("lists() failed !",e);
			return null;
		}
		List<UserSympaListWithUrl> result = new ArrayList<UserSympaListWithUrl>();
		if ( lists != null ) {
			for (String l : lists) {
				Map<String, String> listeInfos = EscoSympaServerAxisWsImpl.stringToMap(l);
				UserSympaListWithUrl item = new UserSympaListWithUrl();
				item.setAddress(listeInfos.get("listAddress"));
				item.setHomepage(listeInfos.get("homepage"));
				item.setSubject(listeInfos.get("subject"));
				result.add(item);
			}
		}
		return result;
	}

	private SympaPort_PortType getPort(final UAI uai, final Domain domain) throws MalformedURLException, ServiceException, RemoteException {
		SympaSOAP locator = new SympaSOAPLocator();
		((SympaSOAPLocator)locator).setMaintainSession(true); // mandatory for cookie after login

		String processedEndPointURL = this.getEndPointUrl();

		//Replace potential variables in the soap url
		if (uai != null) {
			processedEndPointURL = UAI.replaceUai(processedEndPointURL, uai.toString());
		}
		if (domain != null) {
			processedEndPointURL = Domain.replaceDomain(processedEndPointURL, domain.toString());
		}
		this.logger.debug("Fetching sympa URL with " + processedEndPointURL);

		SympaPort_PortType port = locator.getSympaPort(new URL(processedEndPointURL));
		// set a timeout on port (10 seconds)
		((org.apache.axis.client.Stub)port).setTimeout(this.getTimeout());
		// now authenticate
		TYPE credsType = this.getCredentialRetriever().getType();
		SympaCredential creds = this.getCredentialRetriever().getCredentialForService(processedEndPointURL);
		if ( creds == null ) {
			this.logger.error("unable to retrieve credential for service "+processedEndPointURL);
			return null;
		}
		switch ( credsType ) {
		case cas:
			String tmp = port.casLogin(creds.getPassword());
			((SOAPStub)port)._setProperty(HTTPConstants.HEADER_COOKIE,
					"sympa_session=" + tmp);
			if ( this.logger.isDebugEnabled() ) {
				this.logger.debug("CAS authentication ok : "+tmp);
			}
			break;
		case password:
			String tmp2 = port.login(creds.getId(), creds.getPassword());
			((SOAPStub)port)._setProperty(HTTPConstants.HEADER_COOKIE,
					"sympa_session=" + tmp2);
			if ( this.logger.isDebugEnabled() ) {
				this.logger.debug("PASSWORD authentication ok : "+tmp2);
			}
			break;
		case trusted:
			this.logger.info("TODO; must use authenticateAndRun ....");
			break;
		}
		return port;
	}

	/**
	 * @return the timeout
	 */
	public int getTimeout() {
		return this.timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the endPointUrl
	 */
	public String getEndPointUrl() {
		return this.endPointUrl;
	}

	/**
	 * @param endPointUrl the endPointUrl to set
	 */
	public void setEndPointUrl(final String endPointUrl) {
		this.endPointUrl = endPointUrl;
	}



	protected static Map<String, String> stringToMap(final String input) {
		Map<String, String> map = new HashMap<String, String>();

		String[] nameValuePairs = input.split(";");
		for (String nameValuePair : nameValuePairs) {
			String[] nameValue = nameValuePair.split("=");
			try {
				map.put(URLDecoder.decode(nameValue[0], "UTF-8"), nameValue.length > 1 ? URLDecoder.decode(
						nameValue[1], "UTF-8") : "");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException("This method requires UTF-8 encoding support", e);
			}
		}

		return map;
	}

}
