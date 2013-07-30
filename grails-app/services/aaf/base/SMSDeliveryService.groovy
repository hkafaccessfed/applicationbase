package aaf.base

import groovyx.net.http.*
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

class SMSDeliveryService {
  def grailsApplication

  def send(String to, String text) {
    def config = grailsApplication.config.aaf.base.sms

    boolean outcome = true
    http.request( GET, JSON ) {
      uri.path = '/sms/json'
      uri.query = [api_key: config.api_key, api_secret:config.api_secret, from:'AAF', to:to, type:'text', text:text]

      response.success = { resp, json ->
        log.debug resp.statusLine
        log.info "Sent SMS to $to with successful response of ${json.messages}"
        outcome = true

      }

      response.failure = { resp ->
        log.error "Sent SMS to $to with failure response of ${resp.statusLine.statusCode} : ${resp.statusLine.reasonPhrase}"
      }
    }

    outcome
  }

  def getHttp() { new HTTPBuilder(grailsApplication.config.aaf.base.sms.api_endpoint) }
}
