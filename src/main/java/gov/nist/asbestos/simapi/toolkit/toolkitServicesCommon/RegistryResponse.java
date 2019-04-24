package gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon;

import java.util.List;

/**
 *
 */
public interface RegistryResponse {
    ResponseStatusType getStatus();
    List<RegistryError> getErrorList();
}
