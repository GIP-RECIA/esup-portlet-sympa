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
package org.esupportail.sympa.domain.services.sympa;

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
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.impl.SympaRobot;
import org.esupportail.sympa.domain.services.sympa.ICredentialRetriever.TYPE;
import org.sympa.client.ws.axis.v544.SOAPStub;
import org.sympa.client.ws.axis.v544.SympaPort_PortType;
import org.sympa.client.ws.axis.v544.SympaSOAP;
import org.sympa.client.ws.axis.v544.SympaSOAPLocator;

public class SympaServerAxisWsImpl extends AbstractSympaServer {

	private int timeout = 5000;

	private String endPointUrl;

	// must be session scope so the bean of type SympaServerAxisWsImpl is session scope
	private Map<SympaRobot, SympaPort_PortType> portCache = new HashMap<SympaRobot, SympaPort_PortType>(8);

	@Override
	public List<UserSympaListWithUrl> getWhich(final SympaRobot robot) {
		// first of all; get a fresh new port if needed
		if(this.portCache.get(robot)!=null) {
			try {
				String checkCookie = this.portCache.get(robot).checkCookie();
				if((checkCookie == null) || "nobody".equals(checkCookie)) {
					this.portCache.put(robot, null);
				}
			} catch (RemoteException e) {
				this.logger.debug("port is no more usable, we reinitate it",e);
				this.portCache.put(robot, null);
			}
		}
		if(this.portCache.get(robot) == null) {
			try {
				this.portCache.put(robot, this.getPort(robot));
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
		if (this.portCache.get(robot) == null ) {
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
			whichList = this.portCache.get(robot).which();
		} catch (RemoteException e) {
			this.logger.error("complexWhich() failed !",e);
			return null;
		}
		List<UserSympaListWithUrl> result = new ArrayList<UserSympaListWithUrl>();
		if ( whichList != null ) {
			for (String l : whichList) {
				Map<String, String> listeInfos = SympaServerAxisWsImpl.stringToMap(l);
				UserSympaListWithUrl item = new UserSympaListWithUrl();
				item.setEditor(listeInfos.get("isEditor").equals("1"));
				item.setOwner(listeInfos.get("isOwner").equals("1"));
				item.setSubscriber(listeInfos.get("isSubscriber").equals("1"));
				item.setAddress(listeInfos.get("listAddress"));
				item.setHomepage(listeInfos.get("homepage"));
				item.setSubject(listeInfos.get("subject"));
				//  append various urls


				item.setListUrl(this.generateListUrl(robot, item.getHomepage()));
				item.setListAdminUrl(this.generateListAdminUrl(robot, item.getAddress()));
				item.setListArchivesUrl(this.generateListArchivesUrl(robot, item.getAddress()));
				result.add(item);
			}
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<UserSympaListWithUrl> getLists(final SympaRobot robot) {
		// first of all; get a fresh new port if needed
		if(this.portCache.get(robot)!=null) {
			try {
				String checkCookie = this.portCache.get(robot).checkCookie();
				if((checkCookie == null) || "nobody".equals(checkCookie)) {
					this.portCache.put(robot, null);
				}
			} catch (RemoteException e) {
				this.logger.debug("port is no more usable, we reinitate it",e);
				this.portCache.put(robot, null);
			}
		}
		if(this.portCache.get(robot) == null) {
			try {
				this.portCache.put(robot, this.getPort(robot));
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
		if (this.portCache.get(robot) == null ) {
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
			lists = this.portCache.get(robot).lists("", "");
		} catch (RemoteException e) {
			this.logger.error("lists() failed !",e);
			return null;
		}
		List<UserSympaListWithUrl> result = new ArrayList<UserSympaListWithUrl>();
		if ( lists != null ) {
			for (String l : lists) {
				Map<String, String> listeInfos = SympaServerAxisWsImpl.stringToMap(l);
				UserSympaListWithUrl item = new UserSympaListWithUrl();
				item.setAddress(listeInfos.get("listAddress"));
				item.setHomepage(listeInfos.get("homepage"));
				item.setSubject(listeInfos.get("subject"));
				result.add(item);
			}
		}
		return result;
	}

	private SympaPort_PortType getPort(final SympaRobot robot) throws MalformedURLException, ServiceException, RemoteException {
		SympaSOAP locator = new SympaSOAPLocator();
		((SympaSOAPLocator)locator).setMaintainSession(true); // mandatory for cookie after login
		final String endpointUrl = this.getEndPointUrl(robot);
		if (this.logger.isDebugEnabled()) {
			this.logger.debug(String.format("SympaSoap endpoint URL: [%1$s] for Robot: [%2$s].", endpointUrl, robot));
		}
		SympaPort_PortType port = locator.getSympaPort(new URL(endpointUrl));
		// set a timeout on port (10 seconds)
		((org.apache.axis.client.Stub)port).setTimeout(this.getTimeout());
		// now authenticate
		TYPE credsType = this.getCredentialRetriever().getType();
		SympaCredential creds = this.getCredentialRetriever().getCredentialForService(endpointUrl);
		if ( creds == null ) {
			this.logger.error("unable to retrieve credential for service " + endpointUrl);
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
	public String getEndPointUrl(final SympaRobot robot) {
		return robot.transformRobotUrl(this.endPointUrl);
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
