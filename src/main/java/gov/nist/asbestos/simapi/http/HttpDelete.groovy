package gov.nist.asbestos.simapi.http

class HttpDelete  extends HttpBase {
    HttpDelete run(URI uri) {
        assert uri
        HttpURLConnection connection
        try {
            connection = (HttpURLConnection) uri.toURL().openConnection()
            connection.setDoOutput(true)
            connection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded" )
            connection.setRequestMethod('DELETE')
            status = connection.getResponseCode()
        } finally {
            if (connection)
                connection.disconnect()
        }
        this
    }

    HttpDelete run(String url) {
        run(new URI(url))
        this
    }

    HttpDelete run() {
        assert uri
        run(uri)
        this
    }
}
