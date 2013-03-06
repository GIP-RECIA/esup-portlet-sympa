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
