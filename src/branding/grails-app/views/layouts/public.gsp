<!DOCTYPE html>
<html>
  <head>
    <title><g:message code='branding.application.name'/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <r:require modules="modernizr, bootstrap, bootstrap-responsive-css, bootstrap-notify, bootstrap-datepicker, bootbox, less, validate, datatables, formrestrict, app"/>
    <r:layoutResources/>
    <g:layoutHead />
  </head>

  <body>
    
    <header>      
      <g:render template='/templates/branding_header' />
    </header>

    <nav>
      <g:render template='/templates/branding_topnavigation'/>
    </nav>

    <section>
      <div class="container">
        <div class='notifications top-right'></div>
        <g:layoutBody/>
      </div>
    </section>
  
    <footer>
      <div class="container"> 
        <div class="row">
          <div class="span12">
            <g:render template='/templates/branding_footer' />
          </div>
        </div>
      </div>
    </footer>

    <r:layoutResources/>
  </body>

</html>
