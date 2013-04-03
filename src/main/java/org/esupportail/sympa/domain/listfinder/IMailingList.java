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

/**
 * Interface d'un objet MailingList, afin d'autoriser differentes implementations.
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
