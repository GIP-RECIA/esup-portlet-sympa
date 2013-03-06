package org.esupportail.sympa.domain.listfinder;

import java.util.Collection;
import java.util.Map;

/**
 * 
 * Interface definissant l'objet capable de determiner quelles sont
 * les listes de diffusion pouvant etre crees par l'administrateur.
 * 
 * @author Maxime BOSSARD - Gip Recia 2013
 * 
 */
public interface IAvailableListsFinder {

	/**
	 *  Permet de fournir le mecanisme de recuperation des listes existantes.
	 * 
	 *  @param existingListFinder le mecanisme de recuperation des listes existantes
	 */
	public void setExistingListsFinder(IExistingListsFinder existingListFinder);

	/**
	 * Methode retournant le resultat final, c'est a dire la liste des mailing lists
	 * qu'il est possible de creer dans l'interface, en s'assurant de ne pas proposer celles deja existantes.
	 * 
	 * @param userInfo les informations utilisateur
	 * @param modeles les modeles de listes connus
	 * @return une collection de mailing lists
	 */
	public Collection<IMailingList> getAvailableAndNonExistingLists(Map<String,String> userInfo, Collection<IMailingListModel> modeles);
}
