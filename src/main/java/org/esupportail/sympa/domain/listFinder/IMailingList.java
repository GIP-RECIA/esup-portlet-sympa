package org.esupportail.sympa.domain.listFinder;

/**
 *
 * Interface d'un objet MailingList, afin d'autoriser differentes implementations
 *
 * @author GIP Recia
 *
 */
public interface IMailingList {

	/**
	 * Return the name of the mailing list.
	 * 
	 * @return the name of the mailing list
	 */
	String getName();

	/**
	 *
	 * @return The description of the mailing list
	 */
	String getDescription();

	/**
	 * Return the model of the mailing list.
	 * 
	 * @return the model of the mailing list
	 */
	IMailingListModel getModel();

	/**
	 * Return the modelParameter.
	 * 
	 * @return the modelParameter (ex. Classe, Niveau...)
	 */
	String getModelParameter();
}
