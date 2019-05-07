package gov.nist.asbestos.simapi.http

import groovy.transform.TypeChecked

@TypeChecked
class HttpPost  extends HttpGeneralRequest {

    void post(String url, Map<String, String> headers, byte[] content) {
        HttpURLConnection connection
        try {
            connection = (HttpURLConnection) new URL(url).openConnection()
            connection.setRequestMethod('POST')
            connection.setDoOutput(true)
            if (headers)
                addHeaders(connection, headers)
            // TODO use proper charset (from input)
            if (content)
                connection.getOutputStream().write(content)
           // requestHeadersList = connection.getRequestProperties()
            status = connection.getResponseCode()
            if (status == HttpURLConnection.HTTP_OK) {
                responseHeadersList = connection.getHeaderFields()
            }
            try {
                setResponse(connection.inputStream.bytes)
            } catch (Throwable t) {
            }
        } finally {
            if (connection)
                connection.disconnect()
            requestHeadersList = connection.getRequestProperties()
        }
    }

    void postJson(String url, String json) {
        Map<String, String> headers = ['content-type':'application/json']
        post(url, headers, json.bytes)
    }

    void run() {
        assert url
        post(url, requestHeaders.all,  request)
    }

}
