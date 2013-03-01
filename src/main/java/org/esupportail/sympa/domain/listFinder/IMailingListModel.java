package org.esupportail.sympa.domain.listFinder;


/**
 *
 * Interface d'un objet MailingListModel, afin d'autoriser differentes implementations
 * La classe MailingListModel represente un modele de mailing list, qui
 * possede
 *  - un identifiant (un nom pour le modele)
 *  - un template de nom pour les listes creees avec le modele
 *  - un pattern de groupe a matcher pour etre autorise a instancier le modele
 *
 * @author GIP Recia
 *
 */
public interface IMailingListModel {

	/**
	 * @return the id of the model
	 */
	String getId();

	/**
	 * @return the listname of the model
	 */
	String getListname();

	/**
	 * @return a description of the list
	 */
	String getDescription();

	/**
	 * @return the pattern of the model
	 */
	String getGroupPatternToMatch();
}
