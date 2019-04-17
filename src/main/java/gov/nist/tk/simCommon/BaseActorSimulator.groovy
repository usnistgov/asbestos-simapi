package gov.nist.tk.simCommon

import gov.nist.tk.actors.TransactionType
import gov.nist.tk.stubs.MessageValidatorEngine;
import groovy.transform.TypeChecked;

import java.io.IOException;

/**
 *
 */
@TypeChecked
abstract public class BaseActorSimulator {
    public SimDb  db;
    public SimCommon common;
    public SimulatorConfig config;

    abstract  public boolean run(TransactionType transactionType, MessageValidatorEngine mvc, String validation) throws IOException;

    public BaseActorSimulator() {}

    public BaseActorSimulator(SimCommon simCommon) {
        this.common = simCommon;
        db = simCommon.db;
    }

    public void init(SimulatorConfig config) {
        this.config = config;
    }

    // Services may need extension via hooks.  These are the hooks
    // They are meant to be overloaded
    public void onCreate(SimulatorConfig config) {}
    public void onDelete(SimulatorConfig config) {}

    public void onTransactionBegin(SimulatorConfig config) {}
    public void onTransactionEnd(SimulatorConfig config) {}

    // simulatorConfig guaranteed to be initialized
    public void onServiceStart(SimulatorConfig config) {}  // these two refer to Servlet start/stop
    public void onServiceStop(SimulatorConfig config) {}

}
