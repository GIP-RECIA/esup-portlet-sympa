<%--

    Copyright (C) 2010 INSA LYON http://www.insa-lyon.fr
    Copyright (C) 2010 Esup Portail http://www.esup-portail.org
    @Author (C) 2010 Olivier Franco <Olivier.Franco@insa-lyon.fr>
    @Contributor (C) 2010 Doriane Dusart <Doriane.Dusart@univ-valenciennes.fr>
    @Contributor (C) 2010 Jean-Pierre Tran <Jean-Pierre.Tran@univ-rouen.fr>
    @Contributor (C) 2010 Vincent Bonamy <Vincent.Bonamy@univ-rouen.fr>
    @Contributor (C) 2013 Maxime BOSSARD (GIP-RECIA) <mxbossard@gmail.com>

    Licensed under the GPL License, (please see the LICENCE file)

--%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>

<h3 class="portlet-section-header"><spring:message code="title" htmlEscape="true"/></h3>

<%-- We don't want this link anymore, since we have a link by list.
<p>
	<span id="sympalink">
		<img src="<spring:url value="/media/icons/application_go.png" />" />
		<a href="${homeUrl}" target="blank">
			<spring:message code="gotoSympa" htmlEscape="true"/>
		</a>
	</span>
</p>
--%>

<%-- create list
We don't want the create list link since we have an admin tab for this purpose.

<c:if test="${not empty createList and fn:length(createList) gt 0}">
	<div class="portlet-msg-info">
		<c:forEach items="${createList}" var="create">
			<a class="portlet-menu-item" href="<c:out value="${create.accessUrl}" escapeXml="true"/>">
				<spring:message code="createNewList" htmlEscape="true"/> 
				<strong>@<c:out value="${create.serverName}" escapeXml="true"/></strong>
			</a>
			<br/>
		</c:forEach>
	</div>
</c:if>
--%>

<%-- Include the mailing lists listing --%>
<jsp:include page="/WEB-INF/jsp/user/mailingListsListingPane.jsp" />

<%@ include file="/WEB-INF/jsp/resources.jsp" %>