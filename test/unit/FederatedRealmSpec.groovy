import grails.test.mixin.*
import spock.lang.*
import grails.plugin.spock.*

import org.apache.shiro.authc.*
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import test.shared.ShiroEnvironment

import aaf.base.identity.*

@Mock([aaf.base.identity.Subject])
class FederatedRealmSpec extends UnitSpec {

  @Shared def shiroEnvironment = new ShiroEnvironment()
  
  def cleanupSpec() { 
    shiroEnvironment.tearDownShiro() 
  }
  
  def 'UnknownAccountException when federated authentication is not active'() {
    setup:
    mockLogging(FederatedRealm, true)
    def realm = new FederatedRealm()
    
    mockConfig '''
    aaf{
      base{
        realms{
          federated{
            active = false
          }
        }
      }
    }
    ''' 
    realm.grailsApplication = [config: ConfigurationHolder.config]
    
    def token = new FederatedToken()
    
    when:
    realm.authenticate(token)
    
    then:
    UnknownAccountException e = thrown()
    e.message == 'Authentication attempt for federated provider, denying attempt as federation disabled'
  }
  
  def 'UnknownAccountException when principal is not supplied'() {
    setup:
    mockLogging(FederatedRealm, true)
    def realm = new FederatedRealm()
    
    mockConfig '''
    aaf{
      base{
        realms{
          federated{
            active = true
          }
        }
      }
    }
    ''' 
    realm.grailsApplication = [config: ConfigurationHolder.config]
    
    def token = new FederatedToken()
    
    when:
    realm.authenticate(token)
    
    then:
    UnknownAccountException e = thrown()
    e.message == 'Authentication attempt for federated provider, denying attempt as no persistent identifier was provided'
  }
  
  def 'UnknownAccountException when credential is not supplied'() {
    setup:
    mockLogging(FederatedRealm, true)
    def realm = new FederatedRealm()
    
    mockConfig '''
    aaf{
      base{
        realms{
          federated{
            active = true
          }
        }
      }
    }
    ''' 
    realm.grailsApplication = [config: ConfigurationHolder.config]
    
    def token = new FederatedToken(principal:'http://test.com!http://sp.test.com!1234')
    
    when:
    realm.authenticate(token)
    
    then:
    UnknownAccountException e = thrown()
    e.message == 'Authentication attempt for federated provider, denying attempt as no credential was provided'
  }
  
  def 'DisabledAccountException when user doesnt already exist and auto_provision disabled'() {
    setup:
    mockDomain(SessionRecord)
    mockLogging(FederatedRealm, true)
    def realm = new FederatedRealm()
    
    mockConfig '''
    aaf{
      base{
        realms{
          federated{
            active = true
            auto_provision = false
          }
        }
      }
    }
    ''' 
    realm.grailsApplication = [config: ConfigurationHolder.config]
    
    def token = new FederatedToken(principal:'http://test.com!http://sp.test.com!1234', credential:'1234-mockid-5678')
    Subject.metaClass.'static'.withTransaction = { Closure c -> c() }

    when:
    realm.authenticate(token)
    
    then:
    DisabledAccountException e = thrown()
    e.message == 'Authentication attempt for federated provider, denying attempt as federation integration is denying automated account provisioning'
  }
  
  def 'Account created when auto_provision is active'() {
    setup:
    mockDomain(SessionRecord)
    mockLogging(FederatedRealm, true)
    def realm = new FederatedRealm()
    
    mockConfig '''
    aaf{
      base{
        realms{
          federated{
            active = true
            auto_provision = true
            app.subject = 'aaf.base.identity.Subject'
          }
        }
      }
    }
    ''' 
    realm.grailsApplication = [config: ConfigurationHolder.config]
    
    def token = new FederatedToken(principal:'http://test.com!http://sp.test.com!1234', credential:'1234-mockid-5678', 
      attributes:[cn:'Fred Bloggs', email:'fred@uni.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsghGbz1'], userAgent:'Google Chrome X.Y', remoteHost:'192.168.1.1')
    Subject.metaClass.'static'.withTransaction = { Closure c -> c() }

    when:
    def subjects = aaf.base.identity.Subject.count()
    def account = realm.authenticate(token)
    
    then:
    subjects == 0
    aaf.base.identity.Subject.count() == 1  // Note we can't do a count on SessionRecord as cascade save doesn't work in unit tests
    account instanceof SimpleAccount
    account.credentials == '1234-mockid-5678'
    def subject = aaf.base.identity.Subject.get(account.principals.primaryPrincipal)
    subject.principal == 'http://test.com!http://sp.test.com!1234'
    subject.enabled
    def sessionRecord = subject.sessionRecords.toList().get(0)
    sessionRecord.credential == '1234-mockid-5678'
    sessionRecord.userAgent == 'Google Chrome X.Y'
  }

  def 'Account created when auto_provision is active and multiple email address supplied'() {
    setup:
    mockDomain(SessionRecord)
    mockLogging(FederatedRealm, true)
    def realm = new FederatedRealm()
    
    mockConfig '''
    aaf{
      base{
        realms{
          federated{
            active = true
            auto_provision = true
            app.subject = 'aaf.base.identity.Subject'
          }
        }
      }
    }
    ''' 
    realm.grailsApplication = [config: ConfigurationHolder.config]
    
    def token = new FederatedToken(principal:'http://test.com!http://sp.test.com!1234', credential:'1234-mockid-5678', 
      attributes:[cn:'Fred Bloggs', email:'fred@uni.edu.au;12345-staff@internal.uni.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsghGbz1'], userAgent:'Google Chrome X.Y', remoteHost:'192.168.1.1')
    Subject.metaClass.'static'.withTransaction = { Closure c -> c() }

    when:
    def subjects = aaf.base.identity.Subject.count()
    def account = realm.authenticate(token)
    
    then:
    subjects == 0
    aaf.base.identity.Subject.count() == 1  // Note we can't do a count on SessionRecord as cascade save doesn't work in unit tests
    account instanceof SimpleAccount
    account.credentials == '1234-mockid-5678'
    def subject = aaf.base.identity.Subject.get(account.principals.primaryPrincipal)
    subject.principal == 'http://test.com!http://sp.test.com!1234'
    subject.enabled
    def sessionRecord = subject.sessionRecords.toList().get(0)
    sessionRecord.credential == '1234-mockid-5678'
    sessionRecord.userAgent == 'Google Chrome X.Y'
  }

  def 'Update of existing account data succeeds'() {
    setup:
    mockDomain(SessionRecord)
    mockLogging(FederatedRealm, true)
    def realm = new FederatedRealm()
    
    mockConfig '''
    aaf{
      base{
        realms{
          federated{
            active = true
            auto_provision = true
            app.subject = 'aaf.base.identity.Subject'
          }
        }
      }
    }
    ''' 
    realm.grailsApplication = [config: ConfigurationHolder.config]
    
    def token = new FederatedToken(principal:'http://test.com!http://sp.test.com!1234', credential:'1234-mockid-5678', 
      attributes:[cn:'Fred Blogs', email:'fred-blogs@uni.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsghGbz1'], userAgent:'Google Chrome X.Y')
    Subject.metaClass.'static'.withTransaction = { Closure c -> c() }
    
    def subject = new aaf.base.identity.Subject(principal:'http://test.com!http://sp.test.com!1234', cn:'Fred Bloggs', email:'fred@uni.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsghGbz1', active:false).save()

    when:
    realm.authenticate(token)

    then:
    aaf.base.identity.Subject.count() == 1
    subject.refresh()
    subject.cn.equals 'Fred Blogs'
    subject.email.equals 'fred-blogs@uni.edu.au'

    DisabledAccountException e = thrown()
    e.message == "Attempt to authenticate using using federated principal mapped to a locally disabled account [${subject.id}]${subject.principal}"
  }

  def 'Update of existing account data with multival email succeeds'() {
    setup:
    mockDomain(SessionRecord)
    mockLogging(FederatedRealm, true)
    def realm = new FederatedRealm()
    
    mockConfig '''
    aaf{
      base{
        realms{
          federated{
            active = true
            auto_provision = true
            app.subject = 'aaf.base.identity.Subject'
          }
        }
      }
    }
    ''' 
    realm.grailsApplication = [config: ConfigurationHolder.config]
    
    def token = new FederatedToken(principal:'http://test.com!http://sp.test.com!1234', credential:'1234-mockid-5678', 
      attributes:[cn:'Fred Blogs', email:'fred-blogs@uni.edu.au; 12345-staff@internal.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsghGbz1'], userAgent:'Google Chrome X.Y')
    Subject.metaClass.'static'.withTransaction = { Closure c -> c() }
    
    def subject = new aaf.base.identity.Subject(principal:'http://test.com!http://sp.test.com!1234', cn:'Fred Bloggs', email:'fred@uni.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsghGbz1', active:false).save()

    when:
    realm.authenticate(token)

    then:
    aaf.base.identity.Subject.count() == 1
    subject.refresh()
    subject.cn.equals 'Fred Blogs'
    subject.email.equals 'fred-blogs@uni.edu.au'

    DisabledAccountException e = thrown()
    e.message == "Attempt to authenticate using using federated principal mapped to a locally disabled account [${subject.id}]${subject.principal}"
  }

  def 'Modification of sharedtoken on existing account throws IncorrectCredentialsException'() {
    setup:
    mockDomain(SessionRecord)
    mockLogging(FederatedRealm, true)
    def realm = new FederatedRealm()
    
    mockConfig '''
    aaf{
      base{
        realms{
          federated{
            active = true
            auto_provision = true
            app.subject = 'aaf.base.identity.Subject'
          }
        }
      }
    }
    ''' 
    realm.grailsApplication = [config: ConfigurationHolder.config]
    
    def token = new FederatedToken(principal:'http://test.com!http://sp.test.com!1234', credential:'1234-mockid-5678', 
      attributes:[cn:'Fred Bloggs', email:'fred@uni.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsghGbz1'], userAgent:'Google Chrome X.Y')
    Subject.metaClass.'static'.withTransaction = { Closure c -> c() }
    
    def subject = new aaf.base.identity.Subject(principal:'http://test.com!http://sp.test.com!1234', cn:'Fred Bloggs', email:'fred@uni.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsgh', active:false).save()

    when:
    realm.authenticate(token)

    then:
    aaf.base.identity.Subject.count() == 1

    IncorrectCredentialsException e = thrown()
    e.message == "${subject} authentication halted as current sharedToken ${subject.sharedToken} does not match incoming token ${token.attributes.sharedToken}"
  }
  
  def 'Existing, disabled account throws DisabledAccountException'() {
    setup:
    mockDomain(SessionRecord)
    mockLogging(FederatedRealm, true)
    def realm = new FederatedRealm()
    
    mockConfig '''
    aaf{
      base{
        realms{
          federated{
            active = true
            auto_provision = true
            app.subject = 'aaf.base.identity.Subject'
          }
        }
      }
    }
    ''' 
    realm.grailsApplication = [config: ConfigurationHolder.config]
    
    def token = new FederatedToken(principal:'http://test.com!http://sp.test.com!1234', credential:'1234-mockid-5678', 
      attributes:[cn:'Fred Bloggs', email:'fred@uni.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsghGbz1'], userAgent:'Google Chrome X.Y')
    Subject.metaClass.'static'.withTransaction = { Closure c -> c() }
    
    def subject = new aaf.base.identity.Subject(principal:'http://test.com!http://sp.test.com!1234', cn:'Fred Bloggs', email:'fred@uni.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsghGbz1', active:false).save()

    when:
    realm.authenticate(token)

    then:
    aaf.base.identity.Subject.count() == 1

    DisabledAccountException e = thrown()
    e.message == "Attempt to authenticate using using federated principal mapped to a locally disabled account [${subject.id}]${subject.principal}"
  }
  
  def 'Failing subject save for new account throws RuntimeException'() {
    setup:
    mockDomain(SessionRecord)
    mockLogging(FederatedRealm, true)
    def realm = new FederatedRealm()
    
    mockConfig '''
    aaf{
      base{
        realms{
          federated{
            active = true
            auto_provision = true
            app.subject = 'aaf.base.identity.Subject'
          }
        }
      }
    }
    ''' 
    realm.grailsApplication = [config: ConfigurationHolder.config]
    
    def token = new FederatedToken(principal:'http://test.com!http://sp.test.com!1234', credential:'1234-mockid-5678', 
      attributes:[cn:'Fred Bloggs', email:'fred@uni.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsghGbz1'], userAgent:'Google Chrome X.Y')
    Subject.metaClass.'static'.withTransaction = { Closure c -> c() }
    aaf.base.identity.Subject.metaClass.save = { null }
    
    when:
    realm.authenticate(token)
    
    then:
    RuntimeException e = thrown()
    e.message == "Account creation exception for new federated account for http://test.com!http://sp.test.com!1234"
  }
  
  def 'Failing subject save for session record throws RuntimeException'() {
    setup:
    mockDomain(SessionRecord)
    mockLogging(FederatedRealm, true)
    def realm = new FederatedRealm()
    
    mockConfig '''
    aaf{
      base{
        realms{
          federated{
            active = true
            auto_provision = true
            app.subject = 'aaf.base.identity.Subject'
          }
        }
      }
    }
    ''' 
    realm.grailsApplication = [config: ConfigurationHolder.config]
    
    def token = new FederatedToken(principal:'http://test.com!http://sp.test.com!1234', credential:'1234-mockid-5678',
      attributes:[cn:'Fred Bloggs', email:'fred@uni.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsghGbz1'], userAgent:'Google Chrome X.Y')
    Subject.metaClass.'static'.withTransaction = { Closure c -> c() }
    
    
    new aaf.base.identity.Subject(principal:'http://test.com!http://sp.test.com!1234', cn:'Fred Bloggs', email:'fred@uni.edu.au', sharedToken:'LGW3wpNaPgwnLoYYsghGbz1', active:true).save()
    aaf.base.identity.Subject.metaClass.save = { null }
    
    when:
    realm.authenticate(token)
    
    then:
    RuntimeException e = thrown()
    e.message == "Account update exception for existing federated account ${token.principal}"
  }
  
}
