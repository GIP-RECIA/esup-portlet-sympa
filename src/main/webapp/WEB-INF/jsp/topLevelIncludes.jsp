<%--
Includes in the top level jsp page (such as AdminView or UserView)

--%>

<link rel="stylesheet" href="<spring:url value="/css/jquery-ui-1.8.16.custom.css" />" 
	type="text/css"  media="screen, projection">

<link rel="stylesheet" href="<spring:url value="/css/esupsympa.css" />" 
	type="text/css"  media="screen, projection">
    

<script type="text/javascript" src="<spring:url value="/js/jquery-1.6.4.js" />">
</script>

<script type="text/javascript" src="<spring:url value="/js/jquery-ui-1.8.16.custom.js" />">
</script>

<script type="text/javascript" src="<spring:url value="/js/esupsympa.js" />">                                                     
</script>

<script type="text/javascript" src="<spring:url value="/js/jquery.cookie.js" />">
</script>
    
<input type="hidden" id="emailUtilType" value="${emailUtil == null ? 'SIMPLE' : emailUtil.type}" />
<input type="hidden" id="emailUtilURL" value="${emailUtil == null ? '' : emailUtil.url}" />

<input type="hidden" id="userInfo" value="${fn:escapeXml(userInfo)}" />
<input type="hidden" id="userInfoMV" value="${fn:escapeXml(userInfoMV)}" />
