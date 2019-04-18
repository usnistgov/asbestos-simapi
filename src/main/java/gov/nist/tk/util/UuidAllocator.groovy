package gov.nist.tk.util;


import gov.nist.tk.stubs.UUIDFactory
import groovy.transform.TypeChecked;

import java.util.UUID;

@TypeChecked
class UuidAllocator {
    static UUIDFactory fact = null;

    static public String allocate() {
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
