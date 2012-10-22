
<html>
  <head>
    <meta name="layout" content="internal" />
  </head>
  
  <body>
    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link controller="adminDashboard"><g:message code="branding.nav.breadcrumb.admin"/></g:link> <span class="divider">/</span></li>
      <li><g:link controller="workflowProcess" action="list"><g:message code="branding.nav.breadcrumb.workflow.process"/></g:link> <span class="divider">/</span></li>
      <li class="active">${fieldValue(bean: process, field: "name")}</li>
    </ul>

    <g:render template="/templates/flash" />
    <g:render template="/templates/errors_bean" model="['bean':process]"/>

    <h2><g:message code="views.aaf.base.workflow.process.show.heading" args="[process.name]"/></h2>

    <ul class="nav nav-tabs">
      <li class="active"><a href="#tab-overview" data-toggle="tab"><g:message code="label.overview" /></a></li>

      <li class="dropdown pull-right">
        <a class="dropdown-toggle" data-toggle="dropdown" href="#">
          <g:message code="label.actions" />
          <b class="caret"></b>
        </a>
        <ul class="dropdown-menu">
          <li>
            <g:link controller="workflowProcess" action="edit" id="${process.id}"><g:message code="label.edit"/></g:link>
          </li>
        </ul>
      </li>
    </ul>

    <div class="tab-content">
      <div id="tab-overview" class="tab-pane active">
        <div class="row">
          <div class="span4">
          <table class="table table-borderless">
            <tbody>   
              <tr>
                <th><g:message code="label.name" /></th>
                <td>${fieldValue(bean: process, field: "name")}</td>
              </tr>
              <tr>
                <th><g:message code="label.description" /></th>
                <td>${fieldValue(bean: process, field: "description")}</td>
              </tr>
              <tr>
                <th><g:message code="label.active" /></th>
                <td>${fieldValue(bean: process, field: "active")}</td>
              </tr>
              <tr>
                <th><g:message code="label.version" /></th>
                <td>${fieldValue(bean: process, field: "processVersion")}</td>
              </tr>
              <tr>
                <th><g:message code="label.created" /></th>
                <td>${fieldValue(bean: process, field: "dateCreated")}</td>
              </tr>
            </tbody>
          </table>
          </div>

          <div class="span8">
            <pre>${fieldValue(bean: process, field: "definition")}</pre>
          </div>
        </div>
      </div>
    </div>
    
  </body>
</html>
