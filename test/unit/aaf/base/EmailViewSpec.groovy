package aaf.base

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import spock.lang.*
import grails.plugin.spock.*

@TestFor(aaf.base.EmailView)
class EmailViewSpec extends UnitSpec {

  def 'ensure name must not be null or blank'() {
    setup:
    def ev = new EmailView(content:"test content")
    mockForConstraintsTests(EmailView, [ev])

    when:
    ev.name = val
    def result = ev.validate()

    then:
    result == expectedResult

    if (!expectedResult)
      reason == ev.errors['content']

    where:
    val << [null, '', "newsletter"]
    reason << ['nullable', 'blank', '']
    expectedResult << [false, false, true]
  }


  def 'ensure content must not be null or blank and contains HTML replacement banner'() {
    setup:
    def ev = new EmailView(name:"test name")
    mockForConstraintsTests(EmailView, [ev])

    when:
    ev.content = val
    def result = ev.validate()

    then:
    result == expectedResult

    if (!expectedResult)
      reason == ev.errors['content']

    where:
    val << [null, '', "<div>content</div>"]
    reason << ['nullable', 'blank', '']
    expectedResult << [false, false, true]
  }

}
