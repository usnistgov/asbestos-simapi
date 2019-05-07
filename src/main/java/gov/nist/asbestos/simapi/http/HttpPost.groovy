package gov.nist.asbestos.simapi.http

import gov.nist.asbestos.adapter.StackTrace
import groovy.transform.TypeChecked
import org.apache.log4j.Logger

@TypeChecked
class HttpPost  extends HttpGeneralRequest {
    static Logger log = Logger.getLogger(HttpPost);

    void post(String url, Map<String, String> headers, byte[] content) {
        HttpURLConnection connection

        try {
            connection = (HttpURLConnection) new URL(url).openConnection()
            if (headers)
                addHeaders(connection, headers)
            requestHeadersList = connection.getRequestProperties()
            connection.setRequestMethod('POST')
            connection.setDoOutput(true)
            connection.setDoInput(true)
            // TODO use proper charset (from input)
            if (content)
                connection.getOutputStream().write(content)
            status = connection.getResponseCode()
            if (status == HttpURLConnection.HTTP_OK || HttpURLConnection.HTTP_CREATED) {
                //connection.getHeaderFields()
                responseHeadersList = connection.getHeaderFields()
            }
            try {
                byte[] bb = connection.inputStream.bytes
                setResponse(bb)
            } catch (Throwable t) {
                log.info StackTrace.stackTraceAsString(t)
                throw t
            }
        } finally {
            if (connection)
                connection.disconnect()
            //requestHeadersList = connection.getRequestProperties()
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
