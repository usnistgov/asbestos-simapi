package gov.nist.asbestos.simapi.toolkit.toolkitApi


import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.LeafClassList
import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.resource.LeafClassListResource
import groovy.transform.TypeChecked

import javax.ws.rs.core.Response

/**
 *
 */
@TypeChecked
 class XcaRespondingGateway extends AbstractActor implements RespondingGateway {
    @Override
     LeafClassList FindDocuments(String patientID) throws ToolkitServiceException {
        Response response = engine.getTarget()
                .path(String.format("simulators/%s/xds/GetAllDocs/%s", getConfig().getFullId(), patientID))
                .request().get();
        if (response.getStatus() != 200)
            throw new ToolkitServiceException(response);
        return response.readEntity(LeafClassListResource.class);
    }

   /* (non-Javadoc)
    * @see SimConfig#setPatientErrorMap(PatientErrorMap)
    */
   @Override
    void setPatientErrorMap(gov.nist.asbestos.simapi.toolkit.configDatatypes.client.PatientErrorMap errorMap) throws IOException {
      // TODO Auto-generated method stub

   }

   /* (non-Javadoc)
    * @see SimConfig#getPatientErrorMap()
    */
   @Override
    gov.nist.asbestos.simapi.toolkit.configDatatypes.client.PatientErrorMap getPatientErrorMap() throws IOException {
      // TODO Auto-generated method stub
      return null;
   }
}