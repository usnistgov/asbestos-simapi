package gov.nist.asbestos.simapi.http.headers

import gov.nist.asbestos.simapi.http.headers.HeaderBuilder
import gov.nist.asbestos.simapi.http.headers.Headers
import spock.lang.Specification

class HeaderTest extends Specification {

    def 'getMultiple' () {
        given:
        def headerString = '''GET /default__patient1/metadata
user-agent: HAPI-FHIR/3.7.0 (FHIR Client; FHIR 3.0.1/DSTU3; apache)
accept-charset: utf-8
accept-encoding: gzip
accept: application/fhir+xml;q=1.0, application/fhir+json;q=1.0, application/xml+fhir;q=0.9, application/json+fhir;q=0.9
host: localhost:8081
connection: Keep-Alive'''

        when:
        Headers hdrs = HeaderBuilder.parseHeaders(headerString)
        Map<String, String> accepts = hdrs.getMultiple(['accept'])

        then:
        accepts.size() == 3
        hdrs.getAll('accept-encoding') == 'gzip'
        hdrs.getAccept() == 'application/fhir+xml'
    }
}
