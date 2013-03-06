/**
 * 
 */
package org.esupportail.sympa.domain.listfinder.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.esupportail.sympa.domain.listfinder.IAvailableListsFinder;
import org.esupportail.sympa.domain.listfinder.IExistingListsFinder;
import org.esupportail.sympa.domain.listfinder.IMailingList;
import org.esupportail.sympa.domain.listfinder.IMailingListModel;
import org.esupportail.sympa.domain.listfinder.model.BasicMailingList;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * Basic implementation of available list finder.
 * It build a collection of mailing list based on a collection of string configured.
 * 
 * @author GIP RECIA 2013 - Maxime BOSSARD.
 *
 */
public class BasicAvailableListFinder implements IAvailableListsFinder, InitializingBean {

	/** Lists already existing in Sympa. */
	private IExistingListsFinder existingListFinder;

	/** Strings representing a basic description of some creatable lists. */
	private Collection<String> creatableLists;

	/** {@inheritDoc} */
	public Collection<IMailingList> getAvailableAndNonExistingLists(final Map<String, String> userInfo,
			final Collection<IMailingListModel> modeles) {
		final Collection<IMailingList> availableLists = new HashSet<IMailingList>();

		if (!CollectionUtils.isEmpty(modeles)) {
			for (final IMailingListModel modele : modeles) {
				availableLists.addAll(this.buildMailingListCollection(modele, this.creatableLists));
			}
		}

		// Remove existing lists
		final Collection<String> existingLists = this.existingListFinder.findExistingLists(userInfo);
		if (!CollectionUtils.isEmpty(existingLists)) {
			final Iterator<IMailingList> avListIt = availableLists.iterator();
			while (avListIt.hasNext()) {
				// Remove all already created lists
				IMailingList creatableList = avListIt.next();

				if (existingLists.contains(creatableList.getName())) {
					avListIt.remove();
				}
			}
		}

		return availableLists;
	}

	/** {@inheritDoc} */
	public void setExistingListsFinder(final IExistingListsFinder existingListFinder) {
		this.existingListFinder = existingListFinder;
	}

	/** {@inheritDoc} */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.existingListFinder, "Existing list finder wasn't injected !");
		Assert.notEmpty(this.creatableLists, "No creatable lists configured !");

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

	public void setCreatableLists(final Collection<String> creatableLists) {
		this.creatableLists = creatableLists;
	}

}
