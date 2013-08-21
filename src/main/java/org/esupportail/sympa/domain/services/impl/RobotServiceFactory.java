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
/**
 * 
 */
package org.esupportail.sympa.domain.services.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.sympa.domain.services.IDomainService;
import org.esupportail.sympa.domain.services.IRobotDomainNameResolver;
import org.esupportail.sympa.domain.services.sympa.AbstractSympaServer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Factory using the Request scope to retrieve the correct instance of IDomainService
 * 
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class RobotServiceFactory implements FactoryBean<IDomainService>, InitializingBean, Serializable {

	/** Svuid. */
	private static final long serialVersionUID = 975791216275931163L;

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(RobotServiceFactory.class);

	/** Sympa server used. */
	private AbstractSympaServer sympaServer;

	/** Robot domain name resolver. */
	private IRobotDomainNameResolver robotDomainNameResolver;

	/** Factory cache. */
	private Map<SympaRobot, IDomainService> cache = new HashMap<SympaRobot, IDomainService>(128);

	/**
	 * Retrieve the instance of IDomainService corresponding to this robot.
	 * 
	 * @param robot
	 * @return the correct IDomainService
	 */
	public IDomainService getSympaRobotService(final SympaRobot robot) {
		RobotServiceFactory.LOG.debug("Try to retrieve RobotDomainService from cache...");
		IDomainService service = this.cache.get(robot);

		if (service == null) {
			service = new RobotServiceImpl(this.sympaServer, robot);
			this.cache.put(robot, service);
			RobotServiceFactory.LOG.debug("RobotDomainService not found, new one was built and cached.");
		}

		return service;
	}

	/**
	 * Build a Sympa Robot with it's domain name.
	 * 
	 * @param domainName the domain name of the robot
	 * @return the Sympa Robot
	 */
	public SympaRobot buildSympaRobot(final String domainName) {
		SympaRobot robot = new SympaRobot(domainName);

		return robot;
	}

	@Override
	public IDomainService getObject() throws Exception {
		SympaRobot robot = SympaRobot.getDefaultRobot();

		RobotServiceFactory.LOG.debug("Resolving robot domain name...");
		final String domainName = this.robotDomainNameResolver.resolveRobotDomainName();
		if (RobotServiceFactory.LOG.isDebugEnabled()) {
			RobotServiceFactory.LOG.debug(
					String.format("Resolved robot domain name: [%1$s].", domainName));
		}

		if (StringUtils.hasText(domainName)) {
			robot = this.buildSympaRobot(domainName);
		}

		return this.getSympaRobotService(robot);
	}

	@Override
	public Class<?> getObjectType() {
		return RobotServiceImpl.class;
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.sympaServer, "No Sympa Server injected !");
		Assert.notNull(this.sympaServer, "No IRobotDomainNameResolver injected !");
	}

	public AbstractSympaServer getSympaServer() {
		return this.sympaServer;
	}

	public void setSympaServer(final AbstractSympaServer sympaServer) {
		this.sympaServer = sympaServer;
	}

	public IRobotDomainNameResolver getRobotDomainNameResolver() {
		return this.robotDomainNameResolver;
	}

	public void setRobotDomainNameResolver(final IRobotDomainNameResolver robotDomainNameResolver) {
		this.robotDomainNameResolver = robotDomainNameResolver;
	}

}
