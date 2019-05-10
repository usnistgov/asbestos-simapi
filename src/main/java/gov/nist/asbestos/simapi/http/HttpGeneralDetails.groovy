package gov.nist.asbestos.simapi.http

import gov.nist.asbestos.simapi.sim.headers.HeaderBuilder
import gov.nist.asbestos.simapi.sim.headers.Headers
import groovy.transform.TypeChecked

@TypeChecked
abstract class HttpGeneralDetails {
    Map<String, List<String>> requestHeadersList = null
    Headers _requestHeaders = null
    Headers _responseHeaders = null
    int status
    String _responseText = null
    byte[] _response
    String _requestText = null
    byte[] _request
    URI uri

    abstract void run()

    static String parameterMapToString(Map<String, List<String>> parameterMap) {
        if (!parameterMap || parameterMap.isEmpty())
            return ''
        StringBuilder buf = new StringBuilder()

        boolean isFirst = true
//        buf.append('?')
        parameterMap.each { String name, Object ovalues ->
            List<String> values = ovalues as List<String>
            values.each { String value ->
                if (isFirst) {
                    isFirst = false
                } else {
                    buf.append('&')
                }
                buf.append(name).append('=').append(value)
            }
        }

        buf
    }

    void setResponseHeadersList(Map<String, List<String>> responseHeadersList) {
        _responseHeaders = HeaderBuilder.parseHeaders(responseHeadersList)
    }

    void setResponse(byte[] bytes) {
        _response = bytes
    }

    void setResponseText(String txt) {
        _responseText = txt
    }

    byte[] getResponse() {
        _response
    }

    void setRequest(byte[] bytes) {
        _request = bytes
    }

    void setRequestText(String txt) {
        _requestText = txt
    }

    byte[] getRequest() {
        _request
    }

    Headers setRequestHeaders(Headers hdrs) {
        _requestHeaders = hdrs
        hdrs
    }

    Headers setResponseHeaders(Headers hdrs) {
        _responseHeaders = hdrs
        hdrs
    }

    String getResponseText() {
        _responseText
    }

    Headers getRequestHeaders() {
        if (!_requestHeaders)
            _requestHeaders = HeaderBuilder.parseHeaders(requestHeadersList)
        _requestHeaders
    }

    Headers getResponseHeaders() {
        _responseHeaders
    }

    String getResponseContentType() {
        responseHeaders.contentType
    }

    String getRequestContentType() {
        requestHeaders.contentType
    }

    static addHeaders(HttpURLConnection connection, Map<String, String> headers) {
        headers.each { String name, String value ->
            connection.setRequestProperty(name, value)
        }
    }

    static addHeaders(HttpURLConnection connection, Headers headers) {
        headers.all.each { String name, String value ->
            connection.setRequestProperty(name, value)
        }
    }

}
