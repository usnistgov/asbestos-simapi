package gov.nist.tk.actors


import gov.nist.toolkit.configDatatypes.client.FhirVerb
import groovy.json.JsonSlurper
import groovy.transform.TypeChecked

/**
 *
 */
@TypeChecked
class TransactionType {
    String id = "";
    String name = "";
	 String shortName = "";
    String code = "";   // like pr.b - used in actors table
    String asyncCode = "";
    boolean needsRepUid = false;  // I think maybe not used? RM
    String requestAction = "";
    String responseAction = "";
    boolean requiresMtom = false;
    boolean http = false; // Is this Http only (non-SOAP) transaction
//    Map<String, TransactionType> basicTypeMap = new HashMap<>();
    boolean fhir = false;
    String endpointSimPropertyName;  // TODO is this irrelevant?
    String tlsEndpointSimPropertyName;  // TODO is this irrelevant?
    FhirVerb fhirVerb = FhirVerb.NONE;

    Map asMap() {
        def x = [:]
        x.id = id
        x.name = name
        x.shortName = shortName
        x.code = code
        x.asyncCode = asyncCode
        x.needsRepUid = needsRepUid
        x.requestAction = requestAction
        x.responseAction = responseAction
        x.requiresMtom = requiresMtom
        x.http = http
        x.fhir = fhir
        x.endpointSimPropertyName = endpointSimPropertyName
        x.tlsEndpointSimPropertyName = tlsEndpointSimPropertyName
        x.fhirVerb = fhirVerb
        x
    }

    static List<TransactionType> types = []

    static init(File ec) {
        def jsonSlurper = new JsonSlurper()
        File typesDir = new File(new File(ec, 'types'), 'transactions')
        typesDir.listFiles().each { File file ->
            if (!file.name.endsWith('json')) return
            TransactionType type = jsonSlurper.parse(file) as TransactionType
            types << type
        }
    }

   @Override
    String toString() { return shortName; }

    boolean isFhir() { return fhir;  }

     boolean isRequiresMtom() {
        return requiresMtom;
    }

     String getId() {
        return id;
    }

     String getName() {
        return name;
    }

	 String getShortName() {
        return shortName;
    }

	 String getCode() {
        return code;
    }

	 String getAsyncCode() {
        return asyncCode;
    }

     boolean usesTraditionalTransactions() {
        if (requestAction.equals("")) return false;
        return true;
    }

     FhirVerb getFhirVerb() {
        return fhirVerb;
    }

    /**
    * @return the {@link #requestAction} value.
    */
    String getRequestAction() {
      return requestAction;
   }

   /**
    * @return the {@link #responseAction} value.
    */
    String getResponseAction() {
      return responseAction;
   }

    boolean isHttpOnly() {
      return http;
   }

    boolean equals(TransactionType tt) {
        return name.equals(tt.name);
	}

    // if lookup by id is needed, must also select off of receiving actor
    static  TransactionType find(String s) {
        if (s == null) return null;
        for (TransactionType t : types) {
            if (s.equals(t.name)) return t;
            if (s.equals(t.shortName)) return t;
            if (s.equals(t.code)) return t;
            if (s.equals(t.asyncCode)) return t;
            if (s.equals(t.getId())) return t;
        }
        return null;
    }

    static  TransactionType find(String s, FhirVerb fhirVerb) {
        if (s == null) return null;
        for (TransactionType t : types) {
            if (s.equals(t.name) && t.fhirVerb == fhirVerb) return t;
            if (s.equals(t.shortName) && t.fhirVerb == fhirVerb) return t;
            if (s.equals(t.code) && t.fhirVerb == fhirVerb) return t;
            if (s.equals(t.asyncCode) && t.fhirVerb == fhirVerb) return t;
            if (s.equals(t.getId()) && t.fhirVerb == fhirVerb) return t;
        }
        return null;
    }

     boolean isIdentifiedBy(String s) {
        if (s == null) return false;
        return
				s.equals(id) ||
						s.equals(name) ||
						s.equals(shortName) ||
						s.equals(code) ||
						s.equals(asyncCode);
    }

	static  TransactionType findByRequestAction(String action) {
		if (action == null) return null;
		for (TransactionType t : types) {
			if (action.equals(t.requestAction)) return t;
		}
		return null;
	}

	static  TransactionType findByResponseAction(String action) {
		if (action == null) return null;
		for (TransactionType t : types) {
			if (action.equals(t.responseAction)) return t;
		}
		return null;
	}

	static  List<TransactionType> asList() {
        List<TransactionType> l = new ArrayList<TransactionType>();
        for (TransactionType t : types)
            l.add(t);
        return l;
    }

     String getEndpointSimPropertyName() {
        return endpointSimPropertyName;
    }

     String getTlsEndpointSimPropertyName() {
        return tlsEndpointSimPropertyName;
    }
}
