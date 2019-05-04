package gov.nist.asbestos.simapi.http

import gov.nist.asbestos.simapi.sim.headers.HeaderBuilder
import gov.nist.asbestos.simapi.sim.headers.Headers

// TODO do not log body on get request
class HttpGet {
    Map<String, List<String>> requestHeaders
    Map<String, List<String>> responseHeaders = null
    int status
    String response


    String get(String url, String accept, String acceptEncoding, String content) {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection()
        connection.setRequestMethod('GET')
        if (content) {
            connection.setDoOutput(content != null)
            connection.setRequestProperty("accept", accept)
        }
        connection.setRequestProperty('accept', accept)
        if (acceptEncoding)
            connection.setRequestProperty('accept-encoding', acceptEncoding)
        if (content)
            connection.getOutputStream().write(content.getBytes("UTF-8"))

        //connection.connect()
        requestHeaders = connection.getRequestProperties()
        status = connection.getResponseCode()
        if (status == HttpURLConnection.HTTP_OK) {
            responseHeaders = connection.getHeaderFields()
        }
        try {
            response = connection.inputStream.text
        } catch (Throwable t) {}
    }

    String get(String url, String accept, String acceptEncoding) {
        get(url, accept, acceptEncoding, null)
    }

    String get(String url) {
        get(url, '*/*', null)
    }

    String getResponseContentType() {
        Headers headers = HeaderBuilder.parseHeaders(responseHeaders)
        headers.contentType
    }
}
