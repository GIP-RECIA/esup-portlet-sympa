package org.esupportail.sympa.util;

import org.springframework.util.StringUtils;

/**
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public abstract class UaiHelper {
	
	public static String replaceUai(final String unprocessedDomain, final String uai) {
		//To lower case (handles null)
		String lowerCaseUai = StringUtils.trimWhitespace(uai).toLowerCase();

		//only do the replacement if the uai contains non whitespace
		return StringUtils.replace(unprocessedDomain,
				"%UAI", StringUtils.trimWhitespace(lowerCaseUai) //if this is null, no replacement is made
				);

	}
}