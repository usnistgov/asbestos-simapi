package gov.nist.tk.simCommon

import gov.nist.tk.actors.ActorType
import gov.nist.tk.actors.TransactionType
import groovy.transform.TypeChecked

/**
 * See SimDbEvents for how this is used
 */
@TypeChecked
class SimDbEvent {
    SimId simId
    String actor
    String trans
    String eventId // These sort in time order

    SimDbEvent(SimId simId, String actor, String trans, String eventId) {
        this.simId = simId
        this.actor = actor
        this.trans = trans
        this.eventId = eventId
    }

    SimDb open() {
        return SimDb.open(this)
    }

    String getSimLogUrl() {
        SimLogEventLinkBuilder.buildUrl(Installation.instance().getToolkitBaseUrl(), simId.toString(), actor, trans, eventId)
    }

    String getSimLogPlaceToken() {
        SimLogEventLinkBuilder.buildToken(simId.toString(), actor, trans, eventId)
    }

    ActorType getActorType() { return ActorType.findActor(actor) }

    TransactionType getTransactionType() { return TransactionType.find(trans) }

    File getRequestBodyFile() {
        return open().getRequestBodyFile()
    }

    File getRequestHeaderFile() {
        return open().getRequestHeaderFile()
    }

    File getResponseBodyFile() {
        return open().getResponseBodyFile()
    }

    File getResponseHeaderFile() {
        return open().getResponseHdrFile()
    }

    byte[] getRequestBody() {
        return getRequestBodyFile().bytes
    }

    String getRequestHeader() {
        return getRequestHeaderFile().text
    }

    byte[] getResponseBody() {
        return getResponseBodyFile().bytes
    }

    String getResponseHeader() {
        return getResponseHeaderFile().text
    }

    void setActor(String actor) {
        this.actor = actor
    }

    void setTrans(String trans) {
        this.trans = trans
    }
}
