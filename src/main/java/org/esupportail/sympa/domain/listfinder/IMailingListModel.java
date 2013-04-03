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
