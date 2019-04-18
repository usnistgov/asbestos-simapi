package gov.nist.toolkit.toolkitApi;

import gov.nist.toolkit.toolkitServicesCommon.LeafClassList
import groovy.transform.TypeChecked;

/**
 *
 */
@TypeChecked
 interface RespondingGateway extends AbstractActorInterface {
    LeafClassList FindDocuments(String patientId) throws ToolkitServiceException;
}
