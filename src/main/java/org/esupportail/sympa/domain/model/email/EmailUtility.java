package org.esupportail.sympa.domain.model.email;


import java.util.List;

/**
 * Configuration to an email utility (what will be used when
 * user sends an email).
 *
 */
/**
 * @author stephane
 *
 */
public class EmailUtility {
	//Type (horde / simple email form)	
	private String type;
	//The profiles which will use this email configuration
	private List<String> profiles;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<String> getProfiles() {
		return profiles;
	}
	public void setProfiles(List<String> profiles) {
		this.profiles = profiles;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private String url;
}
