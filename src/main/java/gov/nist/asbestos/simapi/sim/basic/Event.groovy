package gov.nist.asbestos.simapi.sim.basic

import gov.nist.asbestos.simapi.sim.headers.Headers
import gov.nist.asbestos.simapi.sim.headers.RawHeaders
import groovy.transform.TypeChecked

@TypeChecked
class Event {
    RawHeaders _requestRawHeaders = null
    Headers _requestHeaders = null
    byte[] _requestRawBody = null
    String _requestBody = null

    RawHeaders _responseRawHeaders = null
    Headers _responseHeaders = null
    byte[] _responseRawBody = null
    String _responseBody = null

    EventStore store

    // This is for creating a new event
    Event(EventStore store) {
        this.store = store
    }

//    void clearCache() {
//        _requestRawHeaders = null
//        _requestHeaders = null
//        _requestRawBody = null
//        _requestBody = null
//        _responseRawHeaders = null
//        _responseHeaders = null
//        _responseRawBody  = null
//        _responseBody = null
//    }

}
