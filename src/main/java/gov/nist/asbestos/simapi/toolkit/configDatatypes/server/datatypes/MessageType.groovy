package gov.nist.asbestos.simapi.toolkit.configDatatypes.server.datatypes
/**
 *
 */
class MessageType {
    MessageTechnology messageTechnology
    String soapAction

    MessageType(MessageTechnology messageTechnology1, String soapAction) {
        this.messageTechnology = messageTechnology1
        this.soapAction = soapAction
    }
}
