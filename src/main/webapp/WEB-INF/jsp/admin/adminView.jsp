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
<%@ include file="/WEB-INF/jsp/includes.jsp"%>

<portlet:actionURL var="adminActionURL" />

<div id="createListURLBase" style="display:none">${createListURLBase}</div>

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
			<li>
				<a href="#tabs-3">
					<spring:message code="esupsympaAdmin.updateList.title" htmlEscape="true" />
				</a>
			</li>
			<c:if test="${not empty switchEtabMapEntries}">
			<li>
        <form method="post" class="switchEtab-form" action="<c:out value="${adminActionURL}" escapeXml="true"/>">
	        <span>
	          <spring:message code="esupsympaAdmin.switchEtab.selector.title" htmlEscape="true" var="switchEtab_selector_title" />
	          <select name="adminSwitchEtabUai" class="switchEtab-selector" size="1" title="${switchEtab_selector_title}"
	             onchange="submit();">
	          <c:forEach items="${switchEtabMapEntries}" var="entry">
	            <option value="${entry.key}"
	            
	            <c:if test="${entry.key eq uai}">selected="selected"</c:if>
	              
	            >${entry.value}</option>
	          </c:forEach>
	          </select>
	        </span>
        </form>
      </li>
      </c:if>
		</ul>
		<div id="tabs-1">
			<jsp:include page="/WEB-INF/jsp/user/mailingListsListingPane.jsp" />
		</div>
		
		<div id="tabs-2">
			<jsp:include page="/WEB-INF/jsp/admin/createMailingListPane.jsp" />
		</div>
		
		<div id="tabs-3">
			<jsp:include page="/WEB-INF/jsp/admin/updateMailingListPane.jsp" />
		</div>		
	</div>

</div>


<%@ include file="/WEB-INF/jsp/resources.jsp" %>

<script type="text/javascript" src="<spring:url value="/js/jquery.cookie.js" />">
</script>

<script type="text/javascript" src="<spring:url value="/js/jquery.jstree.js" />">
</script>      
      
<script type="text/javascript" src="<spring:url value="/js/esupsympaCreateListTable.js" />">
</script>

<script type="text/javascript" src="<spring:url value="/js/esupsympaCreateList.js" />">
</script>	
	