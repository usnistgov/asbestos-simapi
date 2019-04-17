package gov.nist.tk.simCommon

import groovy.transform.TypeChecked;

/**
 *
 */
@TypeChecked
public class SimulatorConfigIoFactory {

    static public SimulatorConfigIo impl() { return new SimulatorConfigIoJackson(); }

}
