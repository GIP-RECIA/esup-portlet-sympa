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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.Assert;

/**
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class SympaRobot {

	private static final Pattern ENPOINT_URL_PATTERN =
			Pattern.compile("([^:]*://)([^/\\?]*)(/?.*)");

	private static final SympaRobot DEFAULT_ROBOT = new SympaRobot();
	static {
		SympaRobot.DEFAULT_ROBOT.defaultRobot = true;
	}

	private String domainName;

	private boolean defaultRobot = false;

	private SympaRobot() {
		super();
	}

	@Override
	public String toString() {
		return "SympaRobot [domainName=" + this.domainName + "]";
	}

	public SympaRobot(final String pDomainName) {
		super();

		Assert.hasText(pDomainName, "No domain name passed to build this robot !");
		this.domainName = pDomainName;
	}

	public static SympaRobot getDefaultRobot() {
		return SympaRobot.DEFAULT_ROBOT;

	}

	public String transformRobotUrl(final String serverUrl) {
		String result = serverUrl;

		if (!this.defaultRobot) {
			// If not the default robot
			Matcher m = SympaRobot.ENPOINT_URL_PATTERN.matcher(serverUrl);
			if (m.find()) {
				StringBuilder sb = new StringBuilder(serverUrl.length() * 2);
				sb.append(m.group(1));
				sb.append(this.domainName);
				sb.append(m.group(3));
				result = sb.toString();
			}
		}

		return result;
	}

	public String getDomainName() {
		return this.domainName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.domainName == null) ? 0 : this.domainName.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		SympaRobot other = (SympaRobot) obj;
		if (this.domainName == null) {
			if (other.domainName != null) {
				return false;
			}
		} else if (!this.domainName.equals(other.domainName)) {
			return false;
		}
		return true;
	}

}
