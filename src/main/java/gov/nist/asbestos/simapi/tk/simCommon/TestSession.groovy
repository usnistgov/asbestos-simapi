package gov.nist.asbestos.simapi.tk.simCommon


import groovy.transform.TypeChecked

@TypeChecked
class TestSession implements Serializable {
    private static final long serialVersionUID = 1L;

    private String value;
    public transient static final TestSession DEFAULT_TEST_SESSION = new TestSession("default");
    public transient static final TestSession GAZELLE_TEST_SESSION = new TestSession("gazelle");
    public transient static final TestSession CAT_TEST_SESSION = new TestSession("cat");

    private TestSession() {}

    TestSession(String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }

    @Override
    String toString() {
        return value;
    }

    void clean() { value = value.replaceAll("\\.", "_").toLowerCase(); }

    @Override
    boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        TestSession that = (TestSession) o;

        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    int hashCode() {
        return value != null ? value.hashCode() : 0;
    }
}
