package gov.nist.tk.simCommon

import groovy.transform.TypeChecked;


/**
 *
 */
@TypeChecked
interface SimulatorConfigIo {
    void save(SimulatorConfig sc, String filename)
    SimulatorConfig restoreSimulator(String filename)
}
