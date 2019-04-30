package gov.nist.asbestos.simapi.sim.basic

import groovy.transform.TypeChecked

@TypeChecked
class SimLog {
    File root

    SimLog(File externalCache) {
        root = new File(externalCache, '')
    }
}
