package gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon.resource.xdm;

import java.util.List;

/**
 *
 */
public interface XdmReport {
    boolean isPass();
    String getReport();
    List<XdmItem> getItems();
}
