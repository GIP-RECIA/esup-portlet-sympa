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
/**
 * 
 */
package org.esupportail.sympa.domain.listfinder.model;

import org.esupportail.sympa.domain.listfinder.IMailingList;
import org.esupportail.sympa.domain.listfinder.IMailingListModel;
import org.springframework.util.StringUtils;

/**
 * Implementation d'une mailing list
 * le nom de la liste se construit a partir du modele de mailing list
 *
 * @author GIP Recia
 *
 */
public class BasicMailingList implements IMailingList, Comparable<BasicMailingList> {

	/** Nom de la mailing liste	 */
	private String name;

	/** Description de la mailing List */
	private String description;

	/** Model mailing list */
	private IMailingListModel model;

	/** Paramètre du modèle */
	private String modelParameter;

	/**
	 * Constructor.
	 * 
	 * @param modelToUse the parent model.
	 * @param modelParam the param
	 */
	public BasicMailingList(final IMailingListModel modelToUse, final String modelParam) {
		this.model = modelToUse;
		this.modelParameter = modelParam;
		String listName = this.replaceModelParamToken(this.model.getListname());
		listName = this.stripLdapSpecialChars(listName);
		this.name = this.stripEmailSpecialChars(listName);
		this.description = this.replaceModelParamToken(this.model.getDescription());
	}

	@Override
	public String toString() {
		return "BasicMailingList [name=" + this.name + ", description=" + this.description + "]";
	}

	/**
	 * Strip all Email special chars.
	 * 
	 * @param text the text to strip
	 * @return a striped text
	 */
	protected String stripEmailSpecialChars(final String text) {
		String result = null;

		if (StringUtils.hasText(text)) {
			// Strip forbid email chars
			result = text.replaceAll("[^\\w!#\\$%&'\\*\\+\\-/=\\?\\^_`\\{\\|\\}~\\.]", "");
			// Strip multiple dot
			result = result.replaceAll("[\\.]{2,}", "");
		}

		return result;
	}

	/**
	 * Strip all Ldap special chars.
	 * 
	 * @param text the text to strip
	 * @return a striped text
	 */
	protected String stripLdapSpecialChars(final String text) {
		String result = null;

		if (StringUtils.hasText(text)) {
			// Strip parenthesis content
			result = text.replaceAll("\\(.*\\)", "");
			// Strip ldap special chars ( ) \ * NUL
			result = result.replaceAll("[\\(\\)\\*\\\\\\x00]+", "");
			// Replace spaces by underscore
			result = result.trim();
			result = result.replaceAll("[\\s]+", "_");
		}

		return result;
	}

	/**
	 * @param text A string
	 * @return The string with tokens matching {...} tokens {UAI} is left alone as it is a reserved token
	 */
	protected String replaceModelParamToken(final String text) {
		return (this.modelParameter == null) || (text == null) ? text : text.replaceAll("\\{((?!UAI).)*\\}", this.modelParameter);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	/**
	 * {@inheritDoc}
	 * 
	 * @return the model
	 */
	public IMailingListModel getModel() {
		return this.model;
	}
	/**
	 * {@inheritDoc}
	 * 
	 * @return the modelParameter
	 */
	public String getModelParameter() {
		return this.modelParameter;
	}

	/** {@inheritDoc} */
	public int compareTo(final BasicMailingList list) {
		return this.getName().compareTo(list.getName());
	}
}
