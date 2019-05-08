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

    RawHeaders _requestRawHeaders = null
    Headers _requestHeaders = null
    byte[] _requestRawBody = null
    String _requestBody = null

    RawHeaders _responseRawHeaders = null
    Headers _responseHeaders = null
    byte[] _responseRawBody = null
    String _responseBody = null

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
    Task newTask() {
        int i = _tasks.size()
        File task = getTaskFile(i)
        task.mkdir()
        current = task
        _tasks << task
        clearCache()
        new Task(this, i)
    }

    int getTaskCount() {
        _tasks.size()
    }

    /**
     * Select a task as current
     * @param i selectTask(-1) is the same as selectRequest() except for the return type
     */
    Task selectTask(int i) {
        assert i < taskCount : "Event: cannot return task #${i} - only ${taskCount} tasks\n"
        if (i < 0)
            current = _request
        else
            current = _tasks[i]
        clearCache()
        new Task(this, i)
    }

    Task selectClientTask() {
        selectTask(-1)
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
        _requestRawHeaders = null
        _requestHeaders = null
        _requestRawBody = null
        _requestBody = null
        _responseRawHeaders = null
        _responseHeaders = null
        _responseRawBody  = null
        _responseBody = null
    }

    private File getRequestHeaderFile() { new File(current, 'request_header.txt') }
    private File getRequestBodyFile() { new File(current, 'request_body.bin') }
    private File getRequestBodyStringFile() {  new File(current, 'request_body.txt') }
    private File getResponseHeaderFile() {  new File(current, 'response_header.txt') }
    private File getResponseBodyFile() {  new File(current, 'response_body.bin') }
    private File getResponseBodyStringFile() {  new File(current, 'response_body.txt') }
    private File getResponseBodyHTMLFile() {  new File(current, 'response_body.html') }
    private File getRequestBodyHTMLFile() {  new File(current, 'request_body.html') }

    void putRequestHeader(RawHeaders rawHeaders) {
        _requestRawHeaders = rawHeaders
        _requestHeaders = HeaderBuilder.parseHeaders(rawHeaders)
        current.mkdirs()
        String txt = "${HeaderBuilder.headersAsString(rawHeaders)}"
        requestHeaderFile.text = txt
    }

    void putRequestHeader(Headers headers) {
        _requestHeaders = headers
        current.mkdirs()
        requestHeaderFile.text = headers.toString()
    }

    void putRequestBody(byte[] body) {
        _requestRawBody = body
        current.mkdirs();
        if (body.size()) {
            requestBodyFile.withOutputStream { it.write body }
            _requestBody = new String(body)
            requestBodyStringFile.text = requestBody
        }
    }
    Headers getRequestHeader() {
        if (!_requestHeaders) {
            String headerString = requestHeaderFile.text
            _requestRawHeaders = HeaderBuilder.rawHeadersFromString(headerString)
            _requestHeaders = HeaderBuilder.parseHeaders(_requestRawHeaders)
        }
        _requestHeaders
    }
    byte[] getRequestBody() {
        if (!_requestRawBody) {
            _requestRawBody = requestBodyFile.readBytes()
            _requestBody = new String(_requestRawBody)
        }
        _requestRawBody
    }

    String getRequestBodyAsString() {
        getRequestBody()
        _requestBody
    }

    void putResponseHeader(Headers headers) {
        _responseHeaders = headers
        putResponseHeaderInternal(headers.toString())
    }

    private void putResponseHeaderInternal(String header) {
        current.mkdirs()
        File f = responseHeaderFile
        f.text = header
    }

    void putResponseBody(byte[] body) {
        current.mkdirs();
        responseBodyFile.bytes = body
        _responseRawBody = body
    }

    void putResponseBodyText(String body) {
        responseBodyStringFile.text = body
    }

    void putRequestBodyText(String body) {
        requestBodyStringFile.text = body
    }

    void putResponseHTMLBody(byte[] body) {
        current.mkdirs();
        responseBodyFile.withOutputStream { it.write body }
        _responseRawBody = body
        _responseBody = new String(body)
        responseBodyStringFile.text = responseBody
        responseBodyHTMLFile.text = responseBody
    }

    void putRequestHTMLBody(byte[] body) {
        current.mkdirs();
        requestBodyFile.withOutputStream { it.write body }
        _requestRawBody = body
        _requestBody = new String(body)
        requestBodyStringFile.text = requestBody
        requestBodyHTMLFile.text = requestBody
    }

    String getResponseHeader() {
        if (!_responseHeaders) {
            String headerString = responseHeaderFile.text
            _responseRawHeaders = HeaderBuilder.rawHeadersFromString(headerString)
            _responseHeaders = HeaderBuilder.parseHeaders(_responseRawHeaders)
        }
        _responseHeaders
    }

    byte[] getResponseBody() {
        if (!_responseRawBody) {
            _responseRawBody = responseBodyFile.readBytes()
            _responseBody = new String(_responseRawBody)
        }
        _responseRawBody
    }

    String getResponseBodyAsString() {
        getResponseBody()
        _responseBody
    }

}
