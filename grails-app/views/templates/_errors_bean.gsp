<g:hasErrors bean="${bean}">
  <div class="alert alert-error">
    <strong><g:message code="label.oopserrors"/></strong>
    <ol>
      <g:eachError bean="${bean}" var="error">
        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
      </g:eachError>
    </ol>
  </div>
</g:hasErrors>
