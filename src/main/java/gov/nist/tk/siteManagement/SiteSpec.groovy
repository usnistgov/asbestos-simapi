package gov.nist.tk.siteManagement

import gov.nist.tk.actors.ActorType
import gov.nist.tk.simCommon.TestSession
import groovy.transform.TypeChecked


/**
 * A Site is the collection of endpoints and parameters for a single site or as Gazelle calls it a system.
 * A Site references multiple actor types but it can hold only one copy of an actor type:
 * one Registry, one Repository etc.
 * A SiteSpec is a reference to a Site and a selection of one actor type. Having a SiteSpec you know
 * exactly which transactions are possible. The actorType parameter is the actor type of interest (personality
 * to be used in an operation) and name is the site name.
 *
 * SiteSpec reference the Site through the name attribute.
 * @author bill
 *
 */
@TypeChecked
 class SiteSpec implements Serializable {

	 String name = "";   // site name
	// For tests that depend on Orchestration, we sometimes need to configure supporting actors into the
	// Site. To do this and not alter the Vendor configured Site, a Orchestration Site is created with the following
	// rules.
	//   1. name refers to vendor site
	//   2. orchestrationSiteName refers to orchestration site
	//   3. When searching for endpoint or other facet, look in orchestration site first, vendor site second
	 String orchestrationSiteName = null;
	 ActorType actorType = null;
	 String homeId = "";
	 String homeName = "";
	 boolean isTls = false;
	 boolean isSaml = false;
	String gazelleXuaUsername;
	String stsAssertion;
	 boolean isAsync = false;
	 TestSession testSession;

    /**
     * Create a site spec. This is a data transfer model (DTO) used to manage Sites in the UI.
     * @param name name of the site
     * @param actorType actor type of interest within the site
     * @param toClone if set it is another SiteSpec to get the TLS, SAML, and ASYNC settings from.  If this
     *                parameter is null then default values are used.
     */
	 SiteSpec(String name, ActorType actorType, SiteSpec toClone, TestSession testSession) {
		this.name = name;
		this.actorType = actorType;
		this.testSession = testSession;

		if (toClone == null) {
			isTls = false;
			isSaml = false;
			isAsync = false;
		} else {
			isTls = toClone.isTls;
			isSaml = toClone.isSaml;
			isAsync = toClone.isAsync;
			this.testSession = toClone.testSession;
			this.setGazelleXuaUsername(toClone.getGazelleXuaUsername());
			this.setStsAssertion(toClone.getStsAssertion());
		}
	}

	 SiteSpec() {
	}

	 SiteSpec(String name, TestSession testSession) {
        this(name, null, null, testSession);
    }

	 SiteSpec(TestSession testSession) {
		this("", null, null, testSession);
	}

	 boolean isNullSite() { return name.equals(""); }

	 String getTypeName() {
		if (actorType == null) return null;
		return actorType.getShortName();
	}

	 String getName() {
		return name;
	}

	 ActorType getActorType() {
		return actorType;
	}

	 boolean isTls() {
		return isTls;
	}

	 boolean isSaml() {
		return isSaml;
	}

	 void setTls(boolean isTls) {
		this.isTls = isTls;
	}

	 void setSaml(boolean isSaml) {
		this.isSaml = isSaml;
	}

	 void setName(String name) {
		this.name = name;
	}

	 void setActorType(ActorType actorType) {
		this.actorType = actorType;
	}

	 String getOrchestrationSiteName() {
		return orchestrationSiteName;
	}

	 void setOrchestrationSiteName(String orchestrationSiteName) {
		this.orchestrationSiteName = orchestrationSiteName;
	}

	 String getGazelleXuaUsername() {
		return gazelleXuaUsername;
	}

	 void setGazelleXuaUsername(String gazelleXuaUsername) {
		this.gazelleXuaUsername = gazelleXuaUsername;
	}

	 String getStsAssertion() {
		return stsAssertion;
	}

	 void setStsAssertion(String stsAssertion) {
		this.stsAssertion = stsAssertion;
	}

	 void validate() {
		 assert name : "SiteSpec does not validate - no name - " + toString()
		 assert testSession : "SiteSpec does not validate - no TestSession - " + toString()
	}

	@Override
	 String toString() {
		return testSession.getValue() + "/" + name;
	}

	 SiteSpec copy() {
		SiteSpec siteSpec = new SiteSpec();
		siteSpec.setSaml(isSaml);
		siteSpec.setStsAssertion(stsAssertion);
		siteSpec.setGazelleXuaUsername(getGazelleXuaUsername());
		siteSpec.setName(name);
		siteSpec.setActorType(actorType);
		siteSpec.testSession = new TestSession(testSession.toString());
		return siteSpec;
	}

}
