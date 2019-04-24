package gov.nist.asbestos.simapi.toolkit.toolkitApi

import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.resource.xdm.XdmReport
import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.resource.xdm.XdmRequest
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
