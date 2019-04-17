package gov.nist.tk.simCommon


import groovy.transform.TypeChecked

/**
 * Created by bill on 9/10/15.
 */
@TypeChecked
enum ParamType implements Serializable {
    OID,
    ENDPOINT,
    TEXT,
    BOOLEAN,
    TIME,
    SELECTION,
    LIST;

	ParamType() {
	} // for GWT
}
