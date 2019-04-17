package gov.nist.tk.simCommon

import gov.nist.tk.installation.Installation
import groovy.transform.TypeChecked

/**
 * FHIR extension of SimDb
 */
@TypeChecked
class FSimDb {
    File fsimDb = Installation.instance().fsimDbFile()
    SimDb simDb

    FSimDb() {
        simDb = new SimDb()
    }

    FSimDb(SimId simId) {
        simDb = new SimDb(simId)
    }

}