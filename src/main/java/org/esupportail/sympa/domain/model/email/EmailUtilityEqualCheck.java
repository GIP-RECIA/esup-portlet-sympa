package org.esupportail.sympa.domain.model.email;


import java.util.List;

/**
 * Implementation checking patterns with an ingoring case equal.
 * 
 * @author Maxime BOSSARD - GIP RECIA 2012.
 *
 */
public class EmailUtilityEqualCheck implements IEmailUtility {
	
	/** Type (horde / simple email form). */	
	private String type;
	
	/** The patterns which will match the use of this email configuration. */
	private List<String> patterns;
	
	/** Url of the email utility. */
	private String url;

	
	public EmailUtilityEqualCheck() {
		super();
	}

	public boolean isCorrectEmailUtility(final String profileWebMail) {
		if (this.getPatterns() != null) {
			for (String pattern : this.getPatterns()) {
				if (pattern != null && pattern.equalsIgnoreCase(profileWebMail)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public String getType() {
		return type;
	}

	public void setType(final String type) {
		this.type = type;
	}

	public List<String> getPatterns() {
		return patterns;
	}

	public void setPatterns(final List<String> patterns) {
		this.patterns = patterns;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(final String url) {
		this.url = url;
	}

}
