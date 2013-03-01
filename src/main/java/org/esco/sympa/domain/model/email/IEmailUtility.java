package org.esco.sympa.domain.model.email;

import java.util.List;

/**
 * Configuration to an email utility (what will be used when
 * user sends an email).
 * 
 * @author Maxime BOSSARD - GIP RECIA 2012.
 *
 */
public interface IEmailUtility {

	/** Type (horde / simple email form). */	
	String getType();
	
	/** Type (horde / simple email form). */	
	void setType(String type);
	
	/** The patterns which will match the use of this email configuration. */
	void setPatterns(List<String> patterns);
	
	/** Url of the email utility. */
	String getUrl();
	
	/** Url of the email utility. */
	void setUrl(String pattern);
	
	/**
	 * Test if the EmailUtility is the one to choose.
	 * 
	 * @param profileWebMail chose field to match against.
	 * @return true if this is the correct EmailUtility.
	 */
	boolean isCorrectEmailUtility(String profileWebMail);
	
}
