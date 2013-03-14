<%-- create list --%>

<%@ include file="/WEB-INF/jsp/includes.jsp"%>

<script type="text/javascript" src="/esup-portlet-sympa/js/jquery.jstree.js">
</script>      
      
<script type="text/javascript" src="/esup-portlet-sympa/js/esupsympaCreateListTable.js">
</script>

<script type="text/javascript" src="/esup-portlet-sympa/js/esupsympaCreateList.js">
</script>


<div id="createButtonText" style="display:none">
<spring:message code="esupsympaCreateList.createButton"/> 
</div>
<div id="cancelButtonText" style="display:none">
<spring:message code="esupsympaCreateList.cancelButton"/>
</div>
<div id="okButtonText" style="display:none">
<spring:message code="esupsympaCreateListTable.contexthelp.close"/>
</div>

<div id="contextHelpDialog" 
title="<spring:message code="esupsympaCreateListTable.contexthelp.title" htmlEscape="true"/>" style="display:none">
  <div class="desc"><spring:message code="esupsympaCreateListTable.contexthelp.desc"/></div>
  <div ><spring:message code="esupsympaCreateListTable.contexthelp.text"/></div>
</div>

<div id="createListDialog" title="<spring:message code="esupsympaCreateList.createListTitle"/>" style="display: none">
    
</div>

<div class="subTitle"><spring:message code="esupsympaCreateListTable.subTitle"/></div>
<div><spring:message code="esupsympaCreateListTable.desc"/>
<img class="contextHelpButton" src="<c:url value="/media/icons/context_help.png"/>"
        alt="<c:out value="${iconAlt}" escapeXml="true"/>" />
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
  <c:forEach var="rowEntry" items="${tableData}" >
    <tr>
      <td>${rowEntry.name}  
      <input type="hidden" name="modelId" value="${rowEntry.modelId}"/> 
      <input type="hidden" name="modelParam" value="${rowEntry.modelParam}"/>
      </td>
      <td><div class="listDescription">${rowEntry.subject}</div></td>
      
      <td><img class="createListButton" src="<c:url value="/media/icons/plus.png"/>"
          alt="<c:out value="${iconAlt}" escapeXml="true"/>" />
      </td>
    </tr>
  </c:forEach>
  
  </table>

</div>