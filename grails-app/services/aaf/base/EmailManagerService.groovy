package aaf.base

import aaf.base.admin.EmailTemplate

class EmailManagerService {
  def mailService
  def groovyPagesTemplateEngine
  def groovyPageRenderer

  public void send(String _to, String _subject, EmailTemplate view, Map model, String _cc = null, String _bcc = null) {

    def output = new StringWriter()
    groovyPagesTemplateEngine.createTemplate(view.content, view.name).make(model).writeTo(output)
    def contents = output.toString()

    def email_msg = groovyPageRenderer.render(view: '/layouts/email')
    email_msg = email_msg.replace("###HTML EMAIL CONTENT###", contents)
   
    mailService.sendMail {
      to _to  

      if(_cc)
        cc _cc
        
      if(_bcc)
        bcc _bcc

      subject _subject
      html email_msg
    }
  }

}
