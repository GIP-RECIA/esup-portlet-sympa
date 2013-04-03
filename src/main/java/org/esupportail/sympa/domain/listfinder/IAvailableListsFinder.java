/**
 * Copyright (C) 2010 INSA LYON http://www.insa-lyon.fr
 * Copyright (C) 2010 Esup Portail http://www.esup-portail.org
 * @Author (C) 2010 Olivier Franco <Olivier.Franco@insa-lyon.fr>
 * @Contributor (C) 2010 Doriane Dusart <Doriane.Dusart@univ-valenciennes.fr>
 * @Contributor (C) 2010 Jean-Pierre Tran <Jean-Pierre.Tran@univ-rouen.fr>
 * @Contributor (C) 2010 Vincent Bonamy <Vincent.Bonamy@univ-rouen.fr>
 * @Contributor (C) 2013 Maxime BOSSARD (GIP-RECIA) <mxbossard@gmail.com>
 *
 * Licensed under the GPL License, (please see the LICENCE file)
 */
package org.esupportail.sympa.domain.listfinder;

import java.util.Collection;
import java.util.Map;

import org.esupportail.sympa.domain.listfinder.model.AvailableMailingListsFound;

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
	public AvailableMailingListsFound getAvailableAndNonExistingLists(Map<String,String> userInfo, Collection<IMailingListModel> modeles);

}
