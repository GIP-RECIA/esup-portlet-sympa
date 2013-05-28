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

<c:set var="namespace"><portlet:namespace /></c:set>
<portlet:actionURL var="actionURL" />
<div id="portletNamespace" style="display: none;">${namespace}</div>

<div class="sympaTopLevel">

	<c:choose>
	<c:when test="${not empty listAdmin and listAdmin == true}">
		<%-- If administrator --%>
		<jsp:include page="/WEB-INF/jsp/admin/adminView.jsp" />
	</c:when>
	<c:otherwise>
		<%-- else simple user --%>
		<jsp:include page="/WEB-INF/jsp/user/userView.jsp" />
	</c:otherwise>
	</c:choose>

</div>