package gov.nist.asbestos.simapi.http


class HttpGet extends HttpGeneralDetails {
    // TODO GET parameters in the body
    void get(URI uri, Map<String, String> headers) {
        HttpURLConnection connection
        try {
            connection = (HttpURLConnection) uri.toURL().openConnection()
            connection.setRequestMethod('GET')
            if (headers)
                addHeaders(connection, headers)
            requestHeadersList = connection.getRequestProperties()
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
        }
    }

    void get(String url) {
        get(new URI(url), null)
    }

    void getJson(String url) {
        Map<String, String> headers = [ accept: 'application/json', 'accept-charset': 'utf-8']
        get(new URI(url), headers)
        setResponseText(new String(response))
    }

    void run() {
        assert uri
        get(uri, requestHeaders.all)
    }

}
