package gov.nist.asbestos.simapi.sim.headers

class HeaderBuilder {

    // does not include URI line
    static String headersAsString(RawHeaders headers) {
        Headers heads = parseHeaders(headers)
        heads.toString()
    }

    static RawHeaders rawHeadersFromString(String input) {
        Map<String, List<String>> lines = [:]
        String uriLine = null

        input.eachLine {
            if (!it.contains(':')) {
                uriLine = it
                return
            }
            String[] nameValue = it.split(':', 2)
            assert nameValue.size() == 2
            String name = nameValue[0].trim()
            String value = nameValue[1].trim()
            List<String> values = value.split(';')
            assert values.size() > 0
            (0..<values.size()).each { int i ->
                values[i] = values[i].trim()
            }
            lines.put(name, values)
        }

        RawHeaders rawHeaders = new RawHeaders(lines)
        rawHeaders.uriLine = uriLine


        rawHeaders
    }

    static Headers parseHeaders(String headers) {
        parseHeaders(rawHeadersFromString(headers))
    }

    static Headers parseHeaders(RawHeaders rawHeaders) {
        Headers headers = new Headers()

        String[] lineParts = rawHeaders.uriLine.split()
        assert lineParts.size() == 2 : "HeaderBuilder : URI line should have two elements, has ${lineParts.size()}"
        headers.verb = lineParts[0]
        headers.pathInfo = lineParts[1]

        List<String> names = rawHeaders.names
        names.each { String name ->
            List<String> values = rawHeaders.headers.get(name)
            values.each { String value ->
                headers.nameValueList << new NameValue([name: name, value: value])
            }
        }

        headers
    }

    static Headers parseHeaders(Map<String, List<String>> theHeaders) {
        Headers headers = new Headers()

        List<String> names = theHeaders.keySet() as List
        names.each {String name ->
            List<String> values = theHeaders.get(name)
            values.each {String value ->
                headers.nameValueList << new NameValue([name: name, value: value])
            }
        }

        headers
    }
}
