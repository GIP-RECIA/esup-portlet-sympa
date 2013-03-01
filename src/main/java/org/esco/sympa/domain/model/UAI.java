package org.esco.sympa.domain.model;

import org.springframework.util.StringUtils;



public class UAI {
	private String uai;

	public UAI(final String uai) {
		this.uai = StringUtils.trimWhitespace(uai);
	}

	@Override
	public String toString() {
		return this.uai;
	}

	public static String replaceUai(final String unprocessedDomain, final String uai) {
		//To lower case (handles null)
		String lowerCaseUai = StringUtils.trimWhitespace(uai).toLowerCase();

		//only do the replacement if the uai contains non whitespace
		return StringUtils.replace(unprocessedDomain,
				"%UAI", StringUtils.trimWhitespace(lowerCaseUai) //if this is null, no replacement is made
				);

	}
}
