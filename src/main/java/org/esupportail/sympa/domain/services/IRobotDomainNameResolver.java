/**
 * 
 */
package org.esupportail.sympa.domain.services;

/**
 * In charge of resolving the domain name of a Sympa Robot.
 * 
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public interface IRobotDomainNameResolver {

	/**
	 * Resolve de domain name of the robot to use.
	 * 
	 * @return null or the domain name
	 */
	String resolveRobotDomainName();
}
