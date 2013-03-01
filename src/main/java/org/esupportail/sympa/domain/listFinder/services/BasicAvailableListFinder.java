/**
 * 
 */
package org.esupportail.sympa.domain.listFinder.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.esupportail.sympa.domain.listFinder.IAvailableListsFinder;
import org.esupportail.sympa.domain.listFinder.IExistingListsFinder;
import org.esupportail.sympa.domain.listFinder.IMailingList;
import org.esupportail.sympa.domain.listFinder.IMailingListModel;
import org.esupportail.sympa.domain.listFinder.model.BasicMailingList;
import org.esupportail.sympa.domain.listFinder.model.BasicMailingListModel;
import org.springframework.util.CollectionUtils;

/**
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class BasicAvailableListFinder implements IAvailableListsFinder {

	/** Lists already existing in Sympa. */
	private IExistingListsFinder existingListFinder;

	/** Strings representing a basic description of some creatable lists. */
	private Collection<String> creatableLists;

	/** {@inheritDoc} */
	public Collection<IMailingList> getAvailableAndNonExistingListsForEtab(final Map<String, String> userInfo,
			final Collection<IMailingListModel> modeles) {
		final Collection<IMailingList> availableLists = new HashSet<IMailingList>();

		Collection<String> existingLists = this.existingListFinder.findExistingLists(userInfo, domain);
		final Collection<String> availableParams;
		if (existingLists == null) {
			// No existing list => don't modify the list
			availableParams = this.creatableLists;
		} else {
			// We will modify the list
			availableParams = new HashSet<String>(this.creatableLists);
			for (final String existing : existingLists) {
				availableParams.remove(existing);
			}
		}
		if (!CollectionUtils.isEmpty(modeles)) {
			for (final IMailingListModel modele : modeles) {
				availableLists.addAll(this.buildMailingListCollection(modele, availableParams));
			}
		}

	}

	return availableLists;
}

/** {@inheritDoc} */
public void setExistingListsFinder(final IExistingListsFinder existingListFinder) {
	this.existingListFinder = existingListFinder;
}

/**
 * Build available mailing list base on the modele and the list of params.
 * 
 * @param modele
 * @param paramsList
 * @return the available mailing lists for the modele
 */
protected Collection<IMailingList> buildMailingListCollection(
		final IMailingListModel modele, final Collection<String> paramsList) {
	final Collection<IMailingList> availableLists = new HashSet<IMailingList>();

	final Pattern pattern = Pattern.compile(modele.getGroupPatternToMatch());

	if (!CollectionUtils.isEmpty(paramsList)) {
		for (String param : paramsList) {
			Matcher m = pattern.matcher(param);
			if (m.find()) {
				availableLists.add(new BasicMailingList(modele, param));
			}
		}
	}

	return availableLists;
}

public void setCreatableLists(final List<String> creatableLists) {
	this.creatableLists = creatableLists;
}

}
