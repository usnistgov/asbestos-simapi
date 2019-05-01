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

    String toString() {
        StringBuilder buf = new StringBuilder()

        if (verb && pathInfo)
            buf.append(verb).append(' ').append(pathInfo).append('\r\n')
        Map<String, String> hdrs = [:]
        nameValueList.each { NameValue nv ->
            if (hdrs.containsKey(nv.name)) {
                hdrs.put(nv.name, hdrs.get(nv.name) + "; ${nv.value}")
            } else {
                hdrs.put(nv.name, nv.value)
            }
        }
        hdrs.each { String name, String value ->
            buf.append("${name}: ${value}\r\n")
        }

        buf
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Headers headers = (Headers) o

        if (nameValueList != headers.nameValueList) return false
        if (pathInfo != headers.pathInfo) return false
        if (verb != headers.verb) return false

        return true
    }

    int hashCode() {
        int result
        result = (verb != null ? verb.hashCode() : 0)
        result = 31 * result + (pathInfo != null ? pathInfo.hashCode() : 0)
        result = 31 * result + (nameValueList != null ? nameValueList.hashCode() : 0)
        return result
    }
}
