/**
 * Copyright (C) 2010 INSA LYON http://www.insa-lyon.fr
 * Copyright (C) 2010 Esup Portail http://www.esup-portail.org
 * @Author (C) 2010 Olivier Franco <Olivier.Franco@insa-lyon.fr>
 * @Contributor (C) 2010 Doriane Dusart <Doriane.Dusart@univ-valenciennes.fr>
 * @Contributor (C) 2010 Jean-Pierre Tran <Jean-Pierre.Tran@univ-rouen.fr>
 * @Contributor (C) 2010 Vincent Bonamy <Vincent.Bonamy@univ-rouen.fr>
 *
 * Licensed under the GPL License, (please see the LICENCE file)
 */

package org.esco.sympa.portlet.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.sympa.domain.model.Domain;
import org.esco.sympa.domain.model.EscoUserAttributeMapping;
import org.esco.sympa.domain.model.LdapEstablishment;
import org.esco.sympa.domain.model.UAI;
import org.esco.sympa.domain.model.email.EmailConfiguration;
import org.esco.sympa.domain.model.email.IEmailUtility;
import org.esco.sympa.domain.services.IEscoDomainService;
import org.esco.sympa.util.UserInfoHelper;
import org.esupportail.sympa.domain.listfinder.IAvailableListsFinder;
import org.esupportail.sympa.domain.listfinder.IDaoService;
import org.esupportail.sympa.domain.listfinder.IMailingList;
import org.esupportail.sympa.domain.listfinder.IMailingListModel;
import org.esupportail.sympa.domain.listfinder.model.Model;
import org.esupportail.sympa.domain.model.CreateListInfo;
import org.esupportail.sympa.domain.model.LdapPerson;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.IDomainService.SympaListFields;
import org.esupportail.sympa.domain.services.SympaListCriterion;
import org.esupportail.sympa.portlet.web.beans.HomeForm;
import org.esupportail.sympa.portlet.web.controllers.HomeController;
import org.esupportail.sympa.servlet.JsCreateListTableRow;
import org.esupportail.web.portlet.mvc.ReentrantFormController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;


public class EscoHomeController extends ReentrantFormController implements InitializingBean {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(EscoHomeController.class);

	/** Session key of the placeholder values map. */
	public static final String PLACEHOLDER_VALUES_MAP_SESSION_KEY =
			"UserAttributeMapping.PLACEHOLDER_VALUES_MAP_SESSION_KEY";

	private IEscoDomainService domainService;

	/** User attributes mapping. */
	private EscoUserAttributeMapping userAttributeMapping;

	//Used in order to get the current establishment to filter the lists
	private LdapEstablishment ldapEstablishment;

	private LdapPerson ldapPerson;

	private IAvailableListsFinder availableListFinder;

	private IDaoService daoService;

	/** {@inheritDoc} */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.domainService, "No IEscoDomainService injected !");
		Assert.notNull(this.userAttributeMapping, "No EscoUserAttributeMapping injected !");
		Assert.notNull(this.ldapPerson, "No LdapPerson injected !");
		Assert.notNull(this.ldapEstablishment, "No LdapEstablishment injected !");
		Assert.notNull(this.availableListFinder, "No IAvailableListsFinder injected !");
		Assert.notNull(this.daoService, "No IDaoService injected !");
	}

	@Override
	public Object newCommand(final PortletRequest request) throws Exception {
		HomeForm  form = (HomeForm)super.createCommand();
		form.setEditor(true);
		form.setOwner(true);
		form.setSubscriber(true);
		form.setTabIndex("0");
		return form;
	}

	/* (non-Javadoc)
	 * 
	 * This method fetches data for the top level page
	 * 
	 * @see org.esupportail.web.portlet.mvc.ReentrantFormController#referenceData(javax.portlet.PortletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<?, ?> referenceData(final PortletRequest request, final Object command,
			final Errors errors) throws Exception {
		HomeForm form = (HomeForm)command;

		if (form.isInvalidateCache()) {
			EscoHomeController.LOG.info("Clearing cache");
			this.domainService.invalidateCache();
			form.setInvalidateCache(false);
		}

		Map<String, String> userInfo = UserInfoHelper.getUserInfo(request);

		// Add user informations in portal attributes map.
		userInfo = this.getUserAttributeMapping().enhanceUserInfo(userInfo);

		// Build the placeholder values and put it in session.
		Map<String, String> placeholderValuesMap = this.getUserAttributeMapping()
				.buildPlaceholderValuesMap(userInfo);
		request.getPortletSession().setAttribute(HomeController.PLACEHOLDER_VALUES_MAP_SESSION_KEY,
				placeholderValuesMap, javax.portlet.PortletSession.APPLICATION_SCOPE);

		Map<String,Object> map = new HashMap<String, Object>();

		//Fetch multi-valued attributes
		Map<String, List<Object>> mvUserInfo = (Map<String, List<Object>>)
				request.getAttribute("org.jasig.portlet.USER_INFO_MULTIVALUED");
		EscoHomeController.LOG.debug("Multi variable map is null? " + ((mvUserInfo != null) ? " false " : " true"));

		//Fetch bean in order to have the ldap attribute config and to query for isMemberOf
		try {
			final String uid = userInfo.get(this.ldapPerson.getUidAttribute());
			final String mail = userInfo.get(this.ldapPerson.getMailAttribute());
			final String uai = userInfo.get(this.ldapPerson.getUaiAttribute());
			map.put("uai", uai);
			map.put("mail", mail);

			//Filter the user lists to make sure we only display lists that are in the current establishment.  This
			//is done by comparing the domain of the list address (after the @).
			//As domains are 1 to 1 with establishments
			//this can be used to tell what lists belong to which establishment.
			String domain = this.ldapEstablishment.getMailingListDomain(uai);
			List<UserSympaListWithUrl> sympaList = this.domainService.getWhich(new UAI(
					uai), new Domain(domain), this.formToCriterion(form), false);

			List<CreateListInfo> createList = this.domainService.getCreateListInfo();
			map.put("sympaList", sympaList);
			map.put("createList", createList);

			List<String> emailProfileList = this.fetchEmailProfileList(mvUserInfo, this.ldapPerson, uid);
			List<String> isMemberOfList = this.fetchIsMemberOf(mvUserInfo, this.ldapPerson, uid);

			this.fetchIsAdmin(map, isMemberOfList, this.ldapPerson.getAdminRegex(), uai);

			this.fetchEmailUtility(map, emailProfileList);
		} catch (BeansException e) {
			// No ldapPerson bean declared
		}

		String homeUrl = this.domainService.getHomeUrl();
		map.put("homeUrl",homeUrl);


		if (Boolean.TRUE.equals(map.get("isListAdmin"))) {
			this.fetchCreateListTableData(map, userInfo);
		}

		return map;
	}

	private List<String> fetchIsMemberOf(final Map<String, List<Object>> mvUserInfo, final LdapPerson ldapPerson, final String uid) {
		List<String> isMemberOfList = null;

		//Check for at least the isMemberOfList which won't be empty should the multi-value map exist
		if ((mvUserInfo != null) && mvUserInfo.containsKey(ldapPerson.getMemberAttribute())) {
			isMemberOfList = new ArrayList<String>();
			List<Object> listObjects = mvUserInfo.get(ldapPerson.getMemberAttribute());
			EscoHomeController.LOG.debug("Reading member attribute for ldap person [" + ldapPerson.getMemberAttribute() + " from multivalue map");
			if (listObjects != null) {
				for(Object o : listObjects) {
					isMemberOfList.add(o == null ? "" : o.toString());
					EscoHomeController.LOG.debug("Reading isMemberOf group from MV map : " + isMemberOfList.get(isMemberOfList.size() -1));
				}
			}

		} else {
			EscoHomeController.LOG.debug("MV map not found or does not contain isMemberOf.");

			//Backup plan, use direct ldap queries
			LdapPerson.Person person = ldapPerson.getPerson(uid);

			if (person != null) {
				EscoHomeController.LOG.debug("Ldap person found");
				isMemberOfList = person.getMemberOf();
			} else {
				EscoHomeController.LOG.error("Ldap person NOT found");
			}
		}

		return isMemberOfList;
	}

	private List<String> fetchEmailProfileList(final Map<String, List<Object>> mvUserInfo, final LdapPerson ldapPerson, final String uid) {
		List<String> emailProfileList = null;
		//Check for at least the isMemberOfList which won't be empty should the multi-value map exist
		if ((mvUserInfo != null) && mvUserInfo.containsKey(ldapPerson.getMemberAttribute())) {


			emailProfileList = new ArrayList<String>();
			EscoHomeController.LOG.debug("Reading email profiles for ldap person using attribute [" + ldapPerson.getWebmailProfileAttribute() + "]");
			List<Object> listObjects = mvUserInfo.get(ldapPerson.getWebmailProfileAttribute());
			if (listObjects != null) {
				for(Object o : listObjects) {
					emailProfileList.add(o == null ? "" : o.toString());
				}
			}
		} else {
			EscoHomeController.LOG.debug("MV map not found or does not contain isMemberOf.");

			//Backup plan, use direct ldap queries
			LdapPerson.Person person = ldapPerson.getPerson(uid);

			if (person != null) {
				EscoHomeController.LOG.debug("Ldap person found");
				emailProfileList = person.getProfile();
			} else {
				EscoHomeController.LOG.error("Ldap person NOT found");
			}
		}

		return emailProfileList;
	}

	/**
	 * @param map
	 * @param establishementId
	 */
	private void fetchCreateListTableData(final Map<String,Object> map, final Map<String,String> userInfo) {
		String establishementId = userInfo.get(this.ldapPerson.getUaiAttribute());

		EscoHomeController.LOG.debug("Entering loadCreateListTable.  UAI: [" + establishementId + "]");

		//Find the establishements email address domain
		String domain = this.ldapEstablishment.getMailingListDomain(establishementId);
		EscoHomeController.LOG.debug("Mailing list domain for establishment is [" + domain + "]");

		//Fetch the models from the ESCO-SympaRemote database
		List<Model> listModels = this.daoService.getAllModels();

		EscoHomeController.LOG.debug("Fetched models from SympaRemote db.  Count: " + listModels.size());

		List<IMailingListModel> listMailingListModels = this.daoService.getMailingListModels(listModels, userInfo);

		//Get the mailing lists that we can create
		Collection<IMailingList> listLists =
				this.availableListFinder.getAvailableAndNonExistingLists(userInfo, listMailingListModels);

		List<JsCreateListTableRow> tableData = new ArrayList<JsCreateListTableRow>();

		//Convert domain objects to UI
		if (listLists != null) {
			for(IMailingList mailList : listLists) {
				JsCreateListTableRow row = new JsCreateListTableRow();
				row.setName(mailList.getName().toLowerCase() + "@" + domain);
				row.setSubject(mailList.getDescription());
				row.setModelId(mailList.getModel().getId());
				row.setModelParam(mailList.getModelParameter());
				EscoHomeController.LOG.debug("Loading creatable list " + row.toString());
				tableData.add(row);
			}
		}



		map.put("tableData", tableData);
	}

	private void fetchIsAdmin(final Map<String,Object> map, final List<String> isMemberOfList, String adminRegex, final String uai) {
		// /////////////////////////////////////////////////////////
		// Determine if user is an admin or not

		// Initialize to false
		map.put("isListAdmin", false);

		if (StringUtils.hasText(uai)) {
			adminRegex = adminRegex.replaceAll(Pattern.quote("%UAI"), uai);
		}

		if (isMemberOfList != null) {
			for (String memberOf : isMemberOfList) {
				if (memberOf.matches(adminRegex)) {
					map.put("isListAdmin", true);
					return;
				}
			}
		} else {
			EscoHomeController.LOG.warn("isMemberOfList is NULL!");
		}

	}

	private void fetchEmailUtility(final Map<String,Object> map, final List<String> emailProfileList) {
		//Find which email program is configured for the user
		EmailConfiguration emailConfig = (EmailConfiguration) this
				.getApplicationContext().getBean("emailConfig");

		IEmailUtility selectedEmailUtility = null;

		if (emailProfileList != null) {
			for (String profile : emailProfileList) {
				for (IEmailUtility emailUtil : emailConfig.getUtils()) {
					if (emailUtil.isCorrectEmailUtility(profile)) {
						selectedEmailUtility = emailUtil;
						break;
					}
				}
			}
		}

		//TEST for testing simple email
		//map.put("emailUtil", null);

		map.put("emailUtil", selectedEmailUtility);
	}

	private List<SympaListCriterion> formToCriterion(final HomeForm form) {
		if ( form == null ) {
			return null;
		}
		List<SympaListCriterion> crits = new ArrayList<SympaListCriterion>();
		if ( form.isEditor() ) {
			crits.add(new SympaListCriterion(SympaListFields.editor,new Boolean(form.isEditor())));
		}
		if ( form.isOwner() ) {
			crits.add(new SympaListCriterion(SympaListFields.owner,new Boolean(form.isOwner())));
		}
		if ( form.isSubscriber() ) {
			crits.add(new SympaListCriterion(SympaListFields.subscriber,new Boolean(form.isSubscriber())));
		}
		return crits;
	}

	/**
	 * @return the domainService
	 */
	public IEscoDomainService getDomainService() {
		return this.domainService;
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final IEscoDomainService domainService) {
		this.domainService = domainService;
	}

	public EscoUserAttributeMapping getUserAttributeMapping() {
		return this.userAttributeMapping;
	}

	public void setUserAttributeMapping(final EscoUserAttributeMapping userAttributeMapping) {
		this.userAttributeMapping = userAttributeMapping;
	}

	/**
	 * @return the ldapEstablishment
	 */
	public LdapEstablishment getLdapEstablishment() {
		return this.ldapEstablishment;
	}

	/**
	 * @param ldapEstablishment the ldapEstablishment to set
	 */
	public void setLdapEstablishment(final LdapEstablishment ldapEstablishment) {
		this.ldapEstablishment = ldapEstablishment;
	}

	public LdapPerson getLdapPerson() {
		return this.ldapPerson;
	}

	public void setLdapPerson(final LdapPerson ldapPerson) {
		this.ldapPerson = ldapPerson;
	}

	public IAvailableListsFinder getAvailableListFinder() {
		return this.availableListFinder;
	}

	public void setAvailableListFinder(final IAvailableListsFinder availableListFinder) {
		this.availableListFinder = availableListFinder;
	}

	public IDaoService getDaoService() {
		return this.daoService;
	}

	public void setDaoService(final IDaoService daoService) {
		this.daoService = daoService;
	}

}
