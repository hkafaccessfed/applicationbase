class AAFBaseSecurityFilters {

  def filters = {
    workflow_authenticated(uri:"/workflow/**") {
      before = {
        accessControl { true }
      }
    }

    administration_authenticated(uri:"/administration/**") {
      before = {
        accessControl { true }
      }
    }

    administration(uri: "/administration/**") {
      before = {
        if(!accessControl { permission("app:administration") }) {
          log.info("secfilter: DENIED - [${subject.id}]${subject.principal}|${request.remoteAddr}|$params.controller/$params.action")
          response.sendError(403)
          return false
        }
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }

    console(controller:"console") {
      before = {
        if (!accessControl { permission("app:administration") }) {
          log.info("secfilter: DENIED - [${subject.id}]${subject.principal}|${request.remoteAddr}|$params.controller/$params.action")
          response.sendError(404) // Deliberately not 403 so endpoint can't be figured out.
          return false
        }
        log.info("secfilter: ALLOWED - [$subject.id]$subject.principal|${request.remoteAddr}|$params.controller/$params.action")
      }
    }
  }
  
}
