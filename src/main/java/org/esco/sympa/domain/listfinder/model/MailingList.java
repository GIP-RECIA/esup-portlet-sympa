package org.esco.sympa.domain.listfinder.model;

import org.esupportail.sympa.domain.listfinder.IMailingList;
import org.esupportail.sympa.domain.listfinder.IMailingListModel;
import org.springframework.util.StringUtils;

/**
 *
 * Implementation d'une mailing list
 * (le nom de la liste se construit a partir
 *
 * @author GIP Recia
 *
 */
public class MailingList implements IMailingList, Comparable<MailingList> {

	/** Nom de la mailing liste	 */
	private String name;
	/** Description de la mailing List */
	private String description;
	/** Model mailing list */
	private IMailingListModel model;
	/** Paramètre du modèle */
	private String modelParameter;

	/**
	 * Constructor
	 * @param modelToUse
	 * @param modelParam
	 */
	public MailingList(final IMailingListModel modelToUse, final String modelParam) {
		this.model = modelToUse;
		this.modelParameter = modelParam;
		String listName = this.replaceModelParamToken(this.model.getListname());
		listName = this.stripLdapSpecialChars(listName);
		this.name = this.stripEmailSpecialChars(listName);
		this.description = this.replaceModelParamToken(this.model.getDescription());
	}

	/**
	 * Strip all Email special chars.
	 * 
	 * @param text the text to strip
	 * @return a striped text
	 */
	private String stripEmailSpecialChars(final String text) {
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
	private String stripLdapSpecialChars(final String text) {
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
	private String replaceModelParamToken(final String text) {
		return (this.modelParameter == null) || (text == null) ? text : text.replaceAll("\\{((?!UAI).)*\\}", this.modelParameter);
	}

	/** {@inheritDoc} */
	public String getDescription() {
		return this.description;
	}

	/** {@inheritDoc} */
	public String getName() {
		return this.name;
	}

	/** {@inheritDoc} */
	public IMailingListModel getModel() {
		return this.model;
	}

	/** {@inheritDoc} */
	public String getModelParameter() {
		return this.modelParameter;
	}

	/** {@inheritDoc} */
	public int compareTo(final MailingList list) {
		return this.getName().compareTo(list.getName());
	}
}
