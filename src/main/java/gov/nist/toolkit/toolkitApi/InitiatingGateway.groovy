package gov.nist.toolkit.toolkitApi;

import gov.nist.toolkit.toolkitServicesCommon.RefList
import groovy.transform.TypeChecked;

/**
 *
 */
@TypeChecked
 interface InitiatingGateway extends AbstractActorInterface {
    RefList FindDocuments(String patientId) throws ToolkitServiceException;
}
