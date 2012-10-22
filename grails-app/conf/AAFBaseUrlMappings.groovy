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

    // Workflow
    "/workflow/approval/$action?/$id?" {
      controller="workflowApproval"
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

    "/administration/console"{
      controller = "adminConsole"
      action="index"
    }

    // Console plugin
    "/internal/console/$action?/$id?"{
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
