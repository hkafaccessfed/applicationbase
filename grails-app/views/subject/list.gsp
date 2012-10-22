<html>
  <head>
    <meta name="layout" content="internal">
  </head>
  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link controller="adminDashboard"><g:message code="branding.nav.breadcrumb.admin"/></g:link> <span class="divider">/</span></li>
      <li class="active"><g:message code="branding.nav.breadcrumb.subject"/></li>
    </ul>

    <g:render template="/templates/flash" />
    
    <h2><g:message code="views.aaf.base.admin.subject.list.heading" /></h2>

    <table class="table table-borderless table-sortable">
      <thead>
        <tr>
          <th class="hidden-phone"><g:message code="label.id" /></th>
          <th class="hidden-phone hidden-tablet"><g:message code="label.principal" /></th>
          <th><g:message code="label.displayname" /></th>
          <th><g:message code="label.email" /></th>
          <th/>
        </tr>
      </thead>
      <tbody>
      <g:each in="${subjects}" status="i" var="subject">
        <tr>
          <td class="hidden-phone">${fieldValue(bean: subject, field: "id")}</td>
          <td class="hidden-phone hidden-tablet">${fieldValue(bean: subject, field: "principal")}</td>
          <td>${fieldValue(bean: subject, field: "cn")}</td>
          <td>${fieldValue(bean: subject, field: "email")}</td>
          <td><g:link action="show" id="${subject.id}" class="btn btn-small"><g:message code="label.view"/></g:link>
        </tr>
      </g:each>
      </tbody>
    </table>

  </body>
</html>
