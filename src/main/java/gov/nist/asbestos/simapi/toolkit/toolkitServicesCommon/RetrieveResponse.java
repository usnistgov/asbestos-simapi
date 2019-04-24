package gov.nist.asbestos.simapi.toolkit.toolkitServicesCommon;

/**
 *
 */
public interface RetrieveResponse {
   String getDocumentUid();
   String getRepositoryUid();
   String getHomeCommunityUid();
    String getMimeType();
    byte[] getDocumentContents();
}
