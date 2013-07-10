package aaf.base

import grails.converters.*
import org.apache.shiro.SecurityUtils

class ErrorController {

  def notFound() {
    if(request.forwardURI.startsWith("/api")) {
      log.error "API unknown endpoint when processing request from ${request.remoteHost} directed to ${request.forwardURI}"

      response.status = 404
      render buildJSONErrorResponse('invalid endpoint', 'invalid endpoint mapping for api - see documentation') as JSON
      return
    }

    render view: "/404"
  }

  def internalServerError() {
    if(request.forwardURI.startsWith("/api")) {
      log.error "API internal server error when processing request from ${request.remoteHost} directed to ${request.forwardURI}"

      response.status = 500
      render buildJSONErrorResponse('unexpected internal error', 'unexpected internal error for api - see documentation') as JSON
      return
    }

    render view: "/500"
  }

  private Map buildJSONErrorResponse(String error, String internalError, Map m = [:]) {
    return [error:error, internalerror:internalError] + m + [timestamp:new Date()]
  }

}
