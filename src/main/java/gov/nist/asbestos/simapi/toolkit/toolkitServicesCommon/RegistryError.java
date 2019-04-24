package gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon;

/**
 *
 */
public interface RegistryError {
    String getErrorCode();

    String getErrorContext();

    String getLocation();

    ResponseStatusType getStatus();
}
