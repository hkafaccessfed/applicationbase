package aaf.base.identity

import grails.test.mixin.*
import spock.lang.*
import grails.plugin.spock.*

import org.apache.shiro.authc.*

import org.codehaus.groovy.grails.commons.ConfigurationHolder

import test.shared.ShiroEnvironment

@TestFor(aaf.base.identity.FederatedDevelopmentSessionsController)
class FederatedDevelopmentSessionsControllerSpec extends spock.lang.Specification {

  @Shared def shiroEnvironment = new ShiroEnvironment()
  
  def cleanupSpec() { 
    shiroEnvironment.tearDownShiro() 
  }

  def '403 when local is disabled for local login'() {
    setup:
    grailsApplication.config.aaf.base.realms.federated = [
      active: false,
      development: [ active: false ],
    ] 

    controller.metaClass.getGrailsApplication = { -> [config: ConfigurationHolder.config]}
    
    when:
    controller.locallogin()
    
    then:
    response.status == 403
    response.committed
  }
  
  
  def 'incomplete and redirect to federatedincomplete when principal not provided to locallogin'() {
    setup:
    grailsApplication.config.aaf.base.realms.federated = [
      active: false,
      development: [ active: true ],
      
      request: [ attributes: true],
      
      mapping: [
        principal: 'persistent-id',   
        credential: 'Shib-Session-ID'
      ]
    ] 
    controller.metaClass.getGrailsApplication = { -> [config: ConfigurationHolder.config]}
    
    when:
    controller.locallogin()
    
    then:
    response.redirectedUrl == "/auth/federatederror"
  }
  
  def 'incomplete and redirect to federatedincomplete when credential not provided to locallogin'() {
    setup:
    grailsApplication.config.aaf.base.realms.federated = [
      active: false,
      development: [ active: true ],
      
      request: [ attributes: true],
      
      mapping: [
        principal: 'persistent-id',
        credential: 'Shib-Session-ID'
      ]
    ] 

    controller.metaClass.getGrailsApplication = { -> [config: ConfigurationHolder.config] }
    params.principal = 'http://test.com!http://sp.test.com!1234'
    
    when:
    controller.locallogin()
    
    then:
    response.redirectedUrl == "/auth/federatederror"
  }
  
  def 'redirect to root URI when all is valid and no target supplied to locallogin'() {
    setup:
    grailsApplication.config.aaf.base.realms.federated = [
      active: false,
      development: [ active: true ],
      
      request: [ attributes: true],
      
      mapping: [
        principal: 'persistent-id',
        credential: 'Shib-Session-ID'
      ]
    ] 
    controller.metaClass.getGrailsApplication = { -> [config: ConfigurationHolder.config]}
    params.principal= 'http://test.com!http://sp.test.com!1234'
    params.credential= '1234-mockid-5678'
    request.addHeader("User-Agent", "Google Chrome X.Y")
    
    def token
    def subject = Mock(org.apache.shiro.subject.Subject)
    shiroEnvironment.setSubject(subject)
    
    when:
    controller.locallogin()
    
    then:
    1 * subject.login( { t -> token = t; t instanceof FederatedToken } )
    token.principal == 'http://test.com!http://sp.test.com!1234'
    token.credential == '1234-mockid-5678'
    token.userAgent == "Google Chrome X.Y"
    response.redirectedUrl == '/'
  }
  
  def 'redirect to target URI when all is valid and target supplied to locallogin'() {
    setup:
    grailsApplication.config.aaf.base.realms.federated = [
      active: false,
      development: [ active: true ],
      
      request: [ attributes: true],
      
      mapping: [
        principal: 'persistent-id',
        credential: 'Shib-Session-ID'
      ]
    ] 
    controller.metaClass.getGrailsApplication = { -> [config: ConfigurationHolder.config]}
    params.principal= 'http://test.com!http://sp.test.com!1234'
    params.credential= '1234-mockid-5678'
    request.addHeader("User-Agent", "Google Chrome X.Y")
    
    def token
    def subject = Mock(org.apache.shiro.subject.Subject)
    shiroEnvironment.setSubject(subject)
    
    session[AuthController.TARGET] = '/some/test/content'
    
    when:
    controller.locallogin()
    
    then:
    1 * subject.login( { t -> token = t; t instanceof FederatedToken } )
    token.principal == 'http://test.com!http://sp.test.com!1234'
    token.credential == '1234-mockid-5678'
    token.userAgent == "Google Chrome X.Y"
    response.redirectedUrl == '/some/test/content'
  }

  def 'correctly trim UA when longer then 254 char in local login'() {
    setup:
    grailsApplication.config.aaf.base.realms.federated = [
      active: false,
      development: [ active: true ],
      
      request: [ attributes: true],
      
      mapping: [
        principal: 'persistent-id', 
        credential: 'Shib-Session-ID',
        entityID: 'Shib-Identity-Provider',
        cn: 'cn',
        email: 'mail',
        sharedToken: 'auEduPersonSharedToken',
      ]
    ] 

    controller.metaClass.getGrailsApplication = { -> [config: ConfigurationHolder.config]}
    params.principal= 'http://test.com!http://sp.test.com!1234'
    params.credential= '1234-mockid-5678'
    request.addHeader("User-Agent", "Lorem ipsum dolor sit amet, nonummy ligula volutpat hac integer nonummy. Suspendisse ultricies, congue etiam tellus, erat libero, nulla eleifend, mauris pellentesque. Suspendisse integer praesent vel, integer gravida mauris, fringilla vehicula lacinia non")
    
    def token
    def subject = Mock(org.apache.shiro.subject.Subject)
    shiroEnvironment.setSubject(subject)
    
    when:
    controller.locallogin()
    
    then:
    1 * subject.login( { t -> token = t; t instanceof FederatedToken } )
    token.principal == 'http://test.com!http://sp.test.com!1234'
    token.credential == '1234-mockid-5678'
    token.userAgent == "Lorem ipsum dolor sit amet, nonummy ligula volutpat hac integer nonummy. Suspendisse ultricies, congue etiam tellus, erat libero, nulla eleifend, mauris pellentesque. Suspendisse integer praesent vel, integer gravida mauris, fringilla vehicula lacinia no"
    response.redirectedUrl == '/'
  }
  
  def 'redirect to federatederror when IncorrectCredentialsException thrown in locallogin'() {
    setup:
    grailsApplication.config.aaf.base.realms.federated = [
      active: false,
      development: [ active: true ],
      
      request: [ attributes: true],
      
      mapping: [
        principal: 'persistent-id',
        credential: 'Shib-Session-ID'
      ]
    ]
    controller.metaClass.getGrailsApplication = { -> [config: ConfigurationHolder.config]}
    params.principal= 'http://test.com!http://sp.test.com!1234'
    params.credential= '1234-mockid-5678'
    request.addHeader("User-Agent", "Google Chrome X.Y")
    
    def subject = Mock(org.apache.shiro.subject.Subject)
    shiroEnvironment.setSubject(subject)
    
    session['grails.controllers.aaf.base.identity.AuthController:TARGETURI'] = '/some/test/content'
    
    when:
    controller.locallogin()
    
    then:
    1 * subject.login( _ as FederatedToken ) >> { throw new IncorrectCredentialsException('test') }
    response.redirectedUrl == '/auth/federatederror'
  }
  
  def 'redirect to federatederror when DisabledAccountException thrown in locallogin'() {
    setup:
    grailsApplication.config.aaf.base.realms.federated = [
      active: false,
      development: [ active: true ],
      
      request: [ attributes: true],
      
      mapping: [
        principal: 'persistent-id',
        credential: 'Shib-Session-ID'
      ]
    ] 
    controller.metaClass.getGrailsApplication = { -> [config: ConfigurationHolder.config]}
    params.principal= 'http://test.com!http://sp.test.com!1234'
    params.credential= '1234-mockid-5678'
    request.addHeader("User-Agent", "Google Chrome X.Y")
    
    def subject = Mock(org.apache.shiro.subject.Subject)
    shiroEnvironment.setSubject(subject)
    
    when:
    controller.locallogin()
    
    then:
    1 * subject.login( _ as FederatedToken ) >> { throw new DisabledAccountException('test') }
    response.redirectedUrl == '/auth/federatederror'
  }
  
  def 'redirect to federatederror when AuthenticationException thrown in locallogin'() {
    setup:
    grailsApplication.config.aaf.base.realms.federated = [
      active: false,
      development: [ active: true ],
      
      request: [ attributes: true],
      
      mapping: [
        principal: 'persistent-id',
        credential: 'Shib-Session-ID'
      ]
    ] 
    controller.metaClass.getGrailsApplication = { -> [config: ConfigurationHolder.config]}
    params.principal= 'http://test.com!http://sp.test.com!1234'
    params.credential= '1234-mockid-5678'
    request.addHeader("User-Agent", "Google Chrome X.Y")
    
    def subject = Mock(org.apache.shiro.subject.Subject)
    shiroEnvironment.setSubject(subject)
    
    when:
    controller.locallogin()
    
    then:
    1 * subject.login( _ as FederatedToken ) >> { throw new AuthenticationException('test') }
    response.redirectedUrl == '/auth/federatederror'
  }
}
