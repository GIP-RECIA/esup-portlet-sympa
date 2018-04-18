package org.esupportail.sympa.recia;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ProxyTicketValidator;
import org.jasig.portlet.cas.CASProxyTicketServiceUserInfoImpl;
import org.jasig.portlet.cas.ICASProxyTicketService;
import org.springframework.beans.factory.InitializingBean;

public class CASProxyTicketService  implements ICASProxyTicketService, InitializingBean {
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private Cas20ProxyTicketValidator ticketValidator;
	
	private String serverNames;
	private String serviceUrlFormat;
	
	
	
	
	private Map<String, ICASProxyTicketService> serverName2proxyService ;
	
	public Assertion getProxyTicket(PortletRequest request) {
		String sn = request.getServerName();
		ICASProxyTicketService service = serverName2proxyService.get(sn);
		if (service == null) {
			log.error ("CASProxyTicketService not found for serverName=" + sn);
		} else {
			log.debug ("CASProxyTicketService found for  serverName=" + sn);
		}
		return service.getProxyTicket(request);
	}

	public String haveProxyTicket(PortletRequest request) {
		String sn = request.getServerName();
		ICASProxyTicketService service = serverName2proxyService.get(sn);
		if (service == null) {
			log.error ("CASProxyTicketService not found for serverName=" + sn);
		} else {
			log.debug ("CASProxyTicketService found for  serverName=" + sn);
		}
		return service.haveProxyTicket(request);
	}

	
	public String getCasServiceToken(Assertion assertion, String target) {
        final String proxyTicket = assertion.getPrincipal().getProxyTicketFor(target);
        if (proxyTicket == null){
            log.error("Failed to retrieve proxy ticket for assertion [" + assertion.toString() + "].  Is the PGT still valid?");
            return null;
        }
        if (log.isTraceEnabled()) {
            log.trace("returning from getCasServiceToken(), returning proxy ticket ["
                    + proxyTicket + "]");
        }
        return proxyTicket;
	}
	
	
	
	public void afterPropertiesSet() throws Exception {
		
		serverName2proxyService = new HashMap<String, ICASProxyTicketService>();
		
		for (String serverName : getServerNames().split("\\s+")) {
			String key = serverName.trim();
			String val = String.format(getServiceUrlFormat(), key);
			if (key != null && val != null) {
				CASProxyTicketServiceUserInfoImpl service = new CASProxyTicketServiceUserInfoImpl();
				service.setServiceUrl(val);
				service.setTicketValidator(getTicketValidator());
				serverName2proxyService.put(key, service);
			}
		}
	}

	public Cas20ProxyTicketValidator getTicketValidator() {
		return ticketValidator;
	}

	public void setTicketValidator(Cas20ProxyTicketValidator ticketValidator) {
		this.ticketValidator = ticketValidator;
	}

	public String getServerNames() {
		return serverNames;
	}

	public void setServerNames(String serverNames) {
		this.serverNames = serverNames;
	}

	public String getServiceUrlFormat() {
		return serviceUrlFormat;
	}

	public void setServiceUrlFormat(String serviceUrlFormat) {
		this.serviceUrlFormat = serviceUrlFormat;
	}

}
