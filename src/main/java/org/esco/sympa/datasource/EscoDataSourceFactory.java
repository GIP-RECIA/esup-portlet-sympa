/**
 * 
 */
package org.esco.sympa.datasource;

import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.utils.Assert;
import org.esupportail.web.portlet.mvc.ReentrantFormController;
import org.springframework.aop.TargetSource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.portlet.context.PortletRequestAttributes;

/**
 * Factory which build datasource with settings found in portlet preferences.
 * This factory implements the TargetSource interface to works with the ProxyFactoryBean.
 * The factory will be embeded in a proxy DataSource and will be able to return the correct DataSource 
 * depending on the portlet preferences.
 * 
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class EscoDataSourceFactory implements TargetSource, InitializingBean {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(EscoDataSourceFactory.class);

	/** Datasource URL portlet preferences key. */
	private static final String DATASOURCE_URL_PREF = "databaseUrl";

	/** Default data source URL. */
	private String defaultUrl;
	
	/** Common driver class name. */
	private String driverClassName;
	
	/** Common username. */
	private String username;
	
	/** Common password. */
	private String password;
	
	/** DataSource cache. */
	private Map<String, DataSource> cache = new HashMap<String, DataSource>(8);

	@Override
	public Class<?> getTargetClass() {
		return SimpleDriverDataSource.class;
	}

	@Override
	public boolean isStatic() {
		return false;
	}

	@Override
	public Object getTarget() throws Exception {
		final String url = this.retrieveDataSourceUrl();
		
		DataSource dataSource = this.cache.get(url);
		if (dataSource == null) {
			dataSource = this.buildDataSource(url);
			this.cache.put(url, dataSource);
		} else if (LOG.isDebugEnabled()){
			LOG.debug("Retrieve datasource from cache for URL: [" + url + "].");
		}
		
		return dataSource;
	}

	@Override
	public void releaseTarget(Object target) throws Exception {
		// Nothing to do.
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(this.defaultUrl, "No default Url configured !");
		Assert.hasText(this.driverClassName, "No driver class name configured !");
		Assert.hasText(this.username, "No username configured !");
		Assert.hasText(this.password, "No password configured !");
	}
	
	@SuppressWarnings("unchecked")
	protected SimpleDriverDataSource buildDataSource(final String url) throws ClassNotFoundException {
		SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
		
		final Class<Driver> driverClass = (Class<Driver>) this.getClass().getClassLoader().loadClass(driverClassName);
		dataSource.setDriverClass(driverClass);
		dataSource.setUrl(url);
		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);
		
		if (LOG.isInfoEnabled()) {
			LOG.info("Data source built with URL: [" + url + "].");
		}
		
		return dataSource;
	}

	protected String retrieveDataSourceUrl() {
		String url = this.defaultUrl;
		
		final PortletRequest portletReq = retrievePortletRequest();
		
		if (portletReq != null) {
			final String tempUrl = portletReq.getPreferences().getValue(EscoDataSourceFactory.DATASOURCE_URL_PREF, this.defaultUrl);
			if (!"DEFAULT".equals(tempUrl)) {
				url = tempUrl;
			}
			
			// Register the URL in session with the portlet namespace
			this.registerDatSourceUrlForPortletNamespace(url, portletReq);
		} else {
			// Not a PortletRequest try to resolve the URL with the portlet namespace in session
			final String tempUrl = this.retrieveDataSourceUrlByPortletNamespace();
			if (StringUtils.hasText(tempUrl)) {
				url = tempUrl;
			}
		}

		return url;
	}

	protected String retrieveDataSourceUrlByPortletNamespace() {
		String url = null;
		
		final HttpServletRequest servletReq = this.retrieveServletRequest();
		if (servletReq != null) {
			final String portletNamespace = servletReq.getParameter(ReentrantFormController.PORTLET_NAMESPACE_KEY);
			if (portletNamespace != null) {
				// portletNamespace is a String
				final Object tempUrl = servletReq.getSession().getAttribute(portletNamespace);
				if (tempUrl != null) {
					url = tempUrl.toString();
					if (LOG.isDebugEnabled()){
						LOG.debug("Retrieve datasource url from session with portlet namespace: [" + portletNamespace + "].");
					}
				}
			}
		}
		return url;
	}

	protected void registerDatSourceUrlForPortletNamespace(String url, PortletRequest portletReq) {
		Object portletNamespace = portletReq.getAttribute(ReentrantFormController.PORTLET_NAMESPACE_KEY);
		// If the portlet namespace is correctly registered in the request
		if (portletNamespace != null) {
			// portletNamespace is a String
			portletReq.getPortletSession().setAttribute(portletNamespace.toString(), url);
		}
	}

	protected PortletRequest retrievePortletRequest() {
		PortletRequest portletReq = null;
		
		final RequestAttributes reqAttrs = RequestContextHolder.getRequestAttributes();
		if (reqAttrs != null && 
				PortletRequestAttributes.class.isAssignableFrom(reqAttrs.getClass())) {
			final PortletRequestAttributes portletReqAttrs = (PortletRequestAttributes) reqAttrs;
			
			portletReq = portletReqAttrs.getRequest();
		}
		
		return portletReq;
	}

	protected HttpServletRequest retrieveServletRequest() {
		HttpServletRequest servletReq = null;
		
		final RequestAttributes reqAttrs = RequestContextHolder.getRequestAttributes();
		if (reqAttrs != null && 
				ServletRequestAttributes.class.isAssignableFrom(reqAttrs.getClass())) {
			final ServletRequestAttributes servletReqAttrs = (ServletRequestAttributes) reqAttrs;
			
			servletReq = servletReqAttrs.getRequest();
		}
		
		return servletReq;
	}
	
	public String getDefaultUrl() {
		return defaultUrl;
	}

	public void setDefaultUrl(String defaultUrl) {
		this.defaultUrl = defaultUrl;
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
