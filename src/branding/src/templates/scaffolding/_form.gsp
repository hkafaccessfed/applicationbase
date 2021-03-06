<% import grails.persistence.Event %>

<fieldset>
<%  excludedProps = Event.allEvents.toList() << 'version' << 'dateCreated' << 'lastUpdated'
  persistentPropNames = domainClass.persistentProperties*.name
  boolean hasHibernate = pluginManager?.hasGrailsPlugin('hibernate')
  if (hasHibernate && org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsDomainBinder.getMapping(domainClass)?.identity?.generator == 'assigned') {
    persistentPropNames << domainClass.identifier.name
  }
  props = domainClass.properties.findAll { persistentPropNames.contains(it.name) && !excludedProps.contains(it.name) }
  Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
  for (p in props) {
    if (p.embedded) {
      def embeddedPropNames = p.component.persistentProperties*.name
      def embeddedProps = p.component.properties.findAll { embeddedPropNames.contains(it.name) && !excludedProps.contains(it.name) }
      Collections.sort(embeddedProps, comparator.constructors[0].newInstance([p.component] as Object[]))
      %><fieldset class="embedded"><legend><g:message encodeAs='HTML' code="label.${p.name}"/></legend><%
        for (ep in p.component.properties) {
          renderFieldForProperty(ep, p.component, "${p.name}.")
        }
      %></fieldset><%
    } else {
      renderFieldForProperty(p, domainClass)
    }
  }

private renderFieldForProperty(p, owningClass, prefix = "") {
  boolean hasHibernate = pluginManager?.hasGrailsPlugin('hibernate')
  boolean display = true
  boolean required = false
  if (hasHibernate) {
    cp = owningClass.constrainedProperties[p.name]
    display = (cp ? cp.display : true)
    required = (cp ? !(cp.propertyType in [boolean, Boolean]) && !cp.nullable && (cp.propertyType != String || !cp.blank) : false)
  }
  if (display) { %>
  <div class="control-group \${hasErrors(bean: ${propertyName}, field: '${prefix}${p.name}', 'error')}">
    <label class="control-label" for="${prefix}${p.name}"><g:message encodeAs='HTML' code="label.${p.name.toLowerCase()}"/></label>
    <div class="controls">
      ${renderEditor(p)}
      <a href="#" rel="tooltip" title="\${g.message(encodeAs:'HTML', code:'help.inline.${domainClass.packageName.toLowerCase()}.${domainClass.name.toLowerCase()}.${p.name.toLowerCase()}')}"><i class="icon icon-question-sign"></i></a>
    </div>
  </div>
<%  }   } %>
</fieldset>
