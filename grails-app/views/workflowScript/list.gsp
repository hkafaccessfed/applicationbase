
<html>
  <head>  
    <meta name="layout" content="internal" />
  </head>
  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link controller="adminDashboard"><g:message code="branding.nav.breadcrumb.admin"/></g:link> <span class="divider">/</span></li>
      <li class="active"><g:message code="branding.nav.breadcrumb.workflow.script"/></li>
      
      <li class="pull-right"><strong><g:link controller="workflowScript" action="create"><g:message code="branding.nav.breadcrumb.workflow.script.create"/></g:link></strong></li>
    </ul>

    <g:render template="/templates/flash" />

    <h2><g:message code="views.aaf.base.workflow.script.list.heading" /></h2>
    
      <table class="table table-borderless table-sortable">
        <thead>
          <tr>
            <th><g:message code="label.name" default="Name"/></th>
            <th><g:message code="label.description" default="Description"/></th>
            <th/>
          </tr>
        </thead>
        <tbody>
        <g:each in="${scriptList}" var="p" status="i">
          <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
            <td>${fieldValue(bean: p, field: "name")}</td>
            <td>${fieldValue(bean: p, field: "description")}</td>
            <td>
              <g:link controller='workflowScript' action='show' id="$p.id" class="btn"><g:message code="label.view"/></g:link>
            </td>
          </tr>
        </g:each>
        </tbody>
      </table>
  </body>
</html>
