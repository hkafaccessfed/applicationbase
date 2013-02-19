environments {
  production {
    greenmail.disabled=true
  }
  test {
    testDataConfig.enabled = true
    
    grails.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
    grails.mail.default.from="noreply-test@aaf.edu.au"
    greenmail.disabled = false
  }
  development {
    greenmail.disabled=false
    grails.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
  }
}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"

grails.project.fork.run=true
