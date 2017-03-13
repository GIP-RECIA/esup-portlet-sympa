/**
 * Licensed to ESUP-Portail under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.
 *
 * ESUP-Portail licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.esupportail.sympa.portlet.web.controllers;

import java.util.ArrayList;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.sympa.domain.model.CreateListInfo;
import org.esupportail.sympa.domain.model.LdapEstablishment;
import org.esupportail.sympa.domain.model.LdapPerson;
import org.esupportail.sympa.domain.model.UserAttribute;
import org.esupportail.sympa.domain.model.UserSympaListWithUrl;
import org.esupportail.sympa.domain.model.email.EmailConfiguration;
import org.esupportail.sympa.domain.model.email.IEmailUtility;
import org.esupportail.sympa.domain.services.IDomainService;
import org.esupportail.sympa.domain.services.IDomainService.SympaListFields;
import org.esupportail.sympa.domain.services.SympaListCriterion;
import org.esupportail.sympa.portlet.web.beans.HomeForm;
import org.esupportail.sympa.util.UserInfoService;
import org.esupportail.web.portlet.mvc.ReentrantFormController;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

@Controller
public class HomeController extends ReentrantFormController {
	
	private IDomainService domainService;
	
	/** User attributes mapping. */
	@Autowired
	private UserAttribute userAttributeMapping;
	
	//Used in order to get the current establishment to filter the lists
		@Autowired
		private LdapEstablishment ldapEstablishment;

	private LdapPerson ldapPerson;
	/** Logger. */
	private static final Log LOG = LogFactory.getLog(HomeController.class);
	



	

	@Override
	public Object newCommand(PortletRequest request) throws Exception {
		HomeForm  form = (HomeForm)super.createCommand();
		form.setEditor(true);
		form.setOwner(true);
		form.setSubscriber(true);
		return form;
	}

//	@SuppressWarnings("unchecked")
//	@Override
//	public Map referenceData(PortletRequest request, Object command,
//			Errors errors) throws Exception {
//				
//				
//				
//		HomeForm form = (HomeForm)command;
//		List<UserSympaListWithUrl> sympaList = domainService.getWhich(formToCriterion(form),false);
//		List<CreateListInfo> createList = domainService.getCreateListInfo();
//		String homeUrl = domainService.getHomeUrl();
//		Map<String,Object> map = new HashMap<String, Object>();
//		
//		
//		map.put("homeUrl",homeUrl);
//		map.put("sympaList", sympaList);
//		map.put("createList", createList);
//		
//		
//		return map;
//	}
	private List<SympaListCriterion> formToCriterion(HomeForm form) {
		if ( form == null ) return null;
		List<SympaListCriterion> crits = new ArrayList<SympaListCriterion>();
		if ( form.isEditor() )
			crits.add(new SympaListCriterion(SympaListFields.editor,new Boolean(form.isEditor())));
		if ( form.isOwner() ) 
			crits.add(new SympaListCriterion(SympaListFields.owner,new Boolean(form.isOwner())));
		if ( form.isSubscriber() )
			crits.add(new SympaListCriterion(SympaListFields.subscriber,new Boolean(form.isSubscriber())));
		return crits;
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
	
	
//	@Override
//	@SuppressWarnings("unchecked")
	public Map<?, ?> referenceData(final PortletRequest request, final Object command,
			final Errors errors) throws Exception {
		LOG.debug("Entre dans la fonction referenceData");
		HomeForm form = (HomeForm)command;
		Map<String,Object> map = new HashMap<String, Object>();
		
		// MBD: Check etab switching
		//checkAdminEtabSwitching(request, form);
		
		if (form.isInvalidateCache()) {
			HomeController.LOG.info("Clearing cache");
			this.getDomainService().invalidateCache();
			form.setInvalidateCache(false);
		}

		//this.fetchSwitchEtabMap(map, request);
		
		Map<String, String> userInfo = UserInfoService.getUserInfo(request);

		// Add user informations in portal attributes map.
		userInfo = this.getUserAttributeMapping().enhanceUserInfo(userInfo);

		// Build the placeholder values and put it in session.
		Map<String, String> placeholderValuesMap = this.getUserAttributeMapping()
				.buildPlaceholderValuesMap(userInfo);
		request.getPortletSession().setAttribute(ReentrantFormController.PLACEHOLDER_VALUES_MAP_SESSION_KEY,
				placeholderValuesMap, javax.portlet.PortletSession.APPLICATION_SCOPE);

		//Fetch multi-valued attributes
		Map<String, List<Object>> mvUserInfo = (Map<String, List<Object>>)
				request.getAttribute("org.jasig.portlet.USER_INFO_MULTIVALUED");
		HomeController.LOG.debug("Multi variable map is null? " + ((mvUserInfo != null) ? " false " : " true"));

		//Fetch bean in order to have the ldap attribute config and to query for isMemberOf
		try {
			final String uid = userInfo.get(UserInfoService.getPortalUidAttribute());
			final String mail = userInfo.get(UserInfoService.getPortalMailAttribute());
			final String uai = userInfo.get(UserInfoService.getPortalUaiAttribute());

			Assert.hasText(uid, "UID shouldn't be empty !");
			Assert.hasText(uai, "UAI shouldn't be empty !");
			Assert.hasText(mail, "MAIL shouldn't be empty !");

			map.put("uai", uai);
			map.put("mail", mail);

			//Filter the user lists to make sure we only display lists that are in the current establishment.  This
			//is done by comparing the domain of the list address (after the @).
			//As domains are 1 to 1 with establishments
			//this can be used to tell what lists belong to which establishment.
			List<UserSympaListWithUrl> sympaList = domainService.getWhich(formToCriterion(form), false);

			List<CreateListInfo> createList = domainService.getCreateListInfo();
			String homeUrl = domainService.getHomeUrl();
			map.put("homeUrl",homeUrl);
			map.put("sympaList", sympaList);
			map.put("createList", createList);

			//List<String> emailProfileList = this.fetchEmailProfileList(mvUserInfo, this.ldapPerson, uid);
			//List<String> isMemberOfList = this.fetchIsMemberOf(mvUserInfo, this.ldapPerson, uid);

			//this.fetchIsAdmin(map, isMemberOfList, this.ldapPerson.getAdminRegex(), uai);

			//this.fetchEmailUtility(map, emailProfileList);
			/*******/
			//HomeForm form = (HomeForm)command;
			//List<UserSympaListWithUrl> sympaList = domainService.getWhich(formToCriterion(form),false);
			//List<CreateListInfo> createList = domainService.getCreateListInfo();
			//String homeUrl = domainService.getHomeUrl();
			//Map<String,Object> map = new HashMap<String, Object>();
			
			
//			map.put("homeUrl",homeUrl);
//			map.put("sympaList", sympaList);
//			map.put("createList", createList);
			/****/
		} catch (BeansException e) {
			// No ldapPerson bean declared
		}

		String homeUrl = this.getDomainService().getHomeUrl();
		map.put("homeUrl",homeUrl);


//		if (Boolean.TRUE.equals(map.get("isListAdmin"))) {
//			this.fetchCreateListTableData(map, userInfo);
//		}

		return map;
	}
	
//	protected void fetchSwitchEtabMap(final Map<String,Object> map, final PortletRequest request) {
//		final Map<String, String> switchEtabMap = UserInfoService.findAdminSwitchEtabMap(request);
//		if (switchEtabMap != null && switchEtabMap.size() < 2) {
//			// MBD: if less than 2 establishment no map need to be passed
//			map.remove("switchEtabMapEntries");
//		} else {
//			map.put("switchEtabMapEntries", switchEtabMap.entrySet());
//		}
//	}
	
	
	public LdapEstablishment getLdapEstablishment() {
		return ldapEstablishment;
	}

	public void setLdapEstablishment(LdapEstablishment ldapEstablishment) {
		this.ldapEstablishment = ldapEstablishment;
	}

	public LdapPerson getLdapPerson() {
		return ldapPerson;
	}

	public void setLdapPerson(LdapPerson ldapPerson) {
		this.ldapPerson = ldapPerson;
	}

	/**
	 * @return the domainService
	 */
	public IDomainService getDomainService() {
		return domainService;
	}

	/**
	 * @param domainService the domainService to set
	 */
	public void setDomainService(IDomainService domainService) {
		this.domainService = domainService;
	}
	public UserAttribute getUserAttributeMapping() {
		return userAttributeMapping;
	}

	public void setUserAttributeMapping(UserAttribute userAttributeMapping) {
		this.userAttributeMapping = userAttributeMapping;
	}


}
