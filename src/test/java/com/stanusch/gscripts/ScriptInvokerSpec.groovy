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
                def result = restInvoker.call('https://httpbin.org/get');
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
     */
    def "Example 3: find user by calling external url"() {
        given:
            def question = 2
            def script1 = '''
                println 'user answer: ' + question;
                def result = restInvoker.call('http://www.mocky.io/v2/5b840a64310000ab280d222a/id/' + question);
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
                    println "send mail to " + email
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
     */
    def "Example 5: jaki jest status mojej przesyłki"() {
        given:
        def question = "mój numer paczki to 12213122131221312213122139 i chce wiedziec jaki status"
        def script1 = '''
                println 'user answer: ' + question;
                def paczkaMatcher = (question =~/(\\d+)/)
                if (paczkaMatcher.find()) {
                    no = paczkaMatcher.group()
                    def result = restInvoker.call('http://www.mocky.io/v2/5b8410c9310000ab280d2254/paczka/' + no);
                    statusPaczki = result.get("status").getAsString();
                }
            '''

        when:
        def outputVariables = subject.call(script1, ["question": question])
        println "statusPaczki is: " + outputVariables['statusPaczki']


        then:
        outputVariables['statusPaczki'] == "kurier dzisiaj dostarczy"
    }


    /**
     * Using https://pl.wikipedia.org/api/rest_v1/page/summary/{}
     * https://en.wikipedia.org/api/rest_v1/#!/Page_content/get_page_summary_title
     */
    def "Example 6: co to jest"() {
        given:
        def question = "co to jest samochód"
        def script1 = '''
                println 'user answer: ' + question;
                def wordToSearchMatcher = (question =~/co to jest (.*)/)
                def wordToSearch = wordToSearchMatcher[0][1]
                println wordToSearch
                def result = restInvoker.call('https://pl.wikipedia.org/api/rest_v1/page/summary/' + wordToSearch);
                description = result.get("extract").getAsString();
                println description
            '''

        when:
        def outputVariables = subject.call(script1, ["question": question])
        println "description is: " + outputVariables['description']


        then:
        outputVariables['description'] == "Samochód – pojazd silnikowy służący do przewozu osób lub ładunków."
    }

    /**
     * Using https://www.stanusch.com/kontakt
     */
    def "Example 7: extract information form page"() {
        given:
        def question = "Co jest na stronie https://www.stanusch.com/?q=fact_8385&d=Kontakt"
        def script1 = '''
                println 'user question: ' + question;
                def document = pageParser.parse("https://www.stanusch.com/?q=fact_8385&d=Kontakt")
                text = document.select("#answerContainer div:not(.picture-right)").text();
            '''

        when:
        def outputVariables = subject.call(script1, ["question": question])
        println "text is: " + outputVariables['text']


        then:
        outputVariables['text'] == "Nasze dane kontaktowe: Stanusch Technologies SA, Śląski Inkubator Przedsiębiorczości, " +
                "ul. K. Goduli 36, 41-712 Ruda Śląska. Tel: +48 (32) 248 01 43, Fax: +48 (32) 248 01 43, E-mail: biuro@stanusch.com. " +
                "Możesz skorzystać z formularza kontaktowego. Sprzedażą zajmują się: Maciej Stanusch, " +
                "Prezes Zarządu: tel. 608 550 890, e-mail: ms@stanusch.com Joanna Wcisło, " +
                "Chief Commercial Officer: tel. 602-127-700, e-mail: joanna.wcislo@stanusch.com " +
                "Mogę Ci też udzielić informacji o: jak kupić Platformę ESD, oddział w Warszawie, partnerzy."
    }
}
