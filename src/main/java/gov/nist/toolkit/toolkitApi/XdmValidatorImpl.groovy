package gov.nist.toolkit.toolkitApi

import gov.nist.toolkit.toolkitServicesCommon.resource.xdm.XdmReport
import gov.nist.toolkit.toolkitServicesCommon.resource.xdm.XdmRequest
import groovy.transform.TypeChecked

/**
 *
 */
@TypeChecked
 class XdmValidatorImpl extends AbstractActor implements XdmValidator {
    @Override
     XdmReport validate(XdmRequest request) throws ToolkitServiceException {
        return engine.validateXDM(request);
    }
}
