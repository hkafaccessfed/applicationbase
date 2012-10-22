
<html>
  <head>
    <r:require modules="codemirror"/>
    <meta name="layout" content="internal" />
  </head>
  <body>
    <ul class="breadcrumb">
      <li><g:link controller="dashboard"><g:message code="branding.application.name"/></g:link> <span class="divider">/</span></li>
      <li><g:link controller="adminDashboard"><g:message code="branding.nav.breadcrumb.admin"/></g:link> <span class="divider">/</span></li>
      <li><g:link controller="workflowProcess" action="list"><g:message code="branding.nav.breadcrumb.workflow.process"/></g:link> <span class="divider">/</span></li>
      <li><g:link controller="workflowProcess" action="show" id="${process.id}">${fieldValue(bean: process, field: "name")}</g:link> <span class="divider">/</span></li>
      <li class="active"><g:message code="branding.nav.breadcrumb.workflow.process.edit"/></li>
    </ul>

    <g:render template="/templates/flash" />
    <g:render template="/templates/errors_bean" model="['bean':process]"/>

    <h2><g:message code="views.aaf.base.workflow.process.edit.heading" args="[process.name]"/></h2>
  
    <g:form action="update">
      <g:hiddenField name="id" value="${process.id}" />
      <g:textArea name="code" value="${process.definition}" rows="5" cols="40"/>
      <div class="form-actions">
        <button type="submit" class="btn btn-success"/><g:message code="label.update" default="Update"/></button>
        <g:link class="btn" controller="workflowProcess" action="show" id="${process.id}"><g:message code="label.cancel" default="Cancel"/></g:link>
      </div>
    </g:form>
  
    <r:script>
      var editor = CodeMirror.fromTextArea('code',  {
        height: "600px",
        path: "",
        stylesheet: "${r.resource(dir:'/js/codemirror', file:'groovycolors.css', plugin:'aafApplicationBase') }",
        basefiles: ["${r.resource(dir:'/js/codemirror', file:'codemirror.groovy.inframe.min.js', plugin:'aafApplicationBase') }"],
        parserfile: [],
        autoMatchParens: true,
        disableSpellcheck: true,
        lineNumbers: true,
        tabMode: 'shift'
      });
    </r:script>

  </body>
</html>
