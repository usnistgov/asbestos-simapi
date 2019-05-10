package gov.nist.asbestos.simapi.sim.basic

import groovy.transform.TypeChecked

@TypeChecked
class ChannelConfig {
    String environment
    String testSession
    String channelId
    String actorType
    String channelType
    String fhirBase
    Map extensions

    // TODO test needed
    String translateEndpointToFhirBase(String endpoint) {
        int channelI = endpoint.indexOf('/Channel')
        if (channelI != -1) {
            int beyondChannelI = channelI + '/Channel'.size()
            endpoint = "${fhirBase}${endpoint.substring(beyondChannelI)}"
        }
        endpoint
    }
}
