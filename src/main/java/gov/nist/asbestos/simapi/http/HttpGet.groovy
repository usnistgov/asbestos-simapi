package gov.nist.asbestos.simapi.http


class HttpGet extends HttpGeneralDetails {
    // TODO GET parameters in the body
    void get(String url, Map<String, String> headers) {
        url = url + parameterMapAsString()
        HttpURLConnection connection
        try {
            connection = (HttpURLConnection) new URL(url).openConnection()
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
        get(url, null)
    }

    void getJson(String url) {
        Map<String, String> headers = [ accept: 'application/json', 'accept-charset': 'utf-8']
        get(url, headers)
        setResponseText(new String(response))
    }

    void run() {
        assert url
        get(url, requestHeaders.all)
    }

}
