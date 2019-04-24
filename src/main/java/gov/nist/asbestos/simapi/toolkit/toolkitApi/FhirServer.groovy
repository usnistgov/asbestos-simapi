package gov.nist.asbestos.simapi.toolkit.toolkitApi

import groovy.transform.TypeChecked;

/**
 *
 */
@TypeChecked
 class FhirServer extends AbstractActor implements IFhirServer {
    @Override
     boolean isFhir() {
        return true;
    }

}
