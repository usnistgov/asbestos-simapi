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
 interface DocumentConsumer extends AbstractActorInterface {

    LeafClassRegistryResponse queryForLeafClass(StoredQueryRequest request) throws ToolkitServiceException;
//    RefList queryForObjectRef(String queryId, Map<String, List<String>> parameters);
    RetrieveResponse retrieve(RetrieveRequest request) throws ToolkitServiceException;
}
