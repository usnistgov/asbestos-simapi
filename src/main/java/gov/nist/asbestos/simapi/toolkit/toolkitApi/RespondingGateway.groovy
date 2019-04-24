package gov.nist.asbestos.simapi.toolkit.toolkitApi;

import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.LeafClassList
import groovy.transform.TypeChecked;

/**
 *
 */
@TypeChecked
 interface RespondingGateway extends AbstractActorInterface {
    LeafClassList FindDocuments(String patientId) throws ToolkitServiceException;
}
