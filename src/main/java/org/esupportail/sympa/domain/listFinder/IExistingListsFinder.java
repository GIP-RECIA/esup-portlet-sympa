package org.esupportail.sympa.domain.listFinder;

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
	 * Retourne une collection contenant les noms des groupes de l'etablissement.
	 * 
	 * @param userInfo les informations utilisateur
	 * @param domain Le domaine de liste de base (exemple : list.example.com)
	 * @return collection contenant les noms des listes existantes au niveau de sympa pour l'etablissement
	 */
	Collection<String> findExistingLists(Map<String,String> userInfo, String domain);
}
