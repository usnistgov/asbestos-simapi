package gov.nist.toolkit.toolkitApi

import gov.nist.tk.actors.TransactionType
import gov.nist.toolkit.configDatatypes.client.PatientErrorMap
import gov.nist.toolkit.configDatatypes.server.SimulatorProperties
import gov.nist.toolkit.toolkitServicesCommon.RefList
import gov.nist.toolkit.toolkitServicesCommon.SimConfig
import gov.nist.toolkit.toolkitServicesCommon.resource.RefListResource
import groovy.transform.TypeChecked

import javax.ws.rs.core.Response

/**
 *
 */
@TypeChecked
abstract class AbstractActor implements AbstractActorInterface {
   EngineSpi engine;
   SimConfig config;

    SimConfig getConfig() {
      return config;
   }

    SimConfig update(SimConfig config) throws ToolkitServiceException {
      config = engine.update(config);
      return config;
   }

    void setConfig(SimConfig cnf) {
      config = cnf;
   }

    EngineSpi getEngine() {
      return engine;
   }

    void setEngine(EngineSpi eng) {
      engine = eng;
   }

    void delete() throws ToolkitServiceException {
      engine.delete(config.getId(), config.getUser());
   }

    /**
     * Set a property that takes a String value
     * @param name property name. See {@link SimulatorProperties} for property names.
     * @param value property value
     */
     void setProperty(String name, String value) {
        config.setProperty(name, value);
//        if (SimulatorProperties.environment.equals((name))) {
//            config.setEnvironmentName(value);
//        }
    }
    /**
     * Set a property that takes a boolean value
     * @param name property name. See {@link SimulatorProperties} for property names.
     * @param value property value
     */
     void setProperty(String name, boolean value) {
        config.setProperty(name, value);
    }
    /**
     * Is named property a boolean value?
     * @param name property name. See {@link SimulatorProperties} for property names.
     * @return boolean
     */
     boolean isBoolean(String name) { return config.isBoolean(name);}

    boolean isString(String name) {
      return config.isString(name);
   }

    boolean isList(String name) {
      return config.isList(name);
   }

    /**
     * Return named property as a String
     * @param name property name. See {@link SimulatorProperties} for property names.
     * @return String value
     */
     String asString(String name) { return config.asString(name); }
    /**
     * Return named property as a String
     * @param name property name. See {@link SimulatorProperties} for property names.
     * @return boolean value
     */
     boolean asBoolean(String name) { return config.asBoolean(name); }

    List <String> asList(String name) {
      return config.asList(name);
   }

   /**
    * Describe Simulator Configuration.
    *
    * @return Description string.
    */
    String describe() {
      return config.describe();
   }

    String getId() {
      return config.getId();
   }

    String getEnvironmentName() {
      return config.getEnvironmentName();
   }

    String getActorType() {
      return config.getActorType();
   }

    void setProperty(String name, List <String> value) {
      config.setProperty(name, value);
   }

    String getFullId() {
      return config.getFullId();
   }

    String getUser() {
      return config.getUser();
   }

    Collection <String> getPropertyNames() {
      return config.getPropertyNames();
   }

    RefList getEventIds(String simId, TransactionType transaction) throws ToolkitServiceException {
      Response response = engine.getTarget()
         .path(String.format("simulators/%s/events/%s", getConfig().getFullId(), transaction.getShortName())).request()
         .get();
      if (response.getStatus() != 200) throw new ToolkitServiceException(response);
      return response.readEntity(RefListResource.class);
   }

    RefList getEvent(String simId, TransactionType transaction, String eventId) throws ToolkitServiceException {
      Response response =
         engine.getTarget()
            .path(
               String.format("simulators/%s/event/%s/%s", getConfig().getFullId(), transaction.getShortName(), eventId))
            .request().get();
      if (response.getStatus() != 200) throw new ToolkitServiceException(response);
      return response.readEntity(RefListResource.class);

   }

    @Override
     void setPatientErrorMap(PatientErrorMap errorMap) throws IOException {
//        config.setPatientErrorMap(errorMap);
    }

    @Override
     PatientErrorMap getPatientErrorMap() throws IOException {
        return null;
//        return config.getPatientErrorMap();
    }

    @Override
     boolean isFhir() { return false; }
}
