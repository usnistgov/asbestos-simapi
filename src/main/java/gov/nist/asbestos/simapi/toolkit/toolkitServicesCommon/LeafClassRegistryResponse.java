package gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon;

/**
 *
 */
public interface LeafClassRegistryResponse extends RegistryResponse, LeafClassList {
    ResponseStatusType getStatus();
}
