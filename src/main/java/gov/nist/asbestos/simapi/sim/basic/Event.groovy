package gov.nist.asbestos.simapi.sim.basic

import gov.nist.asbestos.simapi.sim.headers.Headers
import gov.nist.asbestos.simapi.sim.headers.RawHeaders
import gov.nist.asbestos.simapi.tk.simCommon.SimId
import groovy.transform.TypeChecked

@TypeChecked
class Event {
//    RawHeaders _requestRawHeaders = null
    Headers _requestHeaders = null
    byte[] _requestRawBody = null
    String _requestBody = null

    RawHeaders _responseRawHeaders = null
    Headers _responseHeaders = null
    byte[] _responseRawBody = null
    String _responseBody = null

    EventStore store = null
    SimId simId = null
    String resource = null
    String eventId = null // within resource

    Event() {

    }

    Event(EventStore store, SimId simId, String resource, String eventId) {
        this.store = store
        this.simId = simId
        this.resource = resource
        this.eventId = eventId
    }

    boolean isComplete() {
        store && simId && resource && eventId
    }



}
