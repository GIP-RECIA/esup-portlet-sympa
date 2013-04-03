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

/**
 * 
 * @author GIP Recia
 *
 * Interface definissant le mecanisme capable de recuperer toutes les listes existantes
 * (en interrogeant le robot sympa concerne via le webservice sympa par exemple...)
 */
public interface IExistingListsFinder {

	/**
	 * Retourne une collection contenant les noms des listes existantes.
	 * Le nom de la liste est situé à gauche du @.
	 * Pour une liste "liste1@recia.fr" le nom de la liste est "liste1".
	 * 
	 * @param userInfo les informations utilisateur
	 * @return collection contenant les noms des listes existantes au niveau de sympa pour l'etablissement
	 */
	Collection<String> findExistingLists(Map<String,String> userInfo);
}
