<%--

    Copyright (C) 2010 INSA LYON http://www.insa-lyon.fr
    Copyright (C) 2010 Esup Portail http://www.esup-portail.org
    @Author (C) 2010 Olivier Franco <Olivier.Franco@insa-lyon.fr>
    @Contributor (C) 2010 Doriane Dusart <Doriane.Dusart@univ-valenciennes.fr>
    @Contributor (C) 2010 Jean-Pierre Tran <Jean-Pierre.Tran@univ-rouen.fr>
    @Contributor (C) 2010 Vincent Bonamy <Vincent.Bonamy@univ-rouen.fr>

    Licensed under the GPL License, (please see the LICENCE file)

--%>

<%@ include file="/WEB-INF/jsp/includes.jsp"%>

<input type="hidden" id="userUAI" value="${uai}" />
<input type="hidden" id="ajaxServletUrl" value="<spring:url value="/servlet-ajax" />" />

<!-- A div to make sure there are no css conflicts -->
<div class="sympaTopLevel">
	
	<!-- This div will become the tabs -->
	<div id="esupsympaAdminTabs" style="display:none">
		<ul>
			<li>
				<a href="#tabs-1">
					<spring:message code="esupsympaAdmin.myLists.title" htmlEscape="true" />
				</a>
			</li>
			<li>
				<a href="#tabs-2">
					<spring:message code="esupsympaAdmin.createList.title" htmlEscape="true" />
				</a>
			</li>
		</ul>
		<div id="tabs-1">
			<jsp:include page="/WEB-INF/jsp/user/mailingListsListingPane.jsp" />
		</div>
		
		<div id="tabs-2">
			<jsp:include page="/WEB-INF/jsp/admin/createMailingListPane.jsp" />
		</div>
	</div>
</div>
