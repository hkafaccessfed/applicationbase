<html>
  <head>
    <meta name="layout" content="public" />
  </head>
  
  <body>

      <div class="row">
        <div class="span12">
          <div class="hero-unit">
              <h2>Development Login - <g:message code='branding.application.name' /></h2>
              <p>Please choose either federated or local account login for development purposes. For each local account in development mode different access levels are provided.</p>
              <p>This functionality <strong>is not available in production</strong>  (.WAR) deployments.</p>
          </div>
        </div>
      </div>

      <div class="row">
        <div class="span6">
          <h2>Federated Login</h2>
          <p>Login using a Shibboleth SP configured against your local development environemt.</p>
          <a href="${spsession_url}" class="btn btn-info btn-large">Federated Login via Shibboleth SP</a>
        </div>
        <div class="span6">
          <h2>Local Accounts</h2>
          <p>Login using a temporary account for easy development. Modify the posted details to modify the user.</p>
          <div class="">
            <h3>Fred Bloggs</h3>
            <p>Principal: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-1</p>
              <g:form controller="federatedDevelopmentSessions" action="locallogin" method="post">
              <g:hiddenField name="principal" value="https://idp.one.edu.au/idp/shibboleth!https://manager.aaf.edu.au/shibboleth!8698dde3-d9cd-4c44-928c-2b6836bcc4bb" />
              <g:hiddenField name="credential" value="fake-sessionid-webform" />
              <g:hiddenField name="attributes.sharedToken" value="LGW3wpNaPgwnLoYYsghGbz1" />
              <g:hiddenField name="attributes.cn" value="Fred Bloggs" />
              <g:hiddenField name="attributes.email" value="fredbloggs@one.edu.au" />
              <g:hiddenField name="attributes.entityID" value="https://idp.one.edu.au/idp/shibboleth" />

              <g:submitButton name="Login as Fred Bloggs" class="btn btn-success btn-large"/>
            </g:form>
          </div>
          <div class="">
            <h3>Joe Schmoe</h3>
            <p>Principal: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-2</p>
              <g:form controller="federatedDevelopmentSessions" action="locallogin" method="post">
              <g:hiddenField name="principal" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-2" />
              <g:hiddenField name="credential" value="fake-sessionid-webform" />
              <g:hiddenField name="attributes.sharedToken" value="LGW3wpNaPgwnLoYYsghGbz2" />
              <g:hiddenField name="attributes.cn" value="Joe Schmoe" />
              <g:hiddenField name="attributes.email" value="joeschmoe@one.edu.au" />
              <g:hiddenField name="attributes.entityID" value="https://idp.one.edu.au/idp/shibboleth" />

              <g:submitButton name="Login as Joe Schmoe" class="btn btn-warning btn-large"/>
            </g:form>
          </div>
          <div class="">
            <h3>Max Mustermann</h3>
            <p>Principal: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-3</em></strong></p>
              <g:form controller="federatedDevelopmentSessions" action="locallogin" method="post">
              <g:hiddenField name="principal" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-3" />
              <g:hiddenField name="credential" value="fake-sessionid-webform" />
              <g:hiddenField name="attributes.sharedToken" value="LGW3wpNaPgwnLoYYsghGbz3" />
              <g:hiddenField name="attributes.cn" value="Max Mustermann" />
              <g:hiddenField name="attributes.email" value="maxmustermann@one.edu.au" />
              <g:hiddenField name="attributes.entityID" value="https://idp.one.edu.au/idp/shibboleth" />

              <g:submitButton name="Login as Max Mustermann" class="btn btn-large"/>
            </g:form>
          </div>
          <div class="">
            <h3>Иван Петров</h3>
            <p>Principal: https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-4</em></strong></p>
              <g:form controller="federatedDevelopmentSessions" action="locallogin" method="post">
              <g:hiddenField name="principal" value="https://idp.one.edu.au/idp/shibboleth!-!d2404817-6fb9-4165-90d8-4" />
              <g:hiddenField name="credential" value="fake-sessionid-webform" />
              <g:hiddenField name="attributes.sharedToken" value="LGW3wpNaPgwnLoYYsghGbz4" />
              <g:hiddenField name="attributes.cn" value="Иван Петров" />
              <g:hiddenField name="attributes.email" value="i.petrov@one.edu.au" />
              <g:hiddenField name="attributes.entityID" value="https://idp.one.edu.au/idp/shibboleth" />

              <g:submitButton name="Login as Иван Петров" class="btn btn-danger btn-large"/>
            </g:form>
          </div>
      </div>

  </body>
</html>
