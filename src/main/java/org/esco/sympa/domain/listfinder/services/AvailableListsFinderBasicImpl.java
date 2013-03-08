package org.esco.sympa.domain.listfinder.services;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.esco.sympa.domain.groupfinder.IEtabGroupsFinder;
import org.esco.sympa.domain.listfinder.IListsFromGroupsPatternMatcher;
import org.esupportail.sympa.domain.listfinder.IAvailableListsFinder;
import org.esupportail.sympa.domain.listfinder.IExistingListsFinder;
import org.esupportail.sympa.domain.listfinder.IMailingList;
import org.esupportail.sympa.domain.listfinder.IMailingListModel;

/**
 * Implementation basique du 'module' permettant de sortir la liste des
 * listes de diffusion qu'il est possible de creer pour cet
 * etablissement, à partir des modeles de listes.
 * 
 * Par exemple :
 * 		eleves701 avec le model eleves_classe
 * 		eleves702 avec le model eleves_classe
 * 		parents701 avec le model parents_classe
 * 		parents702 ..
 * 		profs701 ..
 * 		profs702 ..
 * 
 * @author GIP Recia
 *
 */
public class AvailableListsFinderBasicImpl implements IAvailableListsFinder {

	/** Logger */
	private static final Logger log = Logger.getLogger(AvailableListsFinderBasicImpl.class);

	/** Le domaine de base des listes (utilise pour fabriquer le nom du robot sympa) */
	private String listDomain;

	/** Le mecanisme de recherche des groupes de l'etablissement injecte par Spring */
	private IEtabGroupsFinder etabGroupsFinder;

	/** Le mecanisme de recuperation des listes existantes de l'etablissement injecte par Spring */
	private IExistingListsFinder existingListsFinder;

	/** Le mecanisme permettant de deduire (par pattern) les listes a creer, en fonction des groupes de l'etablissement */
	private IListsFromGroupsPatternMatcher listsFromGroupsPatternMatcher;

	/** {@inheritDoc} */
	public void setExistingListsFinder(final IExistingListsFinder existingListFinder) {
		this.existingListsFinder = existingListFinder;
	}

	/**
	 * Retourne les listes qu'il est possible de creer pour cet etablissement a partir
	 * de l'ensemble des modeles connus.
	 * Chaque modele possede un pattern de groupe qui, s'il est respecte, autorise
	 * l'instanciation d'une liste avec ce modele.
	 * (Permet d'obtenir la liste des listes qui seront proposees a l'administrateur d'etablissement)
	 * 
	 * @param rne le RNE de l'etablissement concerne
	 * @param modeles les modeles de listes connus, a partir desquels on va deduire les
	 * listes qu'il est possible de creer
	 * @return la collection de mailing lists qu'il est possible de creer
	 * pour cet etablissement
	 */
	public Collection<IMailingList> getAvailableAndNonExistingLists (
			final Map<String,String> userInfo, final Collection<IMailingListModel> modeles) {

		// Create an empty Collection of Mailing Lists
		// => all the lists that could be created by the admin
		Collection<IMailingList> availablelists = new TreeSet<IMailingList>();

		// Get all the groups of the current educational establishment
		Collection<String> groupsOfEtab = this.etabGroupsFinder.findGroupsOfEtab(userInfo);

		AvailableListsFinderBasicImpl.log.debug("Groups found " + groupsOfEtab.size());

		// For each model, we must search for groups that match the model pattern
		// => each time a group matchs the current model's pattern, a new list will be added to the available lists
		// example :
		// the group "esco:etablissement:FICTIF_0450822x:Niveau Seconde:Profs_503'
		// matches the pattern "esco:Etablissements:FICTIF_0450822x:[^:]+:Profs_([\\ -]|\\w+)"
		// so the "profs503" list will be added to the available lists
		Iterator<IMailingListModel> modelesIt = modeles.iterator();
		if (modelesIt != null) {
			while (modelesIt.hasNext()) {
				IMailingListModel currentModel = modelesIt.next();
				Collection<IMailingList> results = this.listsFromGroupsPatternMatcher.findPossibleListsWithModel(
						groupsOfEtab, currentModel);
				AvailableListsFinderBasicImpl.log.debug("Mailing Lists found " + results.size() + " for model [" + currentModel.toString() + "]");
				availablelists.addAll(results);
			}
		}

		AvailableListsFinderBasicImpl.log.debug("Finding existing lists with userInfo [" + userInfo.toString() + "] and domain [" + this.listDomain + "]");
		Collection<String> existingLists = this.existingListsFinder.findExistingLists(userInfo);
		AvailableListsFinderBasicImpl.log.debug("Existing lists found " + existingLists.size());

		Iterator<IMailingList> itLists = availablelists.iterator();
		if (itLists != null) {
			while (itLists.hasNext()) {
				IMailingList list = itLists.next();

				// On test si la liste fait partie des listes existantes
				if (existingLists.contains(list.getName().toLowerCase())) {
					itLists.remove();
					AvailableListsFinderBasicImpl.log.debug("List " + list.toString() + " already exists, removing");
				}
			}
		}

		AvailableListsFinderBasicImpl.log.debug("Available lists count " + availablelists.size());
		return availablelists;
	}

	public void setEtabGroupsFinder(final IEtabGroupsFinder groupsFinder) {
		this.etabGroupsFinder = groupsFinder;
	}

	public void setListsFromGroupsPatternMatcher(final IListsFromGroupsPatternMatcher patternMatcher) {
		this.listsFromGroupsPatternMatcher = patternMatcher;
	}

	public void setListDomain(final String domain) {
		this.listDomain = domain;
	}

}
