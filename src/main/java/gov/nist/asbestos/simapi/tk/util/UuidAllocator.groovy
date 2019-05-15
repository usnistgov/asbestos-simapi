package gov.nist.asbestos.simapi.tk.util;


import gov.nist.asbestos.simapi.tk.stubs.UUIDFactory
import groovy.transform.TypeChecked

@TypeChecked
class UuidAllocator {
    static UUIDFactory fact = null;

    static String allocate() {
        if (fact == null)
            fact = UUIDFactory.getInstance();
        UUID uu = fact.newUUID();
        return "urn:uuid:" + uu;
    }

//    static public String allocateNaked() {
//        if (fact == null)
//            fact = UUIDFactory.getInstance();
//        UUID uu = fact.newUUID();
//        return uu.toString();
//    }

}
