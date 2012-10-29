package aaf.base

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

class EmailView {

  String name
  String content

  Date dateCreated
  Date lastUpdated

  static constraints = {
    name nullable: false, blank: false, unique:true
    content type: 'text', validator: {val ->
      if (val == null || val == '')
        return ['domains.aaf.base.emailview.content']
    }
  }

}
