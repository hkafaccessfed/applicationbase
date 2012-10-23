# README - AAF Application Base

- (c) Australian Access Federation
- Author: Bradley Beddoes, Australian Access Federation

## Overview
The AAF Application Base is an extensible Grails plugin that provides a number 
of key features that are used across all AAF projects. With the use of this
plugin it should be possible to simply start working on new business logic in
a short time frame having all the additional plumbing for a production ready
service simply provided for you.

## Key Features
- Grails 2.1.1+ Support
- JQuery and various JQuery libraries for expressive UI
- Twitter Bootstrap for styling and UI layout
- Report generation via HighCharts
- Refinable data via dataTables
- Responsive AAF branded layouts suitable for Phones, Tables and Desktops
- Cross reader HTML email
- Administrative dashboard
- Subject management
- Role management
- Workflow Engine including data store driven Processes and Scripts
- Internal Grails console
- Default application welcome page and dashboard
- Support for Federated authentication via Shibboleth SP
- Eases development with local accounts
- Full i18n implmentation
- Customizable branding per project including full navigation modification
- AAF specific assistance tools including help directives and custom error pages
- Externalizes configuration and logging ready for production deployment
- Supports Git Flow workflow and provides base .gitignore

## Pre-requisites
If you're new to this space you'll want to get a few things up and running before you can start development.

1. A *nix based development machine - I work directly on my Mac but a [Virtualbox VM](http://www.virtualbox.org) works just as well

2. An install of Java JDK. I am currently using a 7 release:
	
		java -version
		java version "1.7.0_07"
		Java(TM) SE Runtime Environment (build 1.7.0_07-b10)
		Java HotSpot(TM) 64-Bit Server VM (build 23.3-b01, mixed mode)
You can find [offical Oracle JDK downloads here](http://www.oracle.com/technetwork/java/javase/downloads/index.html) or alternatively use an OpenJDK which works just as well.

3. Ensure you have a JAVA_HOME and appropriate JAVA_OPTS environment variables defined in your `~/.bash_profile`, here is an example of mine:

		export JAVA_HOME='/Library/Java/JavaVirtualMachines/jdk1.7.0_07.jdk/Contents/Home'
		export JAVA_OPTS='-Xms1024m -Xmx1024m -XX:MaxPermSize=256m'
For Grails work the above memory allocations seem to give me the best performance.

4. [Install GVM](http://gvmtool.net/) using the below commands. You will require **bash, curl, zip and unzip** to be available on your system
	
		curl -s get.gvmtool.net | bash
		source "~/.gvm/bin/gvm-init.sh"
		gvm install grails 2.1.1
		gvm use grails 2.1.1
Make sure Grails is correctly installed and referenced:

		$> which grails
		~/.gvm/grails/current/bin/grails

## Getting things up and running
This couldn't be simpler on the surface. Though getting things like workflows
up and running will require a little more work and familiarisiation of the
underlying codebase.

**The AAF applicationbase plugin is distributed in source form only and only available from private AAF git repositories. Any changes required to the applicationbase plugin should be done in the upstream project**

1. Create a containing directory called something like 'development'
2. Run `git clone git@github.com:ausaccessfed/applicationbase.git`
3. This will give you a local directory called **applicationbase**
4. Still in your **development** directory create a new Grails application 
using at least Grails release 2.1.1 we'll refer to this as appname
5. Your directory should now contain **applicationbase** and **appname**
6. Change to your application and edit the file:
grails-app/conf/BuildConfig.groovy and add the following (~line 8):

    grails.plugin.location.'aaf-application-base' = '../applicationbase'

7. At the root of your project directory structure run the command:

    `grails clean-new-app`

8. Follow this with the command:

    `grails create-branding`

9. This has given you a number of new files througout your Grails App which
you can explore. To start with lets get something basic up and running.

10. Edit the new file application_config.groovy in the root of your project
Setting the following:

    appName="lowercasenameofapp"  e.g. "federationregistry"
    grails.serverURL="http://brainslave.dev.bradleybeddoes.com:8080/lowercasenameofapp"
    *This is your your dev machine, include appName, no trailing /*

    initial_administrator_auto_populate = true     
    *This will make dev easier, ensure FALSE in production*

    development {
      active = true
    }
    *This will make dev easier, ensure FALSE in production*

11. Lets test everything. Export the environment variable config_dir:

    `export config_dir=.`

12. Run your application:

    `grails run-app`

13. Navigate to your application as indicated by Grails output
14. You should have a public welcome page, be able to login and then access
all the default functionality as an administator. Try logging out and then
in again as someone else, all of the admin functionality should be gone.

## Customisation Points
The commands you ran earlier (clean-new-app and create-branding) have taken your vanilla Grails app and both removed css/js/images that we don't use and added css/js/images that we do.

In particular the following files will help you customise your new application to suit your needs.

1. **grails-app/i18n/messages-branding.properties** - This contains all the strings shown throughout the application. You should change them to be relevant to application you're building.
2. **grails-app/views/layout/*.gsp** - This controls how the application itself looks. In most cases you won't need to modify these but they are there for special circumstances
3. **grails-app/controllers/DashboardController.groovy and grails-app/views/dashboard/*.gsp** - Your public and authenticated dashboard views. You will want to customise the controller and the view files to suit your application. I **strongly** recommend moving the controller into a package structure such as aaf.yourappname - all your subsquent controllers should be packaged as well.
4. **grails-app/views/templates/branding/*.gsp** - These templates give the application its header, footer and most importantly **navigation** views. All applications implementing the Application Base will want to extend navigation markup (along with UrlMappings.groovy) to provide access to the logic they serve.

## Access Control
Within the Application Base a **Subject** is the key building block. Subjects are created (usually automatically) when a new users enters the system via supported authentication realm (federation for production, local accounts for development. See FederatedRealm.groovy)

Subjects can then be granted membership to many **Roles**. Both a Subject and Role can be granted **Permissions**. A permission directly enforces security on requests, actions, views, markup and internal data it is the lowest level of our security structure. A permission takes the form x:y:z to an unlimited number of levels. Optionally a Subject or Role might be granted a permission with a wildcard x:y:* meaning a state of true will be returned for any permission check under x:y. e.g A permissions check for `x:y:delete` would return true for this Subject or Role.

To enforce access control code should always check for valid permissions. It can achieve this several ways. Within a groovy code block:

	import org.apache.shiro.SecurityUtils
	…
	if(SecurityUtils.subject.isPermitted("x:y:z")) {
	}
	else {
	  log.warn("Attempt to do action by $subject was denied")
      response.sendError(403)
	}
	
Within a GSP:

	<aaf:isAdministrator>
	</aaf:isAdministrator>
	
	<aaf:hasPermission target="x:y:z">
	</aaf:hasPermission>
	
**There are a lot more GSP tag options defined in AAFBaseTagLib.groovy within the Application Base project, check them out.**

Roles and Permissions can be created by administrators using the provided UI. You may also choose to implement your own subset of this UI in which case *RoleService* and *PermissionsService* will be useful for you to use when interacting with this subsystem.

## Workflow
A full workflow engine is provided but as yet not well documented. We need to update this section over time as understanding needs to be shared through the team. In the meantime have a look at the extensive test cases to understand how this piece works.

## Suggestions
As you use the Application Base you'll probably find gaps in documentation and test cases that explain how things function. Please bring these up so we can continue to refine over time.
