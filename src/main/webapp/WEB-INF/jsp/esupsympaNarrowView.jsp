<%--

    Licensed to ESUP-Portail under one or more contributor license
    agreements. See the NOTICE file distributed with this work for
    additional information regarding copyright ownership.

    ESUP-Portail licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file except in
    compliance with the License. You may obtain a copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

--%>
<%@ include file="/WEB-INF/jsp/includes.jsp" %>
 <link href="<c:url value="/media/css/sympa-portlet.css"/>" type="text/css" rel="stylesheet"/>  
<%-- Include the part to send email --%>
	<jsp:include page="/WEB-INF/jsp/writeMailForm.jsp" />
	 <link href="<c:url value="/media/css/mobilePortletSympa.css"/>"
	type="text/css" rel="stylesheet" /> 
	
	<link href="<c:url value="/media/css/esupsympa.css"/>"
	type="text/css" rel="stylesheet" /> 
<style type="text/css" media="screen">


            /* 
            Max width before this PARTICULAR table gets nasty
            This query will take effect for any screen smaller than 768px
            and also iPads specifically.
            */

            /*
             * Table reflow headers for sympa-result in mobile view
             * TODO when CSS4 element() function will land in browsers other than FF -> remove these inline CSS. http://caniuse.com/#feat=css-element-function 
             */

            @media 
                only screen and (max-width: 768px)  {





      /*
      Table header Label of the data
      <th>Liste</th>
      <th>Sujet</th>
      <th>Abonné</th>
      <th>Propriétaire</th>
      <th>Modérateur</th>
      */
                    .sympa-result td:nth-of-type(1):before { content: "<spring:message code="list.name" /> :"; font-weight: 700; text-align: left; }
                    .sympa-result td:nth-of-type(2):before { content: "<spring:message code="list.subject" /> :"; font-weight: 700; text-align: left; }
                    .sympa-result td:nth-of-type(3):before { content: "<spring:message code="list.subscriber" /> :"; font-weight: 700; text-align: left; }
                    .sympa-result td:nth-of-type(4):before { content: "<spring:message code="list.owner" /> :"; font-weight: 700; text-align: left;}
                    .sympa-result td:nth-of-type(5):before { content: "<spring:message code="list.editor" /> :"; font-weight: 700; text-align: left;}

            }

            /*
             * Table reflow headers for sympa-result in multi-columns dashboard
             * TODO when CSS4 element() function will land in browser -> remove these inline CSS. http://caniuse.com/#feat=css-element-function 
             */

            @media only screen and (min-width: 992px) and (max-width: 3840px), and (min-device-width: 992px) and (max-device-width: 3840px) {
                
.portal-page-column.col-md-7 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(1):before { content: "<spring:message code="list.name" /> :";font-weight: 700; }
.portal-page-column.col-md-7 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(2):before { content: "<spring:message code="list.subject" /> :";font-weight: 700; }
.portal-page-column.col-md-7 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(3):before { content: "<spring:message code="list.subscriber" /> :";font-weight: 700; }
.portal-page-column.col-md-7 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(4):before { content: "<spring:message code="list.owner" /> :"; font-weight: 700;}
.portal-page-column.col-md-7 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(5):before { content: "<spring:message code="list.editor"  /> :"; font-weight: 700;}

.portal-page-column.col-md-6 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(1):before { content: "<spring:message code="list.name" /> :";font-weight: 700; }
.portal-page-column.col-md-6 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(2):before { content: "<spring:message code="list.subject" /> :";font-weight: 700; }
.portal-page-column.col-md-6 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(3):before { content: "<spring:message code="list.subscriber" /> :";font-weight: 700; }
.portal-page-column.col-md-6 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(4):before { content: "<spring:message code="list.owner" /> :"; font-weight: 700;}
.portal-page-column.col-md-6 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(5):before { content: "<spring:message code="list.editor"  /> :"; font-weight: 700;}

.portal-page-column.col-md-5 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(1):before { content: "<spring:message code="list.name" /> :";font-weight: 700; }
.portal-page-column.col-md-5 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(2):before { content: "<spring:message code="list.subject" /> :";font-weight: 700; }
.portal-page-column.col-md-5 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(3):before { content: "<spring:message code="list.subscriber" /> :";font-weight: 700; }
.portal-page-column.col-md-5 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(4):before { content: "<spring:message code="list.owner" /> :"; font-weight: 700;}
.portal-page-column.col-md-5 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(5):before { content: "<spring:message code="list.editor"  /> :"; font-weight: 700;}
    
.portal-page-column.col-md-4 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(1):before { content: "<spring:message code="list.name" /> :";font-weight: 700; }
.portal-page-column.col-md-4 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(2):before { content: "<spring:message code="list.subject" /> :";font-weight: 700; }
.portal-page-column.col-md-4 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(3):before { content: "<spring:message code="list.subscriber" /> :";font-weight: 700; }
.portal-page-column.col-md-4 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(4):before { content: "<spring:message code="list.owner" /> :"; font-weight: 700;}
.portal-page-column.col-md-4 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(5):before { content: "<spring:message code="list.editor"  /> :"; font-weight: 700;}

.portal-page-column.col-md-3 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(1):before { content: "<spring:message code="list.name" /> :";font-weight: 700; }
.portal-page-column.col-md-3 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(2):before { content: "<spring:message code="list.subject" /> :";font-weight: 700; }
.portal-page-column.col-md-3 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(3):before { content: "<spring:message code="list.subscriber" /> :";font-weight: 700; }
.portal-page-column.col-md-3 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(4):before { content: "<spring:message code="list.owner" /> :"; font-weight: 700;}
.portal-page-column.col-md-3 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(5):before { content: "<spring:message code="list.editor"  /> :"; font-weight: 700;}

.portal-page-column.col-md-2 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(1):before { content: "<spring:message code="list.name" /> :";font-weight: 700; }
.portal-page-column.col-md-2 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(2):before { content: "<spring:message code="list.subject" /> :";font-weight: 700; }
.portal-page-column.col-md-2 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(3):before { content: "<spring:message code="list.subscriber" /> :";font-weight: 700; }
.portal-page-column.col-md-2 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(4):before { content: "<spring:message code="list.owner" /> :"; font-weight: 700;}
.portal-page-column.col-md-2 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(5):before { content: "<spring:message code="list.editor"  /> :"; font-weight: 700;}
    
.portal-page-column.col-md-1 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(1):before { content: "<spring:message code="list.name" /> :";font-weight: 700; }
.portal-page-column.col-md-1 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(2):before { content: "<spring:message code="list.subject" /> :";font-weight: 700; }
.portal-page-column.col-md-1 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(3):before { content: "<spring:message code="list.subscriber" /> :";font-weight: 700; }
.portal-page-column.col-md-1 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(4):before { content: "<spring:message code="list.owner" /> :"; font-weight: 700;}
.portal-page-column.col-md-1 .sympa-portlet.bootstrap-styles-by-6 .col-md-12 table.reflow.sympa-result tbody td:nth-of-type(5):before { content: "<spring:message code="list.editor"  /> :"; font-weight: 700;}

         }

        </style>

<c:set var="namespace"><portlet:namespace/></c:set>
<portlet:actionURL var="actionURL">
</portlet:actionURL>

<div class="container-fluid sympa-portlet awesome-bootstrap-checkbox bootstrap-styles-by-6">
<div class="row">

<!-- begin recia custom -->
<!--
<div class="col-xs-12 col-sm-12 col-md-12">
<nav class="navbar navbar-default">
   <div class="container-fluid">

      <div class="navbar-header">
         <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
         </button>
         <a class="navbar-brand sympa-logo hidden-sm" href="javascript:void(0);"><img src="<%=request.getContextPath()%>/media/icons/sympa32.png" width="32px" height="32px" alt="Logo Sympa" /> </a>
      </div>

      
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
         <ul class="nav navbar-nav">
            <li class="active"><a href="#">Mes listes<span class="sr-only"> (current)</span></a></li>
            <li><a href="#">Cr&eacute;ation de listes</a></li>
            <li><a href="#">Modification de listes</a></li>
            <li class="dropdown">
               <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Administrer autre &eacute;tablissement <span class="caret"></span></a>
               <ul class="dropdown-menu">
                  <li><a href="#">Action</a></li>
                  <li><a href="#">Another action</a></li>
                  <li><a href="#">Something else here</a></li>
                  <li role="separator" class="divider"></li>
                  <li><a href="#">Separated link</a></li>
                  <li role="separator" class="divider"></li>
                  <li><a href="#">One more separated link</a></li>
               </ul>
            </li>
         </ul>
      </div>
   </div>
</nav>
</div>
-->
<!-- end recia custom -->

<h3 class="portlet-section-header"><span class="glyphicon glyphicon-envelope bg-primary" aria-hidden="true"></span> <spring:message code="title" htmlEscape="true"/></h3>
<div id="sympalink"><p><a href="${homeUrl}" class="btn btn-default btn-sm" target="blank"><span class="glyphicon glyphicon-circle-arrow-right" aria-hidden="true"></span> <spring:message code="gotoSympa" htmlEscape="true"/></a></p></div>

<%-- create list --%>
<c:if test="${not empty createList and fn:length(createList) gt 0}">
	<div class="portlet-msg-info">
		<c:forEach items="${createList}" var="create">
			<a class="portlet-menu-item btn btn-default btn-sm" href="<c:out value="${create.accessUrl}" escapeXml="true"/>"><span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span> <spring:message code="createNewList" htmlEscape="true"/> @<c:out value="${create.serverName}" escapeXml="true"/></a><br/>
		</c:forEach>
	</div>
</c:if>
<form method="post" class="c" action="<c:out value="${actionURL}" escapeXml="true"/>">
     <div class="form-group col-md-12">
	<span><spring:message code="search.title" htmlEscape="true"/> :</span>
        <div class="visible-xs-block"> </div>
	<spring:bind path="searchForm.subscriber">
            <div class="checkbox checkbox-success checkbox-inline">
		<c:choose>
		<c:when test="${status.value == true}">
			<input type="checkbox" name="${status.expression}" value="true" id="${namespace}_${status.expression}" class="styled" checked="checked" />
		</c:when>
		<c:otherwise>
			<input type="checkbox" name="${status.expression}" value="true" id="${namespace}_${status.expression}" class="styled" />
		</c:otherwise>
		</c:choose>
		<label for="${namespace}_${status.expression}" class="portlet-form-field-label"><spring:message code="search.subscriber" htmlEscape="true"/></label>
                <input type="hidden" name="_${status.expression}"/>
            </div>
	</spring:bind>
        <div class="visible-xs-block"> </div>
	<spring:bind path="searchForm.owner">
            <div class="checkbox checkbox-danger checkbox-inline">
		<c:choose>
		<c:when test="${status.value == true}">
			<input type="checkbox" name="${status.expression}" value="true" id="${namespace}_${status.expression}" class="styled" checked="checked" />
		</c:when>
		<c:otherwise>
			<input type="checkbox" name="${status.expression}" value="true" id="${namespace}_${status.expression}" class="styled" />
		</c:otherwise>
		</c:choose>
		<label for="${namespace}_${status.expression}" class="portlet-form-field-label"><spring:message code="search.owner" htmlEscape="true"/></label>
                <input type="hidden" name="_${status.expression}" value="false"/>
            </div>
	</spring:bind>
        <div class="visible-xs-block"> </div>
	<spring:bind path="searchForm.editor">
            <div class="checkbox checkbox-warning checkbox-inline">
		<c:choose>
		<c:when test="${status.value == true}">
			<input type="checkbox" name="${status.expression}" value="true" id="${namespace}_${status.expression}" class="styled" checked="checked" />
		</c:when>
		<c:otherwise>
			<input type="checkbox" name="${status.expression}" value="true" id="${namespace}_${status.expression}" class="styled" />
		</c:otherwise>
		</c:choose>
		<label for="${namespace}_${status.expression}" class="portlet-form-field-label"><spring:message code="search.editor" htmlEscape="true"/></label>
                <input type="hidden" name="_${status.expression}" value="false"/>
            </div>
	</spring:bind>
        <div class="visible-xs-block"> </div>
	<input type="submit"  class="portlet-form-button btn btn-default" value="<spring:message code="search.validate" htmlEscape="true"/>"/>
    </div>
</form>
</div>
<div class="row">
<c:choose>
<c:when test="${not empty sympaList and fn:length(sympaList) gt 0}">
     <div class="col-md-12">
	<table class="data centered reflow sympa-result table-striped" summary="<spring:message code="results.summary" htmlEscape="true"/>">
		<caption class="portlet-table-subheader" style="min-width:140px;"><spring:message code="results.caption" arguments="${fn:length(sympaList)}" htmlEscape="true"/></caption>
		<thead class="portlet-table-header">
			<tr>
				<th scope="col"><spring:message code="list.name" /></th>
				<th scope="col"><spring:message code="list.subject" /></th>
				<th scope="col"><spring:message code="list.subscriber" /></th>
				<th scope="col"><spring:message code="list.owner" /></th>
				<th scope="col"><spring:message code="list.editor"  /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${sympaList}" var="list" varStatus="varStatus">
			<tr<c:if test="${varStatus.index%2!=0}"> class="portlet-table-alternate"</c:if>>
				<%-- <td><a class="portlet-menu-item"  
				
				
				title="<spring:message code="gotoList" arguments="${list.address}" 
				onclick="$('#simpleSmtpDialog').toggleClass('visible');" 
				title="<spring:message code="simpleEmail.title" 
				htmlEscape="true"/>"><c:out value="${list.address}" escapeXml="true"/></a>
  </td> --%>
<td>
					<c:choose>
	        			<c:when test="${list.editor==true}">
							<a class="portlet-menu-item mailLink" 
					  			href="" 
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
				<td class="c"><insa:icon value="${list.subscriber}"/></td>
				<td class="c">
					<c:choose>
					<c:when test="${list.owner==true}">
					<a class="portlet-menu-item btn btn-default btn-xs" href="<c:out value="${list.listAdminUrl}" escapeXml="true"/>" target="_blank" title="<spring:message code="gotoListAdmin" arguments="${list.address}" htmlEscape="true"/>"><insa:icon value="${list.owner}"/></a>
					</c:when>
					<c:otherwise>
					<insa:icon value="${list.owner}"/>
					</c:otherwise>
					</c:choose>
				</td>
				<td class="c"><insa:icon value="${list.editor}"/></td>
			</tr>
			</c:forEach>
		</tbody>
	</table>
    </div>
</c:when>
<c:otherwise>
	<div class="portlet-msg-alert col-md-12"><p class="bg-danger"><spring:message code="results.noResults" htmlEscape="true"/></p></div>
</c:otherwise>
</c:choose>
</div>

<!-- begin recia custom -->
<!--
<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#reciaModal">
   Lancer la modale (demo)
</button>


<div class="modal fade" id="reciaModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
   <div class="modal-dialog" role="document">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <h4 class="modal-title" id="myreciaModalLabel">Modal title</h4>
         </div>
         <div class="modal-body">
            <div class="col-xs-12 col-sm-6 col-md-6 recia-treeview">
               <div class="panel panel-default">
                  <div class="panel-heading">
                     <h3 class="panel-title">Treeview</h3>
                  </div>
                  <div class="panel-body">
                     Panel content
                     <br>Panel content
                     <br>Panel content
                     <br>Panel content
                     <br>Panel content
                     <br>Panel content
                     <br>Panel content
                     <br>Panel content
                     <br>Panel content
                     <br>Panel content
                     <br>
                  </div>
               </div>
            </div>
            <div class="col-xs-2 col-sm-2 col-md-1 modal-action-buttons">
               <button type="button" class="btn btn-primary btn-xs hidden-sm hidden-md hidden-lg" style="margin-top:-50.5px">
                  <span class="glyphicon glyphicon-arrow-down" title="Ajouter" aria-label="Ajouter"></span>
               </button>
               <button type="button" class="btn btn-primary btn-xs hidden-xs" style="margin-top:50%;">
                  <span class="glyphicon glyphicon-arrow-right" title="Ajouter" aria-label="Ajouter"></span>
               </button>

            </div>
            <div class="col-xs-12 col-sm-6 col-md-6 recia-target">
               <div class="panel panel-default">
                   <div class="panel-heading">
                      <h3 class="panel-title">Target</h3>
                   </div>
                   <div class="panel-body">
                       Panel content
                   </div>
               </div>
            </div>
            <div class="col-xs-12 col-sm-12 col-md-12 modal-footer">
               <button type="button" class="btn btn-default" data-dismiss="modal">Annuler</button>
               <button type="button" class="btn btn-primary">Valider</button>
            </div>
        </div>
      </div>
   </div>
</div>
-->
<!-- end recia custom -->

</div>


<%@ include file="/WEB-INF/jsp/resources.jsp" %>

<script type="text/javascript" src="<spring:url value="/js/jquery.cookie.js" />">
</script>

<script type="text/javascript" src="<spring:url value="/js/jquery.jstree.js" />">
</script>      
      
