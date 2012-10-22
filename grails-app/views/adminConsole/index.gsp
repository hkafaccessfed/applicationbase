<html>
  <head>
    <meta name="layout" content="public" />
  </head>
  <body>

    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link controller="adminDashboard"><g:message code="branding.nav.breadcrumb.admin"/></g:link> <span class="divider">/</span></li>
      <li class="active"><g:message code="branding.nav.breadcrumb.admin.console"/></li>
    </ul>

    <g:render template="/templates/flash" />

    <h2><g:message code="views.aaf.base.admin.console.heading" /></h2>
    
    <div class="alert alert-error">
      <h4 class="alert-heading"><g:message code="views.aaf.base.admin.console.warning"/></h4>
      <p><g:message code="views.aaf.base.admin.console.details"/></p>
      <p><strong><g:message code="views.aaf.base.admin.console.details.highlight"/></strong></p>
    </div>
    <iframe src ="${grailsApplication.config.grails.serverURL}/internal/console" width="100%" height="1000px" style="border: 0px;">
      <p>Your browser does not support iframes.</p>
    </iframe>

  </body>
</html>
