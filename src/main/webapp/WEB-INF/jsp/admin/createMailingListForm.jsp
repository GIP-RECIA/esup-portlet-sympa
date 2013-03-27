<%@ include file="/WEB-INF/jsp/includes.jsp"%>

<div id = "createListURL_type" style="display:none">${type}</div>
<div id = "createListURL_typeParam" style="display:none">${typeParam}</div>
<div id = "createListURL_typeParamName" style="display:none">${typeParamName}</div>

<c:if test="${not empty userAttributes}">
	<input type="hidden" id="userAttributes" value="${userAttributes}" />
</c:if>

<div>
  <spring:message code="esupsympaCreateList.groupListTitle" />
</div>
<ul class="createListGroupChoice">
  <c:forEach var="ea" items="${editorsAliases}">
    <li class="createListGroupChoice">
      <input type="checkbox" name="${ea.name}" value="${ea.name}"
        <c:if test="${ea.checked}">checked</c:if> <c:if test="${!ea.editable}">readonly disabled</c:if> />
      ${ea.name}
      <input type="hidden" name="idRequest" value="${ea.idRequest}"/>
    </li>
  </c:forEach>

<input type="checkbox" id="createListSubscriberCheckbox" value="${subscribersGroup}">
<spring:message code="esupsympaCreateList.subscriberChoice" /></input>

</ul>

<div id="createListBody" style="width: 800px; overflow: auto;">

  <div class="createListBoxContainer" id="createListNonSelectedGroups">

    <div class="createListBoxHeader"><spring:message code="esupsympaCreateList.otherGroupsChoice" /></div>
    <img id="createListShow" src="<spring:url value="/media/icons/plus.png"/>"/>
    <img id="createListHide" src="<spring:url value="/media/icons/minus.png"/>"/>
    <div class="createListBox createListShowHideToggle">
      <div id="createListTree"></div>
    </div>
  </div>

  <div class="createListArrowButtonsBox createListShowHideToggle" style="width: 20px; float: left;">
    <img id="right_arrow" src="<spring:url value="/media/icons/right_arrow.png"/>"
      style="padding-bottom: 20px;" />
    <img id="left_arrow" src="<spring:url value="/media/icons/left_arrow.png"/>" />
  </div>

  <div id="createListSelectedGroups" class="createListBoxContainer createListShowHideToggle" style="width: 40%;">
    <div class="createListBoxHeader"><spring:message code="esupsympaCreateList.selectedOtherGroupsChoice" /></div>
    <div class="createListBox">
      <ul id="createListSelectedGroups_list" >
       
      </ul>
    </div>
  </div>

</div>