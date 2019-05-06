package gov.nist.asbestos.simapi.http

class HttpGet extends HttpGeneralRequest {

    void get(String url, Map<String, String> headers) {
        HttpURLConnection connection
        try {
            connection = (HttpURLConnection) new URL(url).openConnection()
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
            } catch (Throwable t) {
            }
        } finally {
            if (connection)
                connection.disconnect()
        }
    }

    void get(String url) {
        get(url, null)
    }

}
