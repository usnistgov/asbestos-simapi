package gov.nist.asbestos.simapi.http

import groovy.transform.TypeChecked

@TypeChecked
class HttpPost {

    static int post(String url, String contentType, String content) {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection()
        connection.setRequestMethod('POST')
        connection.setDoOutput(true)
        connection.setRequestProperty("Content-Type", contentType)
        connection.setRequestProperty('accept', contentType)
        if (content)
            connection.getOutputStream().write(content.getBytes("UTF-8"))
        connection.getResponseCode()
    }

    static int postJson(String url, String json) {
        post(url, 'application/json', json)
    }


}
