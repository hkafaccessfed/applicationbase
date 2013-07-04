import org.apache.log4j.DailyRollingFileAppender

environments {
  production {
    greenmail.disabled=true
  }
  test {
    testDataConfig.enabled = true

    grails.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
    grails.mail.default.from="noreply-test@aaf.edu.au"
    greenmail.disabled = false

    log4j = {
      appenders {
        appender new DailyRollingFileAppender(name:"aafbase-security", layout:pattern(conversionPattern: "%d{[ dd.MM.yy HH:mm:ss.SSS]} %-5p %c %x - %m%n"), file:"/tmp/aafbase-security.log", datePattern:"'.'yyyy-MM-dd")
        appender new DailyRollingFileAppender(name:"aafbase", layout:pattern(conversionPattern: "%d{[ dd.MM.yy HH:mm:ss.SSS]} %-5p %c %x - %m%n"), file:"/tmp/aafbase.log", datePattern:"'.'yyyy-MM-dd")
        appender new DailyRollingFileAppender(name:"aafbase-grails", layout:pattern(conversionPattern: "%d{[ dd.MM.yy HH:mm:ss.SSS]} %-5p %c %x - %m%n"), file:"/tmp/aafbase-grails.log", datePattern:"'.'yyyy-MM-dd")
        appender new DailyRollingFileAppender(name:"stacktrace", layout:pattern(conversionPattern: "%d{[ dd.MM.yy HH:mm:ss.SSS]} %-5p %c %x - %m%n"), file:"/tmp/aafbase-stacktrace.log", datePattern:"'.'yyyy-MM-dd")
      }

      info  'aafbase-security'    :['grails.app.filters'], additivity: false

      debug  'aafbase'            :['grails.app.controllers',
                                  'grails.app.domains',
                                  'grails.app.services',
                                  'grails.app.realms',
                                  'aaf.base',
                                  'org.apache.shiro',
                                  'org.spockframework'], additivity: false

      warn  'aafbase-grails'      :['org.codehaus.groovy.grails.web.servlet',
                                  'org.codehaus.groovy.grails.web.pages',
                                  'org.codehaus.groovy.grails.web.sitemesh',
                                  'org.codehaus.groovy.grails.web.mapping.filter',
                                  'org.codehaus.groovy.grails.web.mapping',
                                  'org.codehaus.groovy.grails.commons',
                                  'org.codehaus.groovy.grails.plugins'], additivity: false
    }
  }
  development {
    greenmail.disabled=false
    grails.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
  }
}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"

grails.project.fork.run=true
