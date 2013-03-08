/**
 * ESUP-Portail Disk quota - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-diskquota
 */
package org.esco.sympa.domain.listfinder.services;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.esco.sympa.domain.listfinder.model.MailingListModel;
import org.esupportail.sympa.domain.listfinder.IDaoService;
import org.esupportail.sympa.domain.listfinder.IMailingListModel;
import org.esupportail.sympa.domain.listfinder.model.Model;
import org.esupportail.sympa.domain.listfinder.model.ModelRequest;
import org.esupportail.sympa.domain.listfinder.model.ModelSubscribers;
import org.esupportail.sympa.domain.listfinder.model.PreparedRequest;
import org.esupportail.sympa.domain.model.UserAttributeMapping;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * The Hiberate implementation of the DAO service.
 * 
 * See /properties/dao/dao-example.xml
 */
public class HibernateDaoServiceImpl extends HibernateDaoSupport
implements IDaoService {

	/** User attributes mapping. */
	private UserAttributeMapping userAttributeMapping;

	/**
	 * Bean constructor.
	 */
	public HibernateDaoServiceImpl() {
		super();
	}

	public List<ModelSubscribers> getAllModelSubscribers() {
		return this.getHibernateTemplate().find("FROM ModelSubscribers");
	}

	/**
	 * @return a list of groups that could have access to the list
	 */
	public List<ModelRequest> getAllModelRequests() {
		return this.getHibernateTemplate().find("FROM ModelRequest");
	}

	/**
	 * @return a list of groups that could have access to the list
	 */
	public List<Model> getAllModels() {
		return this.getHibernateTemplate().find("FROM Model");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.esupportail.sympa.domain.services.DaoService#getModel(java.math.
	 * BigInteger)
	 */
	public Model getModel(final BigInteger id) {
		List<Model> models = this.getHibernateTemplate().find(
				"FROM Model where id = ?", id);
		if ((models == null) || (models.size() < 1)) {
			return null;
		}

		return models.get(0);
	}

	public ModelSubscribers getModelSubscriber(final Model model) {
		List<ModelSubscribers> modelSubscribers = this.getHibernateTemplate().find(
				"FROM ModelSubscribers where id = ?", model.getId());
		if ((modelSubscribers == null) || (modelSubscribers.size() < 1)) {
			return null;
		}

		return modelSubscribers.get(0);
	}

	/**
	 * @return a list of groups that could have access to the list
	 */
	public List<PreparedRequest> getAllPreparedRequests() {
		return this.getHibernateTemplate().find("FROM PreparedRequest");
	}

	/**
	 * @param requset
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public ModelRequest getModelRequest(final PreparedRequest preparedRequest,
			final Model model) {

		// List<ModelRequest> listModelRequest =
		// this.getHibernateTemplate().find("");

		List<ModelRequest> listModelRequest = this.getHibernateTemplate()
				.executeFind(new HibernateCallback() {
					public Object doInHibernate(final Session session) // throws
					// HibernateException,
					// SQLException
					{
						// Query query =
						// session.createQuery("FROM ModelRequest mr where mr.idRequest = :idRequest");

						Query query = session
								.createQuery("FROM ModelRequest mr where mr.id.idRequest = :idRequest and mr.id.idModel = :idModel");
						query.setParameter("idRequest", preparedRequest.getId());
						query.setParameter("idModel", model.getId());
						return query.list();
					}
				});

		/*
		 * action)find( "FROM ModelRequest mr where ? = 89 and ? = 39", //new
		 * String[] {"ridRequest", "ridModel"},
		 * preparedRequest.getId()longValue(), model.getId().longValue() );
		 */

		if ((listModelRequest == null) || (listModelRequest.size() == 0)) {
			return null;
		}

		ModelRequest modelRequest = listModelRequest.get(0);

		return modelRequest;
	}

	// ////////////////////////////////////////////////////////////
	// misc
	// ////////////////////////////////////////////////////////////
	public List<IMailingListModel> getMailingListModels(final List<Model> listModels, final Map<String, String> userInfo) {
		List<IMailingListModel> listMailingListModels = new ArrayList<IMailingListModel>();

		// Convert hibernate objects into IMailingListModel objects
		for (Model model : listModels) {
			MailingListModel mailingListModel = new MailingListModel(model
					.getId().toString(),
					model.getListname(),
					//model.getPattern().replaceAll("\\{RNE\\}", StringUtils.defaultString(establishementId)),
					this.userAttributeMapping.substitutePlaceholder(model.getPattern(), userInfo),
					model.getDescription());
			listMailingListModels.add(mailingListModel);
		}

		return listMailingListModels;
	}

	public UserAttributeMapping getUserAttributeMapping() {
		return this.userAttributeMapping;
	}

	public void setUserAttributeMapping(final UserAttributeMapping userAttributeMapping) {
		this.userAttributeMapping = userAttributeMapping;
	}


}
