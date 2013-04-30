class AAFBaseUrlMappings {
  static mappings = {    
    // Session Management
    "/session/federated/$action?/$id?"{
      controller = "federatedSessions"
    }

    "/session/development/$action?/$id?"{
      controller = "federatedDevelopmentSessions"
    }

    "/session/$action?/$id?"{
      controller = "auth"
    }

    "/logout"{
      controller = "auth"
      action = "logout"
    }

    // Workflow
    "/workflow/approval/$action?/$id?" {
      controller="workflowApproval"
    }

    // Role invitation
    "/inviteadministrator/finalization/$inviteCode"{
      controller = "role" 
      action = "finalization"
    }

    "/inviteadministrator/error"{
      controller = "role" 
      action = "finalizationerror"
    }

    // Administration
    "/administration/dashboard"{
      controller="adminDashboard"
      action="index"
    }

    "/administration/environment"{
      controller="adminDashboard"
      action="environment"
    }
    
    "/administration/subjects/$action?/$id?"{
      controller = "subject" 
    }

    "/administration/roles/$action?/$id?"{
      controller = "role" 
    }

    "/administration/workflow/processes/$action?/$id?" {
      controller="workflowProcess"
    }

    "/administration/workflow/scripts/$action?/$id?" {
      controller="workflowScript"
    }

    "/administration/emailtemplates/$action?/$id?"{
      controller = "emailTemplate"
    }

    // Console plugin
    "/console"{
      controller = "console"
      action = "index"
    }

    "/console/$action"{
      controller = "console"
    }

    // Errors
    "403"(view:'/403')
    "404"(view:'/404')
    "405"(view:'/405')
    "500"(view:'/500')

    // Greenmail (Development mode only)
    "/greenmail/$action?/$id?"{
      controller = "greenmail"
    }
  }
}
