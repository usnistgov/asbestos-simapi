package gov.nist.asbestos.simapi.sim.headers

import groovy.transform.TypeChecked

@TypeChecked
class Headers {
    String verb = null
    String pathInfo = null
    int status = 0
    List<NameValue> nameValueList = []

    String getContentType() {
        NameValue nv = nameValueList.find { NameValue nv -> nv.name.equalsIgnoreCase('content-type')}
        if (!nv)
            return ''
        nv.value
    }

    String getAccept() {
        NameValue nv = nameValueList.find { NameValue nv -> nv.name.equalsIgnoreCase('accept')}
        if (!nv)
            return ''
        String value = nv.value
        if (value.contains(';'))
            value = value.split(';', 2)[0].trim()
        value
    }

    String getContentEncoding() {
        NameValue nv = nameValueList.find { NameValue nv -> nv.name.equalsIgnoreCase('content-encoding')}
        if (!nv)
            return ''
        String value = nv.value
        if (value.contains(';'))
            value = value.split(';', 2)[0].trim()
        value
    }

    String getAll(String type) {
        Collection<NameValue> nvs = nameValueList.findAll { NameValue nv -> nv.name.equalsIgnoreCase(type)}
        if (!nvs)
            return ''
        List<String> values = nvs.collect { NameValue nv -> nv.value }
        values.join(';')
    }

    // this is for collecting headers accept* for example
    Map<String, String> getMultiple(String namePrefix) {
        Map<String, String> result = [:]

        nameValueList.each { NameValue nv ->
            String name = nv.name
            if (name.startsWith(namePrefix) && !result.keySet().contains(name)) {
                result.put(name, getAll(name))
            }
        }

        return result
    }

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
        if (status != 0)
            buf.append("1.1 ${status} unknown")
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
