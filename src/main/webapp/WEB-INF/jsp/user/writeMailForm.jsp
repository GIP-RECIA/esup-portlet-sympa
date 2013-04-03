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

<%-- 
<c:set var="emailUtil" value="${param.emailUtil}" />
<c:set var="mail" value="${param.mail}" />
--%>

<div id="simpleSmtpDialog" title="<spring:message code="simpleEmail.title"/>" style="display:none">
	<input id = "emailUtilType" type="hidden" value="${emailUtil == null ? 'SIMPLE' : emailUtil.type}"/>
	<input id = "emailUtilURL" type="hidden" value="${emailUtil == null ? '' : emailUtil.url}"/>

	<label id="simple_email_from_label">
		<spring:message code="simpleEmail.from"/>
	</label>
	<label id="simple_email_from">${mail}</label>
	
	<label id="simple_email_to_label">
		<spring:message code="simpleEmail.to"/>
	</label>
	<label id="simple_email_to"></label>
	
	<label id="simple_email_subject_label">
		<spring:message code="simpleEmail.subject"/>
	</label>
	<span id="simple_email_subject_span">
		<input type="text" id="simple_email_subject" value="" />
	</span>
	
	<label id="simple_email_message_label">
		<spring:message code="simpleEmail.message"/>
	</label>
	
	<div id="simple_email_message_div">
		<textarea id="simple_email_message"></textarea>
	</div> 
	  
	<div style="display:none" id="simpleSmtpSubmitText">
		<spring:message code="simpleEmail.send"/>
	</div>
	<div style="display:none" id="simpleSmtpCancelText">
		<spring:message code="simpleEmail.cancel"/>
	</div>
</div>