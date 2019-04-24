package gov.nist.asbestos.simapi.tk.simCommon


import groovy.transform.TypeChecked

/**
 * FHIR extension of SimDb
 */
@TypeChecked
class FSimDb {
    File fsimDb = gov.nist.asbestos.simapi.tk.installation.Installation.instance().fsimDbFile()
    SimDb simDb

    FSimDb() {
        simDb = new SimDb()
    }

    FSimDb(SimId simId) {
        simDb = new SimDb(simId)
    }

}
