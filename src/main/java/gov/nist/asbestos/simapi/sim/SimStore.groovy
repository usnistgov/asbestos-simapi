package gov.nist.asbestos.simapi.sim


import gov.nist.asbestos.simapi.tk.simCommon.SimId

/**
 * Store is organized as:
 * EC/testSession/fsimdb/simId/actor/transaction/event/event_files
 *                                                     actor/content_files_from_this_event
 * event_files are request_hdr.txt, request_body.txt etc.
 */
class SimStore {
    File externalCache
    private File _simStoreLocation = null
    private File _actorDir = null
    private File _transactionDir = null
    private File _eventDir = null
    SimId simId
    String transaction = null
    String eventId = null // within transaction

    SimStore(File externalCache, SimId simId) {
        assert externalCache : "SimStore: initialized with externalCache == null"
        String problems = simId.validateState()
        assert !problems : "SimStore: initialized with SimId with problems:\n ${problems}"
        this.externalCache = externalCache
        this.simId = simId
    }

    File getStore() {
        if (!_simStoreLocation) {
            _simStoreLocation = new File(new File(externalCache, 'fsimdb'), simId.testSession.value)
            _simStoreLocation.mkdirs()
            assert _simStoreLocation.exists() && _simStoreLocation.canWrite() && _simStoreLocation.isDirectory() :
                    "SimStore: cannot create writable simdb directory at ${_simStoreLocation}"
        }
        _simStoreLocation
    }

    String getActor() {
        simId.actorType
    }

    File getActorDir() {
        assert actor : "SimStore: actor is null"
        if (!_actorDir)
            _actorDir = new File(store, actor)
        _actorDir
    }

    File getTransactionDir() {
        assert transaction : 'SimStore: transaction is null'
        if (!_transactionDir)
            _transactionDir = new File(actorDir(), transaction)
        _transactionDir
    }

    File getEventDir() {
        assert eventId : "SimStore: eventId is null"
        if (!_eventDir)
            _eventDir = new File(transactionDir(), eventId)
        _eventDir
    }

    File createEvent() {
        createEvent(new Date())
    }

    File createEvent(Date date) {
        createEventDir(date.getDateTimeString())
    }

    File useEvent(String eventId) {
        createEventDir(eventId)
    }

    /**
     * Given base name of a new event - extend it to ensure it is unique.
     * Does not create the directory, just the File
     * @param eventBase - usually date/time stamp
     * @return
     */
    private File createEventDir(String eventBase) {
        int incr = 0;
        while (true) {
            if (incr != 0)
                eventId = eventBase + '_' + incr    // make unique
            if (eventDir.exists()) {
                // must be fresh new dir - try again
                incr++
            }
            else
                break
        }
        eventDir
    }

    File getRequestHeaderFile() { new File(eventDir, 'request_header.txt') }
    File getRequestBodyFile() { new File(eventDir, 'request_body.bin') }
    File getRequestBodyStringFile() { new File(eventDir, 'request_body.txt') }
    File getResponseHeaderFile() { new File(eventDir, 'response_header.txt') }
    File getResponseBodyFile() { new File(eventDir, 'response_body.bin') }
    File getResponseBodyStringFile() { new File(eventDir, 'response_body.txt') }

    void putRequestHeader(String header) { requestHeaderFile.text = header }
    void putRequestBody(byte[] body) {
        requestBodyFile.withOutputStream { it.write body }
        requestBodyStringFile.text = new String(body)
    }
    String getRequestHeader() { requestHeaderFile.text }
    byte[] getRequestBody() { requestBodyFile.readBytes() }
    String getRequestBodyAsString() { new String(requestBodyFile.readBytes()) }

    void putResponseHeader(String header) { responseHeaderFile.text = header }
    void putResponseBody(byte[] body) {
        responseBodyFile.withOutputStream { it.write body }
        responseBodyStringFile.text = new String(body)
    }
    String getResponseHeader() { responseHeaderFile.text }
    byte[] getResponseBody() { responseBodyFile.readBytes() }
    String getResponseBodyAsString() { new String(responseBodyFile.readBytes()) }
}
