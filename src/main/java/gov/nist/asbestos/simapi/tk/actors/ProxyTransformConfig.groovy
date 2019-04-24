package gov.nist.asbestos.simapi.tk.actors

import com.google.gwt.user.client.rpc.IsSerializable
import groovy.transform.TypeChecked

@TypeChecked
 class ProxyTransformConfig implements IsSerializable, Serializable {
    private TransactionType transactionType;
    private TransactionDirection transactionDirection;
    private String transformClassName;
    private gov.nist.asbestos.simapi.toolkit.configDatatypes.client.FhirVerb fhirVerb;

     ProxyTransformConfig(TransactionType transactionType, TransactionDirection transactionDirection, gov.nist.asbestos.simapi.toolkit.configDatatypes.client.FhirVerb fhirVerb, String transformClassName) {
        this.transactionType = transactionType;
        this.transactionDirection = transactionDirection;
        this.fhirVerb = fhirVerb;
        this.transformClassName = transformClassName;
    }

     TransactionType getTransactionType() {
        return transactionType;
    }

     gov.nist.asbestos.simapi.toolkit.configDatatypes.client.FhirVerb getFhirVerb() {
        return fhirVerb;
    }

     TransactionDirection getTransactionDirection() {
        return transactionDirection;
    }

     String getTransformClassName() {
        return transformClassName;
    }

     static ProxyTransformConfig parse(String encoded) throws Exception {
        String[] parts = encoded.split("\\^", 4);
        if (parts.length != 4)
            throw new Exception("ProxyTransformConfig: bad configuration: " + encoded);
        TransactionType ttype = TransactionType.find(parts[0]);
        TransactionDirection dir;
        if (parts[1].equals(TransactionDirection.REQUEST.name()))
            dir = TransactionDirection.REQUEST;
        else if (parts[1].equals(TransactionDirection.RESPONSE.name()))
            dir = TransactionDirection.RESPONSE;
        else
            throw new Exception("ProxyTransformConfig: bad TransactionDirection: " + parts[1]);
        return new ProxyTransformConfig(ttype, dir, gov.nist.asbestos.simapi.toolkit.configDatatypes.client.FhirVerb.valueOf(parts[2]), parts[3]);
    }

     String toString() {
        return transactionType.getShortName() + "^" + transactionDirection.name() + "^" + fhirVerb.name() + "^" + transformClassName;
    }
}
