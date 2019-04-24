package gov.nist.asbestos.simapi.toolkit.toolkitApi;

import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.resource.xdm.XdmReport;
import gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.resource.xdm.XdmRequest;

/**
 *
 */
public interface XdmValidator {
    XdmReport validate(XdmRequest request) throws ToolkitServiceException;
}
