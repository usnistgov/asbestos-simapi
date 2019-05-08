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
    SimId channelId = null
    String resource = null
    String eventId = null // within resource

    Event() {

    }

    Event(EventStore store, SimId channelId, String resource, String eventId) {
        this.store = store
        this.channelId = channelId
        this.resource = resource
        this.eventId = eventId
    }

    boolean isComplete() {
        store && channelId && resource && eventId
    }



}
