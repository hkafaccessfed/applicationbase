environments {
  production {
    greenmail.disabled=true
  }
  test {
    grails.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
  }
  development {
    grails.mail.port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
  }
}
grails.views.default.codec="none" // none, html, base64
grails.views.gsp.encoding="UTF-8"
