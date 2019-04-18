package gov.nist.tk.stubs

class UUIDFactory {
    static UUIDFactory getInstance() { return new UUIDFactory() }

    UUID newUUID() {
        return UUID.randomUUID()
    }
}
