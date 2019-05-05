package gov.nist.asbestos.simapi.sim.basic

import gov.nist.asbestos.simapi.sim.headers.Headers
import gov.nist.asbestos.simapi.sim.headers.HeaderBuilder
import gov.nist.asbestos.simapi.sim.headers.RawHeaders
import groovy.transform.TypeChecked

// TODO split off IO portion to EventStore

/**
 * An Event is a request (the trigger) and any number of tasks undertaken
 * to satisfy that request.
 */
@TypeChecked
class Event {
    SimStore simStore
    File root
    File _request = null // interaction with client
    List<File> _tasks = [] // downstream/backend interactions
    File current = null // either request or a task

    Event(SimStore simStore, File eventDir) {
        this.simStore = simStore
        this.root = eventDir
        int i = 0
        while (true) {
            File taskFile = getTaskFile(i)
            if (taskFile.exists())
                _tasks[i] = taskFile
            else
                break
            i++
        }
        clearCache()
    }

    /**
     * creates request (if doesn't exist) and sets it as current
     * @return the request dir
     */
    File getRequest() {
        if (!_request) {
            _request = new File(root, 'request')
            _request.mkdir()
        }
        current = _request
        clearCache()
        _request
    }

    File getTaskFile(int i) {
        new File(root, "task${i}")
    }

    /**
     * creates new task and sets it as current
     * @return the task dir
     */
    Event newTask() {
        int i = _tasks.size()
        File task = getTaskFile(i)
        task.mkdir()
        current = task
        _tasks << task
        clearCache()
        this
    }

    int getTaskCount() {
        _tasks.size()
    }

    /**
     * Select a task as current
     * @param i
     */
    void selectTask(int i) {
        assert i < taskCount : "Event: cannot return task #${i} - only ${taskCount} tasks\n"
        current = _tasks[i]
        clearCache()
    }

    /**
     * select request as current
     * @return
     */
    Event selectRequest() {
        if (!_request)
            getRequest()
        current = _request
        clearCache()
        this
    }

    void clearCache() {
        requestRawHeaders = null
        requestHeaders = null
        requestRawBody = null
        requestBody = null
        responseRawHeaders = null
        responseHeaders = null
        responseRawBody  = null
        responseBody = null
    }

    private File getRequestHeaderFile() { new File(current, 'request_header.txt') }
    private File getRequestBodyFile() { new File(current, 'request_body.bin') }
    private File getRequestBodyStringFile() {  new File(current, 'request_body.txt') }
    private File getResponseHeaderFile() {  new File(current, 'response_header.txt') }
    private File getResponseBodyFile() {  new File(current, 'response_body.bin') }
    private File getResponseBodyStringFile() {  new File(current, 'response_body.txt') }
    private File getResponseBodyHTMLFile() {  new File(current, 'response_body.html') }

    RawHeaders requestRawHeaders = null
    Headers requestHeaders = null
    byte[] requestRawBody = null
    String requestBody = null

    RawHeaders responseRawHeaders = null
    Headers responseHeaders = null
    byte[] responseRawBody  = null
    String responseBody = null

    void putRequestHeader(RawHeaders rawHeaders) {
        requestRawHeaders = rawHeaders
        requestHeaders = HeaderBuilder.parseHeaders(rawHeaders)
        current.mkdirs()
//        String txt = "${rawHeaders.uriLine}\r\n${HeaderBuilder.headersAsString(rawHeaders)}"
        String txt = "${HeaderBuilder.headersAsString(rawHeaders)}"
        requestHeaderFile.text = txt
    }

    void putRequestHeader(Headers headers) {
        requestHeaders = headers
        current.mkdirs()
        requestHeaderFile.text = headers.toString()
    }

    void putRequestBody(byte[] body) {
        requestRawBody = body
        current.mkdirs();
        requestBodyFile.withOutputStream { it.write body }
        requestBody = new String(body)
        requestBodyStringFile.text = requestBody
    }
    Headers getRequestHeader() {
        if (!requestHeaders) {
            String headerString = requestHeaderFile.text
            requestRawHeaders = HeaderBuilder.rawHeadersFromString(headerString)
            requestHeaders = HeaderBuilder.parseHeaders(requestRawHeaders)
        }
        requestHeaders
    }
    byte[] getRequestBody() {
        if (!requestRawBody) {
            requestRawBody = requestBodyFile.readBytes()
            requestBody = new String(requestRawBody)
        }
        requestRawBody
    }

    String getRequestBodyAsString() {
        getRequestBody()
        requestBody
    }

    void putResponseHeader(Headers headers) {
        responseHeaders = headers
        putResponseHeaderInternal(headers.toString())
    }

    private void putResponseHeaderInternal(String header) {
        current.mkdirs()
        responseHeaderFile.text = header
    }

    void putResponseBody(byte[] body) {
        current.mkdirs();
        responseBodyFile.withOutputStream { it.write body }
        responseRawBody = body
        responseBody = new String(body)
        responseBodyStringFile.text = responseBody
    }

    void putResponseBodyText(String body) {
        responseBodyStringFile.text = body
    }

    void putResponseHTMLBody(byte[] body) {
        current.mkdirs();
        responseBodyFile.withOutputStream { it.write body }
        responseRawBody = body
        responseBody = new String(body)
        responseBodyStringFile.text = responseBody
        responseBodyHTMLFile.text = responseBody
    }

    String getResponseHeader() {
        if (!responseHeaders) {
            String headerString = responseHeaderFile.text
            responseRawHeaders = HeaderBuilder.rawHeadersFromString(headerString)
            responseHeaders = HeaderBuilder.parseHeaders(responseRawHeaders)
        }
        responseHeaders
    }

    byte[] getResponseBody() {
        if (!responseRawBody) {
            responseRawBody = responseBodyFile.readBytes()
            responseBody = new String(responseRawBody)
        }
        responseRawBody
    }

    String getResponseBodyAsString() {
        getResponseBody()
        responseBody
    }

}
