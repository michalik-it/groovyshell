package com.stanusch.gscripts

import spock.lang.Specification

class ScriptInvokerSpec extends Specification {
    ScriptInvoker subject = new ScriptInvoker()

    def "Example 1: println"() {
        given:
            def script1 = '''
                println "hey CAP ... welcome in 2018"
            '''
        when:
            subject.call(script1)

        then:
            true
    }

    def "Example 2: call external url: https://httpbin.org/get and get response Host value"() {
        given:
            def script1 = '''
                def result = invoker.call('https://httpbin.org/get');
                header = result.getAsJsonObject("headers").get("Host").getAsString();
            '''

        when:
            def outputVariables = subject.call(script1)
            def host = (String)outputVariables['header']
            println "Host value is: " + host

        then:
            host == "httpbin.org"
    }

    /**
     * Using https://www.mocky.io
     * Response: {"data":{"id":2,"first_name":"Janet","last_name":"Weaver","avatar":"https://s3.amazonaws.com/uifaces/faces/twitter/josephstein/128.jpg"}}
     * Mock url: http://www.mocky.io/v2/5b840a64310000ab280d222a
     * @return
     */
    def "Example 3: find user by calling external url"() {
        given:
            def question = 2
            def script1 = '''
                println 'user answer: ' + question;
                def result = invoker.call('http://www.mocky.io/v2/5b840a64310000ab280d222a/id/' + question);
                firstName = result.getAsJsonObject("data").get("first_name").getAsString();
                lastName = result.getAsJsonObject("data").get("last_name").getAsString();
            '''

        when:
            def outputVariables = subject.call(script1, ["question": question])
            println "first name is: " + outputVariables['firstName']
            println "last name is: " + outputVariables['lastName']

        then:
            outputVariables['firstName'] == "Janet"
            outputVariables['lastName'] == "Weaver"
    }

    def "Example 4: mój email to "() {
        given:
            def question = "mój email to kamil@michalik.it 123"
            def script1 = '''
                println 'user answer: ' + question;
                def emailMatcher = (question =~/([a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+)/)
                if (emailMatcher.find()) {
                    email = emailMatcher.group()
                }
            '''

        when:
            def outputVariables = subject.call(script1, ["question": question])
            println "email is: " + outputVariables['email']


        then:
            outputVariables['email'] == "kamil@michalik.it"
    }

    /**
     * Using https://www.mocky.io
     * Response: {"rate":"4"}
     * Mock url: http://www.mocky.io/v2/5b840a64310000ab280d222a
     * @return
     */
    def "Example 4: jaki jest staus mojej przesyłki"() {
        given:
        def question = "mój numer paczki to 12213122131221312213122139 i chce wiedziec jaki status"
        def script1 = '''
                println 'user answer: ' + question;
                def paczkaMatcher = (question =~/(\\d+)/)
                if (paczkaMatcher.find()) {
                    no = paczkaMatcher.group()
                    def result = invoker.call('http://www.mocky.io/v2/5b8410c9310000ab280d2254/paczka/' + no);
                    statusPaczki = result.get("status").getAsString();
                }
            '''

        when:
        def outputVariables = subject.call(script1, ["question": question])
        println "statusPaczki is: " + outputVariables['statusPaczki']


        then:
        outputVariables['statusPaczki'] == "kurier dzisiaj dostarczy"
    }
}
