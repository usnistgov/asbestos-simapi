package gov.nist.asbestos.simapi.http

class HttpDelete  {
    void run(String url) {
        assert url
        HttpURLConnection connection
        try {
            connection = (HttpURLConnection) new URL(url).openConnection()
            connection.setDoOutput(true)
            connection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded" )
            connection.setRequestMethod('DELETE')
            connection.getResponseCode()
        } finally {
            if (connection)
                connection.disconnect()
        }
    }
}
