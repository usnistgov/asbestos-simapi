/**
 *
 */
package gov.nist.toolkit.toolkitApi

import gov.nist.toolkit.configDatatypes.client.PatientErrorMap
import gov.nist.toolkit.toolkitServicesCommon.DcmImageSet
import groovy.transform.TypeChecked

/**
 * Implementation class for XDSI Image Document Source
 */
@TypeChecked
 class XdsiImagingDocumentSource extends AbstractActor implements ImagingDocumentSource {

   /* (non-Javadoc)
    * @see gov.nist.toolkit.tookitApi.ImageDocumentSource#retrieveImagingDocumentSet(gov.nist.toolkit.toolkitServicesCommon.DcmImageSet)
    */
   @Override
    DcmImageSet retrieveImagingDocumentSet(DcmImageSet request) {
      // TODO Auto-generated method stub
      return null;
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
