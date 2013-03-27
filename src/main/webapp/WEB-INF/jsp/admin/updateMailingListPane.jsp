<%@ include file="/WEB-INF/jsp/includes.jsp"%>

<%-- Dialog div configuration which will open with the create list form. --%>

<div class="validButtonText" style="display:none">
	<spring:message code="esupsympaUpdateList.updateButton" /> 
</div>

<div class="cancelButtonText" style="display:none">
	<spring:message code="esupsympaUpdateList.cancelButton" />
</div>

<div class="closeConfirmText" style="display:none">
	<spring:message code="esupsympaUpdateListTable.closeConfirm" />
</div>

<div class="subTitleText" style="display:none">
	<div class="subTitle">
		<spring:message code="esupsympaUpdateList.subTitle" htmlEscape="true" />
	</div>
</div>

<%-- The create list form dialog is configured for update --%>
<div id="updateListDialog" class="createListDialog action:UPDATE" style="display: none" 
	title="<spring:message code="esupsympaUpdateList.title"/>">
</div>

<div class="okButtonText" style="display:none">
	<spring:message code="esupsympaUpdateListTable.contexthelp.close"/>
</div>

<div id="updateContextHelpDialog" class="contextHelpDialog" style="display:none"
	title="<spring:message code="esupsympaUpdateListTable.contexthelp.title" htmlEscape="true"/>">
	<div class="desc">
		<spring:message code="esupsympaUpdateListTable.contexthelp.desc"/>
	</div>
	<div >
		<spring:message code="esupsympaUpdateListTable.contexthelp.text"/>
	</div>
</div>
<%-- End Dialog div configuration. --%>

<div class="subTitle">
	<spring:message code="esupsympaUpdateListTable.subTitle"/>
</div>

<div>
	<spring:message code="esupsympaUpdateListTable.desc"/>
	<img class="contextHelpButton" src="<c:url value="/media/icons/context_help.png"/>"
       	alt="<spring:message code="esupsympaUpdateListTable.contexthelp.title"/>"
       	title="<spring:message code="esupsympaUpdateListTable.contexthelp.title"/>" />
</div>

<c:choose>
<c:when test="${not empty sympaList and fn:length(sympaList) gt 0}">
	<div id="updateListTableBox">
		<table id="updateListTable">
			<thead class="tableHeader">
				<tr>
				  	<td><spring:message code="esupsympaUpdateListTable.header.name"/></td>
					<td><spring:message code="esupsympaUpdateListTable.header.description"/></td>
					<td></td>
					<td></td>
				</tr>
			</thead>
			<tbody>
			<c:forEach items="${updateTableData}" var="rowEntry" varStatus="varStatus">
				<tr>
				  	<td>
				  		<div class="listName">
				  			<c:out value="${rowEntry.name}" escapeXml="true"/>
				  		</div>
				  		<input type="hidden" name="modelId" value="${rowEntry.modelId}"/> 
						<input type="hidden" name="modelParam" value="${rowEntry.modelParam}"/>
					</td>
					<td>
						<div class="listDescription">
							<c:out value="${rowEntry.subject}" escapeXml="true"/>
						</div>
					</td>
					<td>
						<img class="updateListButton" src="<c:url value="/media/icons/update.png"/>"
							alt="<spring:message code="esupsympaUpdateListTable.updateButton"/>"
							title="<spring:message code="esupsympaUpdateListTable.updateButton"/>" />
				  	</td>
				  	<td>
						<img class="closeListButton" src="<c:url value="/media/icons/close.png"/>"
							alt="<spring:message code="esupsympaUpdateListTable.closeButton"/>"
							title="<spring:message code="esupsympaUpdateListTable.closeButton"/>" />
				  	</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
</c:when>
<c:otherwise>
	<div class="portlet-msg-alert">
		<p>
			<spring:message code="results.noResults" htmlEscape="true"/>
		</p>
	</div>
</c:otherwise>
</c:choose>

<div id="waitingDialog" style="display:none"
	title="<spring:message code="waitingDialog.title"/>">
	<spring:message code="waitingDialog.bodyText" />
</div>

<div id="resultsDialog" style="display:none" 
	title="<spring:message code="esupsympaCreateList.createListTitle" />">
	<div id="resultsDialogText"></div>
</div>