<html>
  <head>
    <meta name="layout" content="public" />
  </head>
  
  <body>
    <div class="row">
      <div class="span12">
        <h2><g:message encodeAs='HTML' code="views.aaf.base.identity.auth.federatederror.heading"/></h2>

        <div class="alert alert-block alert-error">
          <p><g:message encodeAs='HTML' code="views.aaf.base.identity.auth.federatederror.details"/></p>
        </div>

        <p><g:message encodeAs='HTML' code="branding.application.supportdesk"/></p>
        
        <br><br><br>

        <h4 class="muted">Complete Request Details</h4>
        <g:include controller="auth" action="echo" />
      </div>
    </div>
  </body>
</html>
