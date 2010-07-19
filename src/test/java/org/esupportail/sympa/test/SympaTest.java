package org.esupportail.sympa.test;

import java.net.URL;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.axis.handlers.soap.SOAPService;
import org.apache.axis.transport.http.HTTPConstants;
import org.sympa.client.ws.axis.v544.ListType;
import org.sympa.client.ws.axis.v544.SympaPort_PortType;
import org.sympa.client.ws.axis.v544.SympaSOAP;
import org.sympa.client.ws.axis.v544.SympaSOAPLocator;
import org.sympa.client.ws.axis.v544.SOAPStub;

public class SympaTest extends TestCase {


	public void testSympa() throws Exception {
		
		Properties prop = new Properties();
		prop.load(ClassLoader.getSystemResourceAsStream("test.properties"));

		String testSympaUrl = prop.getProperty("testSympaUrl");
		String testSympaUsername = prop.getProperty("testSympaUsername");
		String testSympaPassword = prop.getProperty("testSympaPassword");
		
		System.out.println("testSympaUrl:" + testSympaUrl);
		System.out.println("testSympaUsername:" + testSympaUsername);
		System.out.println("testSympaPassword:" + testSympaPassword);
		
		SympaSOAP locator = new SympaSOAPLocator();
		((SympaSOAPLocator)locator).setMaintainSession(true); // mandatory for cookie after login
		
		URL sympaURL = new URL(testSympaUrl);
		SympaPort_PortType port = locator.getSympaPort(sympaURL);
		
		String cookie = port.login(testSympaUsername, testSympaPassword);
		System.out.println("cookie:" + cookie);
		// force keeping cookie
		((SOAPStub)port)._setProperty(HTTPConstants.HEADER_COOKIE,
			    "sympa_session=" + cookie);

		System.out.println("port.checkCookie():" + port.checkCookie());
		
		String[] simpleWhich = port.which();
		System.out.println(simpleWhich.length);
		ListType[] which = port.complexWhich();
		System.out.println(which.length);
		for ( ListType l : which ) {
			System.out.printf("%1$s %2$s %3$s\n",l.getListAddress(),l.getSubject(),l.getHomepage());
		}

	}

}
