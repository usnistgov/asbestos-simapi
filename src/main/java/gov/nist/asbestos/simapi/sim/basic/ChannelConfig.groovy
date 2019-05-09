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
}
