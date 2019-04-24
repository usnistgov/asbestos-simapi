package gov.nist.asbestos.simapi.tk.simCommon

import groovy.transform.TypeChecked;

/**
 *
 */
@TypeChecked
public class SimulatorConfigIoFactory {

    static public SimulatorConfigIo impl() { return new SimulatorConfigIoJackson(); }

}
