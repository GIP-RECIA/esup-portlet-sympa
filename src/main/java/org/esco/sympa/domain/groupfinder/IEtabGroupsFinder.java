package org.esco.sympa.domain.groupfinder;

import java.util.Collection;
import java.util.Map;

/**
 * Interface definissant le mecanisme capable de recuperer tous les groupes
 * de l'etablissement concerne, en utilisant les infos passes en parametre
 * 
 * @author GIP Recia
 * 
 */
public interface IEtabGroupsFinder {

	/**
	 * Retourne une collection contenant les noms des groupes de l'etablissement
	 * @param userInfo Les informations utilisateur
	 * @return collection contenant les noms des groupes de l'etablissement
	 */
	public Collection<String> findGroupsOfEtab(Map<String,String> userInfo);
}
