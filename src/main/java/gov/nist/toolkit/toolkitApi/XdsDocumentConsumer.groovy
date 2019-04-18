package gov.nist.toolkit.toolkitApi

import gov.nist.toolkit.configDatatypes.client.PatientErrorMap
import gov.nist.toolkit.toolkitServicesCommon.LeafClassRegistryResponse
import gov.nist.toolkit.toolkitServicesCommon.RetrieveRequest
import gov.nist.toolkit.toolkitServicesCommon.RetrieveResponse
import gov.nist.toolkit.toolkitServicesCommon.StoredQueryRequest
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
    * @see gov.nist.toolkit.toolkitServicesCommon.SimConfig#setPatientErrorMap(gov.nist.toolkit.configDatatypes.client.PatientErrorMap)
    */
   @Override
    void setPatientErrorMap(PatientErrorMap errorMap) throws IOException {
      // TODO Auto-generated method stub

   }

   /* (non-Javadoc)
    * @see gov.nist.toolkit.toolkitServicesCommon.SimConfig#getPatientErrorMap()
    */
   @Override
    PatientErrorMap getPatientErrorMap() throws IOException {
      // TODO Auto-generated method stub
      return null;
   }
}
