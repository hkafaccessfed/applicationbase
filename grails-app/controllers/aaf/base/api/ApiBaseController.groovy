package aaf.base.api

import aaf.base.identity.ApiToken

import org.apache.shiro.SecurityUtils
import org.apache.shiro.authc.AuthenticationException
import org.apache.shiro.authc.DisabledAccountException
import org.apache.shiro.authc.IncorrectCredentialsException
import org.apache.shiro.authc.UnknownAccountException

class ApiBaseController {

  private boolean validateRequest() {
    def incomplete = false
    def errors = []

    try {
      def token = new ApiToken(request:request) 
      SecurityUtils.subject.login(token)
    }
    catch (IncorrectCredentialsException e) {
      response.status = 403
      render(contentType: 'text/json') { ['error':'Signature validation failed. Verify account, private secret and computation method used to access this secured API is valid.', 'internalerror':e.message] }
      return false
    }
    catch (UnknownAccountException e) {
      response.status = 403
      render(contentType: 'text/json') { ['error':'Verify account used to access this secured API is known', 'internalerror':e.message] }
      return false
    }
    catch (DisabledAccountException e) {
      response.status = 403
      render(contentType: 'text/json') { ['error':'Verify account used to access this secured API is valid', 'internalerror':e.message] }
      return false
    }
    catch (AuthenticationException e) {
      response.status = 500
      render(contentType: 'text/json') { ['error':'General fault when validating request. Verify correct data is supplied for authentication to this API.', 'internalerror':e.message] }
      return false
    }

    true
  }

  private Map buildJSONResponse(Map m) {
    return m + [timestamp:new Date(), issuer:grailsApplication.config.aaf.base.service_identifier]
  }
}
