package gov.nist.asbestos.simapi.http

import gov.nist.asbestos.simapi.sim.headers.HeaderBuilder
import gov.nist.asbestos.simapi.sim.headers.Headers

// TODO do not log body on get request
class HttpGet {
    Map<String, List<String>> requestHeaders
    Map<String, List<String>> responseHeaders = null
    int status
    String _responseText
    byte[] _response


    String get(String url, Map<String, String> headers) {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection()
        connection.setRequestMethod('GET')
        if (headers)
            addHeaders(connection, headers)
        requestHeaders = connection.getRequestProperties()
        status = connection.getResponseCode()
        if (status == HttpURLConnection.HTTP_OK) {
            responseHeaders = connection.getHeaderFields()
        }
        try {
            putResponse(connection.inputStream.bytes)
        } catch (Throwable t) {}
    }

    void putResponse(byte[] bytes) {
        _response = bytes
        _responseText = new String(response)
    }

    byte[] getResponse() {
        _response
    }

    String get(String url) {
        get(url, null)
    }

    String getResponseContentType() {
        Headers headers = HeaderBuilder.parseHeaders(responseHeaders)
        headers.contentType
    }

    static addHeaders(HttpURLConnection connection, Map<String, String> headers) {
        headers.each { String name, String value ->
            connection.setRequestProperty(name, value)
        }
    }
}
