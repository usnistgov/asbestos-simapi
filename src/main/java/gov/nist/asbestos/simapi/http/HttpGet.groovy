package gov.nist.asbestos.simapi.http

import gov.nist.asbestos.simapi.sim.headers.HeaderBuilder
import gov.nist.asbestos.simapi.sim.headers.Headers

class HttpGet {
    Map<String, List<String>> requestHeaders
    Map<String, List<String>> responseHeaders = null
    int status
    String response


    String get(String url, String contentType, String content) {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection()
        connection.setRequestMethod('GET')
        if (content) {
            connection.setDoOutput(true)
            connection.setRequestProperty("Content-Type", contentType)
        }
        connection.setRequestProperty('accept', contentType)
        if (content)
            connection.getOutputStream().write(content.getBytes("UTF-8"))
        requestHeaders = connection.getRequestProperties()


        connection.connect()
        status = connection.getResponseCode()
        if (status == HttpURLConnection.HTTP_OK) {
            responseHeaders = connection.getHeaderFields()
        }
        response = connection.inputStream.text
    }

    String get(String url, String contentType) {
        get(url, contentType, null)
    }

    String get(String url) {
        get(url, '*/*')
    }

    String getResponseContentType() {
        Headers headers = HeaderBuilder.parseHeaders(responseHeaders)
        headers.contentType
    }
}
