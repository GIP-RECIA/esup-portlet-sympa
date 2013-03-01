package org.esco.sympa.domain.model.email;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.InitializingBean;

/**
 * Implementation matching pattern with a regexp.
 * 
 * @author Maxime BOSSARD - GIP RECIA 2012.
 *
 */
public class EmailUtilityRegexpMatch implements IEmailUtility, InitializingBean {
	
	/** Type (horde / simple email form). */	
	private String type;
	
	/** The patterns which will match the use of this email configuration. */
	private List<String> patterns;
	
	/** The patterns which will match the use of this email configuration. */
	private List<Pattern> patternsMatcher;
	
	/** Url of the email utility. */
	private String url;

	
	public EmailUtilityRegexpMatch() {
		super();
	}

	/** Compile the pattern list from the string format. */
	public void afterPropertiesSet() throws Exception {
		this.patternsMatcher = new ArrayList<Pattern>();
		
		if (this.patterns != null) {
			for (String s : this.patterns) {
				if (s != null) {
					this.patternsMatcher.add(Pattern.compile(s));
				}
			}
		}
		
	}
	
	public boolean isCorrectEmailUtility(final String profileWebMail) {
		Matcher m = null;
		
		if (this.patternsMatcher != null && profileWebMail != null) {
			for (Pattern p : this.patternsMatcher) {
				m = p.matcher(profileWebMail);
				if (m.matches()) {
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
