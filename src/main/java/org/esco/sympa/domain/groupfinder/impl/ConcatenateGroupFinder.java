package org.esco.sympa.domain.groupfinder.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.esco.sympa.domain.groupfinder.IEtabGroupsFinder;
import org.springframework.util.CollectionUtils;

/**
 * Concatenate the results of multiple groups finders.
 * 
 * @author GIP - RECIA Maxime BOSSARD.
 *
 */
public class ConcatenateGroupFinder implements IEtabGroupsFinder {

	/** Configured via spring. */
	private List<IEtabGroupsFinder> groupsFinders;

	/** Constructor. */
	public ConcatenateGroupFinder() {

	}

	/** {@inheritDoc} */
	public Collection<String> findGroupsOfEtab(final Map<String, String> userInfo) {

		Collection<String> groups = new ArrayList<String>();
		if (!CollectionUtils.isEmpty(this.groupsFinders)) {
			for (IEtabGroupsFinder groupsFinder : this.groupsFinders) {
				Collection<String> etabGroupes = groupsFinder.findGroupsOfEtab(userInfo);
				if (etabGroupes != null) {
					groups.addAll(etabGroupes);
				}
			}
		}

		return groups;
	}

	/** Groups finders setter. */
	public void setGroupsFinders(final List<IEtabGroupsFinder> groupsFinders) {
		this.groupsFinders = groupsFinders;
	}

}
