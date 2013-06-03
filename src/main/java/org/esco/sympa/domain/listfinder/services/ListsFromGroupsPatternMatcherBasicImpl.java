package org.esco.sympa.domain.listfinder.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.esco.sympa.domain.listfinder.IListsFromGroupsPatternMatcher;
import org.esupportail.sympa.domain.listfinder.IMailingList;
import org.esupportail.sympa.domain.listfinder.IMailingListModel;
import org.esupportail.sympa.domain.listfinder.model.BasicMailingList;


/**
 * 
 * @see IListsFromGroupsPatternMatcher
 * 
 * @author GIP Recia
 *
 */
public class ListsFromGroupsPatternMatcherBasicImpl implements
IListsFromGroupsPatternMatcher {

	/** Logger */
	private final static Logger log = Logger.getLogger(ListsFromGroupsPatternMatcherBasicImpl.class);
	/** le pattern associe a cette instance du pattern matcher */
	private Pattern patternToMatch = null;


	/**
	 * @see IListsFromGroupsPatternMatcher#findPossibleListsWithModel(Collection, IMailingListModel)
	 */
	public Collection<IMailingList> findPossibleListsWithModel(
			final Collection<String> groups, final IMailingListModel listModel) {
		Collection<IMailingList> theLists = new ArrayList<IMailingList>();
		String casePattern = listModel.getGroupPatternToMatch();
		ListsFromGroupsPatternMatcherBasicImpl.log.debug("List model pattern is set to " + casePattern);
		this.patternToMatch = Pattern.compile(casePattern, Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		ListsFromGroupsPatternMatcherBasicImpl.log.debug("Groups to filter size " + groups.size());
		if (groups != null) {
			String argument = null;
			Iterator<String> itGroupsEtab = groups.iterator();
			if(itGroupsEtab != null) {
				while (itGroupsEtab.hasNext()) {
					String group = itGroupsEtab.next();
					Matcher matcher = this.patternToMatch.matcher(group);
					if (matcher.matches()) {
						// Si le group courant respecte le pattern, on ajoute la liste de diffusion
						// correspondante à la liste des listes possibles pour cet etablissement
						if (matcher.groupCount() == 1) {
							// Si un parametre a ete recuperer, il faudra utiliser ce parametre pour instancier le modele
							// (par exemple la classe ou le niveau correspondant)
							// group(0) = la chaine complete
							// group(1) = le parametre (= le premier groupe)
							argument = matcher.group(1);
						}
						IMailingList list = new BasicMailingList(listModel, argument);
						theLists.add(list);
						ListsFromGroupsPatternMatcherBasicImpl.log.debug("Group [" + group + "] matched");
						// Si aucun parametre n'est recupere, c'est qu'il n'y aura aucun parametre a passer au modele
					} else {
						ListsFromGroupsPatternMatcherBasicImpl.log.debug("Group [" + group + "] did not match");
					}
				}
			}
		}
		return theLists;
	}

}
