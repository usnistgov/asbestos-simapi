package gov.nist.asbestos.simapi.toolkit.toolkitApi


import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.DocumentContent
import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.RefList
import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.resource.DocumentContentResource
import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.resource.RefListResource
import groovy.transform.TypeChecked

import javax.ws.rs.core.Response

/**
 *
 */
@TypeChecked
 class XdsDocumentRegRep extends AbstractActor implements DocumentRegRep {
    @Override
     RefList findDocumentsForPatientID(String patientID) throws ToolkitServiceException {
        Response response = engine.getTarget()
                .path(String.format("simulators/%s/xds/GetAllDocs/%s", getConfig().getFullId(), patientID))
                .request().get();
        if (response.getStatus() != 200)
            throw new ToolkitServiceException(response);
        return response.readEntity(RefListResource.class);
    }

    @Override
     String getDocEntry(String id) throws ToolkitServiceException {
        Response response = engine.getTarget()
                .path(String.format("simulators/%s/xds/GetDoc/%s", getConfig().getFullId(), id))
                .request().get();
        if (response.getStatus() != 200)
            throw new ToolkitServiceException(response);
        return response.readEntity(String.class);
    }

    @Override
     DocumentContent getDocument(String uniqueId) throws ToolkitServiceException {
        Response response = engine.getTarget()
                .path(String.format("simulators/%s/document/%s", getConfig().getFullId(), uniqueId))
                .request().get();
        if (response.getStatus() != 200)
            throw new ToolkitServiceException(response);
        return response.readEntity(DocumentContentResource.class);
    }

   /* (non-Javadoc)
    * @see ChannelConfig#setPatientErrorMap(PatientErrorMap)
    */
   @Override
    void setPatientErrorMap(gov.nist.asbestos.simapi.toolkit.configDatatypes.client.PatientErrorMap errorMap) throws IOException {
      // TODO Auto-generated method stub

   }

   /* (non-Javadoc)
    * @see ChannelConfig#getPatientErrorMap()
    */
   @Override
    gov.nist.asbestos.simapi.toolkit.configDatatypes.client.PatientErrorMap getPatientErrorMap() throws IOException {
      // TODO Auto-generated method stub
      return null;
   }

}
