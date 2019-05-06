package gov.nist.asbestos.simapi.http

import gov.nist.asbestos.simapi.sim.headers.HeaderBuilder
import gov.nist.asbestos.simapi.sim.headers.Headers
import groovy.transform.TypeChecked

@TypeChecked
abstract class HttpGeneralRequest {
    Map<String, List<String>> requestHeaders
    Map<String, List<String>> responseHeaders = null
    int status
    String _responseText
    byte[] _response
    String _requestText
    byte[] _request

    void putResponse(byte[] bytes) {
        _response = bytes
        _responseText = new String(response)
    }

    byte[] getResponse() {
        _response
    }

    void putRequest(byte[] bytes) {
        _request = bytes
        _requestText = new String(response)
    }

    byte[] getRequest() {
        _request
    }

    String getResponseContentType() {
        Headers headers = HeaderBuilder.parseHeaders(responseHeaders)
        headers.contentType
    }

    String getRequestContentType() {
        Headers headers = HeaderBuilder.parseHeaders(requestHeaders)
        headers.contentType
    }

    static addHeaders(HttpURLConnection connection, Map<String, String> headers) {
        headers.each { String name, String value ->
            connection.setRequestProperty(name, value)
        }
    }

}
