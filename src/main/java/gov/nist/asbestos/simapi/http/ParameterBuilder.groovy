package gov.nist.asbestos.simapi.http

class ParameterBuilder {
    Map<String, List<String>> parameterMap = [:]

    ParameterBuilder add(String name, String value) {
        List<String> values = parameterMap.get(name)
        if (values) {
            values << value
        } else {
            values = []
            values << value
            parameterMap.put(name, values)
        }

        this
    }
}
