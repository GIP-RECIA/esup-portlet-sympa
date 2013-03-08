package org.esco.sympa.domain.listfinder;

import java.util.Collection;

import org.esupportail.sympa.domain.listfinder.IMailingList;
import org.esupportail.sympa.domain.listfinder.IMailingListModel;

/**
 * 
 * Interface definissant le mecanisme qui permet de deduire (par pattern matching)
 * les mailing lists qu'il est possible de creer, en fonction des groupes de l'etablissement,
 * pour un model de liste donne
 * 
 * @author GIP Recia
 *
 */
public interface IListsFromGroupsPatternMatcher {

	/**
	 * Trouve, a partir des groupes de l'etablissement, toute les listes
	 * qu'il est possible de creer avec un modele donne
	 * @param groups la liste des groupes de l'etablissement concerne
	 * @param model le model concerne
	 * @return une liste de mailing lists (toute les listes
	 * qu'il est possible de creer avec ce modele, pour l'etablissement)
	 */
	public Collection<IMailingList> findPossibleListsWithModel(Collection<String> groups, IMailingListModel model);
}
