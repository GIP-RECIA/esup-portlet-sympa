package org.esco.sympa.domain.model.email;

import java.util.List;

/** Represents a list of possible email utilities. */
public class EmailConfiguration {
	private List<IEmailUtility> utils;

	public List<IEmailUtility> getUtils() {
		return utils;
	}

	public void setUtils(List<IEmailUtility> utils) {
		this.utils = utils;
	}
}
