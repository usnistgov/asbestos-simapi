package gov.nist.asbestos.simapi.http.headers

import groovy.transform.TypeChecked

@TypeChecked
class NameValue {
    String name
    String value

    String toString() {
        "${name}: ${value}"
    }

    String getName() {
        if (!name) name = ''
        name
    }
}
