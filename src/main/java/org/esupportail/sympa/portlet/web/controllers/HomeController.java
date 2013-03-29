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

package org.esupportail.sympa.portlet.web.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esco.sympa.domain.model.LdapEstablishment;
import org.esco.sympa.domain.model.email.EmailConfiguration;
import org.esco.sympa.domain.model.email.IEmailUtility;
import org.esco.sympa.util.UserInfoService;
import org.esupportail.sympa.domain.listfinder.IAvailableListsFinder;
import org.esupportail.sympa.domain.listfinder.IDaoService;
import org.esupportail.sympa.domain.listfinder.IMailingList;
import org.esupportail.sympa.domain.listfinder.IMailingListModel;
import org.esupportail.sympa.domain.listfinder.model.AvailableMailingListsFound;
import org.esupportail.sympa.domain.listfinder.model.Model;
import org.esupportail.sympa.domain.model.CreateListInfo;
import org.esupportail.sympa.domain.model.LdapPerson;
import org.esupportail.sympa.domain.model.UserAttributeMapping;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.services.IDomainService;
import org.esupportail.sympa.domain.services.IDomainService.SympaListFields;
import org.esupportail.sympa.domain.services.impl.SympaListCriterion;
import org.esupportail.sympa.portlet.web.beans.HomeForm;
import org.esupportail.sympa.servlet.JsCreateListTableRow;
import org.esupportail.web.portlet.mvc.ReentrantFormController;
import org.springframework.beans.BeansException;
import org.springframework.validation.Errors;


public class HomeController extends ReentrantFormController {

	/** Logger. */
	private static final Log LOG = LogFactory.getLog(HomeController.class);

	private IDomainService domainService;

	/** User attributes mapping. */
	private UserAttributeMapping userAttributeMapping;

	/** Available list finder. */
	private IAvailableListsFinder availableListFinder;

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

		Map<String, String> userInfo = UserInfoService.getInstance().getUserInfo(request);

		// Build the placeholder values and put it in session.
		Map<String, String> placeholderValuesMap = this.getUserAttributeMapping()
				.buildPlaceholderValuesMap(userInfo);
		request.getPortletSession().setAttribute(ReentrantFormController.PLACEHOLDER_VALUES_MAP_SESSION_KEY,
				placeholderValuesMap, javax.portlet.PortletSession.APPLICATION_SCOPE);



		List<UserSympaListWithUrl> sympaList = this.domainService.getWhich(this.formToCriterion(form),false);
		List<CreateListInfo> createList = this.domainService.getCreateListInfo();
		String homeUrl = this.domainService.getHomeUrl();
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("homeUrl",homeUrl);
		map.put("sympaList", sympaList);
		map.put("createList", createList);


		//Fetch multi-valued attributes
		Map<String, List<Object>> mvUserInfo = (Map<String, List<Object>>)
				request.getAttribute("org.jasig.portlet.USER_INFO_MULTIVALUED");
		HomeController.LOG.debug("Multi variable map is null? " + ((mvUserInfo != null) ? " false " : " true"));

		//Fetch bean in order to have the ldap attribute config and to query for isMemberOf
		try {
			LdapPerson ldapPerson = (LdapPerson) this.getApplicationContext().getBean("ldapPerson");

			final String uid = userInfo.get(ldapPerson.getUidAttribute());
			final String mail = userInfo.get(ldapPerson.getMailAttribute());
			final String uai = userInfo.get(ldapPerson.getUaiAttribute());
			map.put("uai", uai);
			map.put("mail", mail);

			List<String> emailProfileList = this.fetchEmailProfileList(mvUserInfo, ldapPerson, uid);
			List<String> isMemberOfList = this.fetchIsMemberOf(mvUserInfo, ldapPerson, uid);

			this.fetchIsAdmin(map, isMemberOfList, ldapPerson.getAdminRegex());

			this.fetchEmailUtility(map, emailProfileList);
		} catch (BeansException e) {
			// No ldapPerson bean declared
		}

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
			HomeController.LOG.debug("Reading member attribute for ldap person [" + ldapPerson.getMemberAttribute() + " from multivalue map");
			if (listObjects != null) {
				for(Object o : listObjects) {
					isMemberOfList.add(o == null ? "" : o.toString());
					HomeController.LOG.debug("Reading isMemberOf group from MV map : " + isMemberOfList.get(isMemberOfList.size() -1));
				}
			}

		} else {
			HomeController.LOG.debug("MV map not found or does not contain isMemberOf.");

			//Backup plan, use direct ldap queries
			LdapPerson.Person person = ldapPerson.getPerson(uid);

			if (person != null) {
				HomeController.LOG.debug("Ldap person found");
				isMemberOfList = person.getMemberOf();
			} else {
				HomeController.LOG.error("Ldap person NOT found");
			}
		}

		return isMemberOfList;
	}

	private List<String> fetchEmailProfileList(final Map<String, List<Object>> mvUserInfo, final LdapPerson ldapPerson, final String uid) {
		List<String> emailProfileList = null;
		//Check for at least the isMemberOfList which won't be empty should the multi-value map exist
		if ((mvUserInfo != null) && mvUserInfo.containsKey(ldapPerson.getMemberAttribute())) {


			emailProfileList = new ArrayList<String>();
			HomeController.LOG.debug("Reading email profiles for ldap person using attribute [" + ldapPerson.getWebmailProfileAttribute() + "]");
			List<Object> listObjects = mvUserInfo.get(ldapPerson.getWebmailProfileAttribute());
			if (listObjects != null) {
				for(Object o : listObjects) {
					emailProfileList.add(o == null ? "" : o.toString());
				}
			}
		} else {
			HomeController.LOG.debug("MV map not found or does not contain isMemberOf.");

			//Backup plan, use direct ldap queries
			LdapPerson.Person person = ldapPerson.getPerson(uid);

			if (person != null) {
				HomeController.LOG.debug("Ldap person found");
				emailProfileList = person.getProfile();
			} else {
				HomeController.LOG.error("Ldap person NOT found");
			}
		}

		return emailProfileList;
	}

	/**
	 * @param map
	 * @param establishementId
	 */
	private void fetchCreateListTableData(final Map<String,Object> map, final Map<String,String> userInfo) {
		if (this.availableListFinder != null) {
			LdapPerson ldapPerson = (LdapPerson) this.getApplicationContext().getBean("ldapPerson");
			String establishementId = userInfo.get(ldapPerson.getUaiAttribute());

			HomeController.LOG.debug("Entering loadCreateListTable.  UAI: [" + establishementId + "]");

			//Find the establishements email address domain
			LdapEstablishment ldapEstablishment = (LdapEstablishment) this.getApplicationContext().getBean("ldapEstablishment");

			String domain = ldapEstablishment.getMailingListDomain(establishementId);
			HomeController.LOG.debug("Mailing list domain for establishment is [" + domain + "]");

			//Fetch the models from the ESCO-SympaRemote database
			IDaoService daoService = (IDaoService) this.getApplicationContext().getBean("daoService");


			List<Model> listModels = daoService.getAllModels();

			HomeController.LOG.debug("Fetched models from SympaRemote db.  Count: " + listModels.size());

			List<IMailingListModel> listMailingListModels = daoService.getMailingListModels(listModels, userInfo);

			//Get the mailing lists that we can create
			final AvailableMailingListsFound availableLists =
					this.availableListFinder.getAvailableAndNonExistingLists(userInfo, listMailingListModels);
			final Collection<IMailingList> creatableLists = availableLists.getCreatableLists();
			final Collection<IMailingList> updatableLists = availableLists.getUpdatableLists();

			//Convert domain objects to UI
			List<JsCreateListTableRow> createTableData = this.convertMailingListsToJsListTableTow(domain, creatableLists);
			List<JsCreateListTableRow> updateTableData = this.convertMailingListsToJsListTableTow(domain, updatableLists);

			map.put("createTableData", createTableData);
			map.put("updateTableData", updateTableData);

			List<JsCreateListTableRow> tableData = new ArrayList<JsCreateListTableRow>();

			//Convert domain objects to UI
			if (creatableLists != null) {
				for(IMailingList mailList : creatableLists) {
					JsCreateListTableRow row = new JsCreateListTableRow();
					row.setName(mailList.getName().toLowerCase() + "@" + domain);
					row.setSubject(mailList.getDescription());
					row.setModelId(mailList.getModel().getId());
					row.setModelParam(mailList.getModelParameter());
					HomeController.LOG.debug("Loading creatable list " + row.toString());
					tableData.add(row);
				}
			}
		}
	}

	protected List<JsCreateListTableRow> convertMailingListsToJsListTableTow(
			final String domain, final Collection<IMailingList> creatableLists) {
		List<JsCreateListTableRow> tableData = new ArrayList<JsCreateListTableRow>();

		if (creatableLists != null) {
			for(IMailingList mailList : creatableLists) {
				JsCreateListTableRow row = new JsCreateListTableRow();
				row.setName(mailList.getName().toLowerCase() + "@" + domain);
				row.setSubject(mailList.getDescription());
				row.setModelId(mailList.getModel().getId());
				row.setModelParam(mailList.getModelParameter());
				HomeController.LOG.debug("Loading creatable list " + row.toString());
				tableData.add(row);
			}
		}

		return tableData;
	}

	private void fetchIsAdmin(final Map<String,Object> map, final List<String> isMemberOfList, final String adminRegex) {
		// Determine if user is an admin or not

		// Initialize to false
		map.put("isListAdmin", false);

		if (this.availableListFinder != null) {
			// If no available list finder configured
			// No one can be administrator
			return;
		}

		if (isMemberOfList != null) {
			for (String memberOf : isMemberOfList) {
				if (memberOf.matches(adminRegex)) {
					map.put("isListAdmin", true);
					return;
				}
			}
		} else {
			HomeController.LOG.warn("isMemberOfList is NULL!");
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
	public IDomainService getDomainService() {
		return this.domainService;
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(final IDomainService domainService) {
		this.domainService = domainService;
	}

	public UserAttributeMapping getUserAttributeMapping() {
		return this.userAttributeMapping;
	}

	public void setUserAttributeMapping(final UserAttributeMapping userAttributeMapping) {
		this.userAttributeMapping = userAttributeMapping;
	}

	public IAvailableListsFinder getAvailableListFinder() {
		return this.availableListFinder;
	}

	public void setAvailableListFinder(final IAvailableListsFinder availableListFinder) {
		this.availableListFinder = availableListFinder;
	}

}
