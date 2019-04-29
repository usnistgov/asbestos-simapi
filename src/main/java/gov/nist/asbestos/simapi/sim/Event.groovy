package gov.nist.asbestos.simapi.sim

import groovy.transform.TypeChecked

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
        _request
    }

    /**
     * creates new task and sets it as current
     * @return the task dir
     */
    File getNewTask() {
        int i = _tasks.size()
        File task = new File(root, "task${i}")
        task.mkdir()
        current = task
        _tasks << task
        task
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
    }

    /**
     * select request as current
     * @return
     */
    void selectRequest() {
        if (!_request)
            getRequest()
        current = _request
    }

    private File getRequestHeaderFile() { new File(current, 'request_header.txt') }
    private File getRequestBodyFile() { new File(current, 'request_body.bin') }
    private File getRequestBodyStringFile() {  new File(current, 'request_body.txt') }
    private File getResponseHeaderFile() {  new File(current, 'response_header.txt') }
    private File getResponseBodyFile() {  new File(current, 'response_body.bin') }
    private File getResponseBodyStringFile() {  new File(current, 'response_body.txt') }


    // these getters and setters operate on current
    void putRequestHeader(String header) { current.mkdirs(); requestHeaderFile.text = header }
    void putRequestBody(byte[] body) {
        current.mkdirs();
        requestBodyFile.withOutputStream { it.write body }
        requestBodyStringFile.text = new String(body)
    }
    String getRequestHeader() { requestHeaderFile.text }
    byte[] getRequestBody() { requestBodyFile.readBytes() }
    String getRequestBodyAsString() { new String(requestBodyFile.readBytes()) }

    void putResponseHeader(String header) { current.mkdirs(); responseHeaderFile.text = header }
    void putResponseBody(byte[] body) {
        current.mkdirs();
        responseBodyFile.withOutputStream { it.write body }
        responseBodyStringFile.text = new String(body)
    }
    String getResponseHeader() { responseHeaderFile.text }
    byte[] getResponseBody() { responseBodyFile.readBytes() }
    String getResponseBodyAsString() { new String(responseBodyFile.readBytes()) }

}
