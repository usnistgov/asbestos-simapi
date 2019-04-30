package gov.nist.asbestos.simapi.sim.headers

import groovy.transform.TypeChecked

@TypeChecked
class Headers {
    String verb
    String pathInfo
    List<NameValue> nameValueList = []

    Headers withVerb(String verb) {
        this.verb = verb
        this
    }

    Headers withPathInfo(String pathInfo) {
        this.pathInfo = pathInfo
        this
    }
}
