<%@ include file="/WEB-INF/jsp/includes.jsp"%>

<%-- Dialog div configuration which will open with the create list form. --%>

<div class="validButtonText" style="display:none">
	<spring:message code="esupsympaCreateList.createButton"/> 
</div>

<div class="cancelButtonText" style="display:none">
	<spring:message code="esupsympaCreateList.cancelButton"/>
</div>

<div class="subTitleText" style="display:none">
	<div class="subTitle">
		<spring:message code="esupsympaCreateList.createListSubTitle" htmlEscape="true" />
 	</div>
</div>
	
<%-- The create list form dialog is configured for creation --%>
<%-- Dialog div configuration which will open with the create list form. --%>
<div id="createListDialog" class="createListDialog action:CREATE" style="display: none" 
	title="<spring:message code="esupsympaCreateList.createListTitle"/>" >
</div>

<div class="okButtonText" style="display:none">
	<spring:message code="esupsympaCreateListTable.contexthelp.close"/>
</div>

<div id="createContextHelpDialog" class="contextHelpDialog" style="display:none"
	title="<spring:message code="esupsympaCreateListTable.contexthelp.title" htmlEscape="true"/>">
	<div class="desc">
		<spring:message code="esupsympaCreateListTable.contexthelp.desc"/>
	</div>
	<div >
		<spring:message code="esupsympaCreateListTable.contexthelp.text"/>
	</div>
</div>

<%-- End Dialog div configuration. --%>

<div class="subTitle">
	<spring:message code="esupsympaCreateListTable.subTitle" />
</div>

<div>
	<spring:message code="esupsympaCreateListTable.desc"/>
	<img class="contextHelpButton" src="<c:url value="/media/icons/context_help.png"/>"
        alt="<spring:message code="esupsympaCreateListTable.contexthelp.title"/>"
        title="<spring:message code="esupsympaCreateListTable.contexthelp.title"/>" />
</div>

<div id="createListTableBox">
	<table id="createListTable">
		<thead class="tableHeader">
			<tr>
			  	<td><spring:message code="esupsympaCreateListTable.header.name"/></td>
				<td><spring:message code="esupsympaCreateListTable.header.description"/></td>
				<td></td>
			</tr>
		</thead>
		<tbody>
		<c:forEach var="rowEntry" items="${createTableData}" >
			<tr>
			  	<td>
			  		${rowEntry.name}  
					<input type="hidden" name="modelId" value="${rowEntry.modelId}"/> 
					<input type="hidden" name="modelParam" value="${rowEntry.modelParam}"/>
				</td>
				<td>
					<div class="listDescription">${rowEntry.subject}</div>
				</td>
				<td>
					<img class="createListButton" src="<c:url value="/media/icons/plus.png"/>"
						alt="<spring:message code="esupsympaCreateListTable.createButton"/>"
						title="<spring:message code="esupsympaCreateListTable.createButton"/>" />
			  	</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>