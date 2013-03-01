package org.esco.sympa.domain.model;

import org.springframework.util.StringUtils;


public class Domain {
	private String domain;

	public Domain(String domain) {
		if (domain == null) {
			domain = "";
		} else {
			this.domain = domain;
		}
	}

	@Override
	public String toString() {
		return this.domain;
	}

	public static String replaceDomain(final String unprocessedDomain, final String domain) {

		//only do the replacement if the uai contains non whitespace
		return StringUtils.replace(unprocessedDomain,
				"%DOMAIN", StringUtils.trimWhitespace(domain) //if this is null, no replacement is made
				);

	}
}
