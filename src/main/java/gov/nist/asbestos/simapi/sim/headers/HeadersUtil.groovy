package gov.nist.asbestos.simapi.sim.headers

class HeadersUtil {

    static String headersAsString(RawHeaders headers) {
        StringBuilder buf = new StringBuilder()


        Enumeration names = headers.names
        while (names.hasMoreElements()) {
            String name = names.nextElement()
            Enumeration values = headers.headers.get(name)
            while (values.hasMoreElements()) {
                String value = values.nextElement()
                buf << "${name}: ${value}\r\n"
            }
        }

        buf.toString()
    }

    static Headers parseHeaders(RawHeaders rawHeaders) {
        Headers headers = new Headers()

        Enumeration names = headers.names
        while (names.hasMoreElements()) {
            String name = names.nextElement()
            Enumeration values = headers.headers.get(name)
            while (values.hasMoreElements()) {
                String value = values.nextElement()
                headers.nameValueList << new NameValue([name: name, value: value])
            }
        }

        headers
    }
}
