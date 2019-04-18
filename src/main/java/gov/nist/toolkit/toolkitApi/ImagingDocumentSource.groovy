/**
 *
 */
package gov.nist.toolkit.toolkitApi;

import gov.nist.toolkit.toolkitServicesCommon.DcmImageSet
import groovy.transform.TypeChecked;

/**
 * XDSI Image Document Source API
 */
@TypeChecked
 interface ImagingDocumentSource extends AbstractActorInterface {

   DcmImageSet retrieveImagingDocumentSet(DcmImageSet request);
}
