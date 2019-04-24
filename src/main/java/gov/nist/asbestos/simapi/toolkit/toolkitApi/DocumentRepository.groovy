package gov.nist.asbestos.simapi.toolkit.toolkitApi;

import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.DocumentContent
import groovy.transform.TypeChecked;

/**
 *
 */
@TypeChecked
 interface DocumentRepository extends AbstractActorInterface {


    /**
     * Get contents of a document
     * @param uniqueId - DocumentEntry.uniqueId
     * @return contents of the document
     * @throws ToolkitServiceException
     */
    DocumentContent getDocument(String uniqueId) throws ToolkitServiceException;
}
