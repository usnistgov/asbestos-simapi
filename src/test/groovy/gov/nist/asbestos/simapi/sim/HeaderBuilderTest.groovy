package gov.nist.asbestos.simapi.sim

import gov.nist.asbestos.simapi.sim.headers.HeaderBuilder
import gov.nist.asbestos.simapi.sim.headers.Headers
import gov.nist.asbestos.simapi.sim.headers.RawHeaders
import spock.lang.Specification

class HeaderBuilderTest extends Specification {
    def input = '''POST /default__1/balloon/pop\r
content-type: application/json\r
user-agent: Java/1.8.0_191\r
host: localhost:8080\r
accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r
connection: keep-alive\r
content-length: 31'''

    def 'typical' () {
        when:
        RawHeaders r = HeaderBuilder.rawHeadersFromString(input)
        List<String> acceptList = r.headers['accept']
        List<String> hostList = r.headers['host']
        println "names ${r.names}"

        then:
        r.uriLine == 'POST /default__1/balloon/pop'
        r.names == ['content-type', 'user-agent', 'host', 'accept', 'connection', 'content-length']
        hostList == ['localhost:8080']
        acceptList[0] == 'text/html, image/gif, image/jpeg, *'
        acceptList[2] == 'q=.2'
    }

    def 'raw <-> string' () {
        when:
        RawHeaders raw = HeaderBuilder.rawHeadersFromString(input)
        String str = HeaderBuilder.headersAsString(raw).trim()

        then:
        input == str
    }

    def 'from map' () {
        setup:
        Map<String, List<String>> input = [:]
        input['content-type'] = ['application/json']
        input['accept'] = ['text/html, image/gif, image/jpeg, *', 'q=.2, */*', 'q=.2']

        when:
        Headers headers = HeaderBuilder.parseHeaders(input)

        then:
        headers.nameValueList[0].name == 'content-type'
        headers.nameValueList[0].value == 'application/json'
        headers.nameValueList[1].name == 'accept'
        headers.nameValueList[1].value == 'text/html, image/gif, image/jpeg, *'
        headers.nameValueList[2].name == 'accept'
        headers.nameValueList[2].value == 'q=.2, */*'
        headers.nameValueList[3].name == 'accept'
        headers.nameValueList[3].value == 'q=.2'
    }
}
