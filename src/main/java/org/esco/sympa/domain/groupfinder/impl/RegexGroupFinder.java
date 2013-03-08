package org.esco.sympa.domain.groupfinder.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.esco.sympa.domain.groupfinder.IEtabGroupsFinder;
import org.esupportail.sympa.domain.model.UserAttributeMapping;

/**
 * Class used to filter a list of groups based on a regular expression.
 *
 */
public class RegexGroupFinder implements IEtabGroupsFinder {

	/** configured via spring with connection info and baseDN. */
	private IEtabGroupsFinder groupsFinder;

	/** [^:]*:Establishements:[^:]*_%UAI:*. */
	private String regularExpressionFilter;

	/** User attributes mapping. */
	private UserAttributeMapping userAttributeMapping;

	public Collection<String> findGroupsOfEtab(final Map<String, String> userInfo) {

		Collection<String> groups = this.groupsFinder.findGroupsOfEtab(userInfo);

		String regex = this.userAttributeMapping.substitutePlaceholder(this.regularExpressionFilter, userInfo);

		for (Iterator<String> iterator = groups.iterator(); iterator.hasNext();) {
			String name = iterator.next();

			if (!name.matches(regex)) {
				iterator.remove();
				continue;
			}
		}

		return groups;
	}

	public IEtabGroupsFinder getGroupsFinder() {
		return this.groupsFinder;
	}

	public void setGroupsFinder(final IEtabGroupsFinder groupsFinder) {
		this.groupsFinder = groupsFinder;
	}

	/**
	 * @return the regularExpressionFilter
	 */
	public String getRegularExpressionFilter() {
		return this.regularExpressionFilter;
	}

	/**
	 * @param regularExpressionFilter the regularExpressionFilter to set
	 */
	public void setRegularExpressionFilter(final String regularExpressionFilter) {
		this.regularExpressionFilter = regularExpressionFilter;
	}

	public UserAttributeMapping getUserAttributeMapping() {
		return this.userAttributeMapping;
	}

	public void setUserAttributeMapping(final UserAttributeMapping userAttributeMapping) {
		this.userAttributeMapping = userAttributeMapping;
	}

}
