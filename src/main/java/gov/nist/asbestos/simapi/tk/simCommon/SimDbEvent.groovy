package gov.nist.asbestos.simapi.tk.simCommon


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
        SimLogEventLinkBuilder.buildUrl(gov.nist.asbestos.simapi.tk.installation.Installation.instance().getToolkitBaseUrl(), simId.toString(), actor, trans, eventId)
    }

    String getSimLogPlaceToken() {
        SimLogEventLinkBuilder.buildToken(simId.toString(), actor, trans, eventId)
    }

    gov.nist.asbestos.simapi.tk.actors.ActorType getActorType() { return gov.nist.asbestos.simapi.tk.actors.ActorType.findActor(actor) }

    gov.nist.asbestos.simapi.tk.actors.TransactionType getTransactionType() { return gov.nist.asbestos.simapi.tk.actors.TransactionType.find(trans) }

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
