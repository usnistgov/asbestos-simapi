package gov.nist.asbestos.simapi.toolkit.toolkitApi


import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.LeafClassRegistryResponse
import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.RetrieveRequest
import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.RetrieveResponse
import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.StoredQueryRequest
import groovy.transform.TypeChecked

/**
 *
 */
@TypeChecked
 class XdsDocumentConsumer extends AbstractActor implements DocumentConsumer {
    @Override
     LeafClassRegistryResponse queryForLeafClass(StoredQueryRequest request) throws ToolkitServiceException {
        return engine.queryForLeafClass(request);
    }

//    @Override
//     RefList queryForObjectRef(String queryId, Map<String, List<String>> parameters) {
//        return null;
//    }

    @Override
     RetrieveResponse retrieve(RetrieveRequest request) throws ToolkitServiceException {
        return engine.retrieve(request);
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
