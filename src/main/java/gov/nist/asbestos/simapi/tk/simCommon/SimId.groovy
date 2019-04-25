package gov.nist.asbestos.simapi.tk.simCommon

import com.fasterxml.jackson.annotation.JsonAutoDetect
import gov.nist.asbestos.simapi.tk.siteManagement.SiteSpec
import groovy.transform.TypeChecked

/**
 *
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.NONE)
@TypeChecked
 class SimId implements Serializable {

    private static final String SEPARATOR = "__";
    private static final String SLASH = "/";

    private TestSession testSession = null;
    private String id = null;
    private String actorType = null;
    private String environmentName = null;
    private boolean fhir = false;

    // server only
     SimId(TestSession testSession, String id, String actorType, String environmentName, boolean fhir)  {
        this(testSession, id, actorType);
        this.environmentName = environmentName;
        this.fhir = fhir;
    }

    // server only
     SimId(TestSession testSession, String id, String actorType, String environmentName) {
        this(testSession, id, actorType);
        this.environmentName = environmentName;
    }

    // client only
     SimId(TestSession testSession, String id, String actorType)  {
        this(testSession, id);
        this.actorType = actorType;
    }

     SimId(SiteSpec siteSpec, TestSession testSession) {
        this(testSession, (siteSpec == null) ? null : siteSpec.getName());
        if (siteSpec != null && siteSpec.getActorType() != null)
            actorType = siteSpec.getTypeName();
    }

    // client and server
     SimId(TestSession testSession, String id) {
         assert testSession : "SimId - TestSession is null"

        build(testSession, id);
    }

     SimId() {}

     SimId forFhir() {
        fhir = true;
        return this;
    }

     boolean isFhir() { return fhir; }

    private void build(TestSession testSession, String id) {
        testSession.clean()
        assert !testSession.getValue().contains(SEPARATOR) : SEPARATOR + " is illegal in simulator testSession name"
        assert !testSession.getValue().contains(SLASH) : SLASH + " is illegal in simulator testSession name"

        id = cleanId(id);
        assert !id.contains(SLASH) : SLASH + " is illegal in simulator id"

        this.testSession = testSession;
        this.id = id;
    }

    // equals and hashCode ignore FHIR status on purpose
    @Override
     boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimId simId = (SimId) o;

        if (testSession != null ? !testSession.equals(simId.testSession) : simId.testSession != null) return false;
        return id != null ? id.equals(simId.id) : simId.id == null;
    }

    @Override
     int hashCode() {
        int result = testSession != null ? testSession.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

     String toString() { return testSession.toString() + SEPARATOR + id; }

     String validateState() {
        StringBuilder buf = new StringBuilder();

        if (testSession == null || testSession.equals("")) buf.append("No testSession specified\n");
        if (id == null || id.equals("")) buf.append("No id specified\n");
        if (actorType == null || actorType.equals("")) buf.append("No actorType specified\n");
        if (environmentName == null || environmentName.equals("")) buf.append("No environmentName specified");

        if (buf.length() == 0) return null;   // no errors
        return buf.toString();
    }

    String cleanId(String id) { return id.replaceAll("\\.", "_").toLowerCase(); }

     String getActorType() {
        return actorType;
    }

     void setActorType(String actorType) {
        this.actorType = actorType;
    }

     String getEnvironmentName() {
        return environmentName;
    }

     void setEnvironmentName(String environmentName) {
        this.environmentName = environmentName;
    }

     TestSession getTestSession() {
        return testSession;
    }

     String getId() {
        return id;
    }

     boolean isTestSession(TestSession testSession) {
        return testSession != null && testSession.equals(this.testSession);
    }
     boolean isValid() { return (!isEmpty(testSession.getValue())) && (!isEmpty(id)); }
     void setValid(boolean x) { }
    boolean isEmpty(String x) { return x == null || x.trim().equals(""); }

     SiteSpec getSiteSpec() {
        SiteSpec siteSpec = new SiteSpec(testSession);
        siteSpec.setName(toString());
        if (actorType != null)
            siteSpec.setActorType(gov.nist.asbestos.simapi.tk.actors.ActorType.findActor(actorType));
        return siteSpec;
    }

}