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

<c:set var="namespace"><portlet:namespace/></c:set>
<portlet:actionURL var="actionURL" />

<%-- Include the part to send email --%>
<jsp:include page="/WEB-INF/jsp/user/writeMailForm.jsp" />

<%-- Search bar to filter mailing lists lisiting --%>
<form method="post" class="c userListCriteriaForm" action="<c:out value="${actionURL}" escapeXml="true"/>">
	<spring:bind path="searchForm.tabIndex">
  		<input type="hidden" class="tabIndex" id="${namespace}_${status.expression}" name="${status.expression}" value="${searchForm.tabIndex}"/>
	</spring:bind>

	<spring:bind path="searchForm.invalidateCache">
  		<input type="hidden" class="invalidateCache" id="${namespace}_${status.expression}" name="${status.expression}" value="${searchForm.invalidateCache}"/>
	</spring:bind>
	
	<span><spring:message code="search.title" htmlEscape="true"/> : </span>
	<spring:bind path="searchForm.subscriber">
		<c:choose>
		<c:when test="${status.value == true}">
			<input type="checkbox" name="${status.expression}" value="true" 
				id="${namespace}_${status.expression}" checked="checked"/>
			<input type="hidden" name="_${status.expression}"/>
		</c:when>
		<c:otherwise>
			<input type="checkbox" name="${status.expression}" value="true" 
				id="${namespace}_${status.expression}"/>
			<input type="hidden" name="_${status.expression}"/>
		</c:otherwise>
		</c:choose>
		<label for="${namespace}_${status.expression}" class="portlet-form-field-label">
			<spring:message code="search.subscriber" htmlEscape="true"/>
		</label>
	</spring:bind>
	<spring:bind path="searchForm.owner">
		<c:choose>
		<c:when test="${status.value == true}">
			<input type="checkbox" name="${status.expression}" value="true" 
				id="${namespace}_${status.expression}" checked="checked"/>
			<input type="hidden" name="_${status.expression}" value="false"/>
		</c:when>
		<c:otherwise>
			<input type="checkbox" name="${status.expression}" value="true" 
				id="${namespace}_${status.expression}"/>
			<input type="hidden" name="_${status.expression}" value="false"/>
		</c:otherwise>
		</c:choose>
		<label for="${namespace}_${status.expression}" class="portlet-form-field-label">
			<spring:message code="search.owner" htmlEscape="true"/>
		</label>
	</spring:bind>
	<spring:bind path="searchForm.editor">
		<c:choose>
		<c:when test="${status.value == true}">
			<input type="checkbox" name="${status.expression}" value="true" 
				id="${namespace}_${status.expression}" checked="checked"/>
			<input type="hidden" name="_${status.expression}" value="false"/>
		</c:when>
		<c:otherwise>
			<input type="checkbox" name="${status.expression}" value="true" 
				id="${namespace}_${status.expression}"/>
			<input type="hidden" name="_${status.expression}" value="false"/>
		</c:otherwise>
		</c:choose>
		<label for="${namespace}_${status.expression}" class="portlet-form-field-label">
			<spring:message code="search.editor" htmlEscape="true"/>
		</label>
	</spring:bind>
	<button type="submit" class="ui-widget submitUserListsCriteriaButton" 
  		role="button" aria-disabled="false">
  		<span class="ui-button-text">
			<spring:message code="search.validate" htmlEscape="true"/>
		</span>
	</button>
</form>

<%-- Listing of mailing lists --%>
<c:choose>
<c:when test="${not empty sympaList and fn:length(sympaList) gt 0}">
	<table class="userListsTable data centered" border="0" cellpadding="0" cellspacing="0" 
		summary="<spring:message code="results.summary" htmlEscape="true"/>" width="90%">
		<caption class="portlet-table-subheader">
			<spring:message code="results.caption" arguments="${fn:length(sympaList)}" htmlEscape="true"/>
		</caption>
		<thead class="portlet-table-header tableHeader">
			<tr>
				<td><spring:message code="list.name" htmlEscape="true"/></td>
				<td><spring:message code="list.subject" htmlEscape="true"/></td>
				<td class="centered"><spring:message code="list.subscriber" htmlEscape="true"/></td>
				<td class="centered"><spring:message code="list.owner" htmlEscape="true"/></td>
				<td class="centered"><spring:message code="list.editor" htmlEscape="true"/></td>
				<td></td>        
        		<td></td>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${sympaList}" var="list" varStatus="varStatus">
			<tr <c:if test="${varStatus.index%2!=0}">class="portlet-table-alternate"</c:if>>
				<td>
					<c:choose>
	        			<c:when test="${list.editor==true}">
							<a class="portlet-menu-item mailLink" 
					  			href="#" target="_blank" 
					  			title="<spring:message code="simpleEmail.title" 
					  			arguments="${list.address}" htmlEscape="true"/>" >
								<c:out value="${list.address}" escapeXml="true" />
							</a>
	          			</c:when>
	          			<c:otherwise>
	          				<c:out value="${list.address}" escapeXml="true"/>
	          			</c:otherwise>
          			</c:choose>
				</td>
				<td><c:out value="${list.subject}" escapeXml="true"/></td>
				<td class="c centered icon_column_with_title">
					<insa:icon value="${list.subscriber}"/>
				</td>
				<td class="c centered icon_column_with_title">
					<c:choose>
					<c:when test="${list.owner==true}">
						<a class="portlet-menu-item" href="<c:out value="${list.listAdminUrl}" escapeXml="true"/>" 
							target="_blank" title="<spring:message code="gotoListAdmin" arguments="${list.address}" htmlEscape="true"/>">
							<insa:icon value="${list.owner}"/>
						</a>
					</c:when>
					<c:otherwise>
						<insa:icon value="${list.owner}"/>
					</c:otherwise>
					</c:choose>
				</td>
				<td class="c centered icon_column_with_title">
					<insa:icon value="${list.editor}"/>
				</td>
				<td class="centered icon_column_without_title">
					<c:choose>
					<%-- Display archive link for owners and subjects only --%>
					<c:when test="${list.owner==true || list.subscriber==true}">
	        			<a class="portlet-menu-item" target="_blank" 
	        				href="<c:out value="${list.listArchivesUrl}" escapeXml="true"/>" 
	          				title="<spring:message code="gotoListArchives" arguments="${list.address}" htmlEscape="true"/>" >
	          				<img src="<c:url value="/media/icons/archives.png"/>" alt="<c:out value="${iconAlt}" escapeXml="true"/>" />
	          			</a>
					</c:when>
					<c:otherwise>
						&nbsp;
					</c:otherwise>
					</c:choose>	          			
          		</td>
        		<td class="icon_column_without_title">
          			<c:if test="${list.owner==true}">
            		<a class="portlet-menu-item" target="_blank"
            			href="<c:out value="${list.listAdminUrl}" escapeXml="true"/>" 
             			title="<spring:message code="gotoListAdmin" arguments="${list.address}" htmlEscape="true"/>" >
            			<img src="<c:url value="/media/icons/admin_list.png"/>" alt="<c:out value="${iconAlt}" escapeXml="true"/>"/>
            		</a>
          			</c:if>
        		</td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</c:when>
<c:otherwise>
	<div class="portlet-msg-alert">
		<p>
			<spring:message code="results.noResults" htmlEscape="true"/>
		</p>
	</div>
</c:otherwise>
</c:choose>