package gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon;

/**
 *
 */
public interface RetrieveRequest extends SimId {
    void setRepositoryUniqueId(String repositoryUniqueId);
    void setDocumentUniqueId(String documentUniqueId);
    void setHomeCommunityId(String homeCommunityId);
    String getRepositoryUniqueId();
    String getDocumentUniqueId();
    String getHomeCommunityId();
}
