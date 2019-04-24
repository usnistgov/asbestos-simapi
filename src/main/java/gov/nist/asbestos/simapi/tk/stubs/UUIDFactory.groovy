package gov.nist.asbestos.simapi.tk.stubs

class UUIDFactory {
    static UUIDFactory getInstance() { return new UUIDFactory() }

    UUID newUUID() {
        return UUID.randomUUID()
    }
}
