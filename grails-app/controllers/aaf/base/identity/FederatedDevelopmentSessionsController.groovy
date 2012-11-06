package aaf.base.identity

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.DisabledAccountException
import org.apache.shiro.authc.IncorrectCredentialsException

class FederatedDevelopmentSessionsController {
  def grailsApplication
  
  def locallogin = {
    def incomplete = false
    def errors = []

    if (!grailsApplication.config.aaf.base.realms.federated.development.active) {
      log.error "Authentication diverted to local development accounts but this mode is not enabled in configuration."
      response.sendError(403)
      return
    }

    def ua = request.getHeader("User-Agent")
    if (!ua) {
      incomplete = true
      errors.add "Browser User Agent was not presented"
    }
    
    // Instead of SAML attributes we setup the session via form posted name/val
    def principal = params.principal
    def credential = params.credential  
    def sharedToken = params.sharedToken
    def attributes = params.attributes
    
    if (!principal) {
      incomplete = true
      errors.add "Unique subject identifier (principal) was not presented"
    }

    if (!credential) {
      incomplete = true
      errors.add "Internal SAML session identifier (credential) was not presented"
    }

    if (!sharedToken) {
      incomplete = true
      errors.add "Internal SAML session identifier (sharedToken) was not presented"
    }
    
    if(incomplete) {
      log.info "Incomplete federated authentication attempt was aborted"
      errors.each { log.warn it }
      redirect controller:'auth', action: 'federatederror'
      return
    }
    
    try {
      def remoteHost = request.getRemoteHost()
      if (ua.length() > 254) // Handle stupid user agents that present every detail known to man about corporate environments
        ua = ua.substring(0,254) 
      
      def token = new FederatedToken(principal:principal, credential:credential, sharedToken:sharedToken, attributes:attributes, remoteHost:remoteHost, userAgent:ua ) 
      
      log.info "Attempting development authentication event for subject identified in $token"
      SecurityUtils.subject.login(token)
      log.info "Successfully processed local development/testing authentication event for subject $principal based on credential provided in $credential, redirecting to content"
      
      def targetUri = session.getAttribute(AuthController.TARGET)
      session.removeAttribute(AuthController.TARGET)
      targetUri ? redirect(uri: targetUri) : redirect(uri:"/")
      return
    }
    catch (IncorrectCredentialsException e) {
      log.warn "Local credentials failure for subject $principal, incorrect credentials."
      log.debug e
    }
    catch (DisabledAccountException e) {
      log.warn "Local credentials failure for subject $principal, account disabled locally"
      log.debug e
    }
    catch (AuthenticationException e) {
      log.warn "Local credentials failure for subject $principal, generic fault"
      log.debug e
    }
    
    redirect controller:'auth', action: 'federatederror'
  }

}
