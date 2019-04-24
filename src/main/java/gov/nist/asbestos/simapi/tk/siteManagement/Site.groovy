package gov.nist.asbestos.simapi.tk.siteManagement


import groovy.transform.TypeChecked;


/**
 * A Site is the collection of endpoints and parameters for a single site or as Gazelle calls it a system.
 * A Site references multiple actor types but it can hold only one copy of an actor type:
 * one Registry, one Repository etc.
 * A SiteSpec is a reference to a Site and a selection of one actor type. Having a SiteSpec you know
 * exactly which transactions are possible.
 *
 * Usage for Orchestration
 *
 * From the perspective of SiteSpec:
 * 	For tests that depend on Orchestration, we sometimes need to configure supporting actors into the
 * Site. To do this and not alter the Vendor configured Site, a Orchestration Site is created with the following
 * rules.
 *   1. name refers to vendor site
 *   2. orchestrationSiteName refers to orchestration site
 *   3. When searching for endpoint or other facet, look in orchestration site first, vendor site second
 *
 * When a SiteSpec gets translated into a Site:
 *
 * 1. Orchestration Site has non-null mainSite attribute naming the Vendor Site
 * 2. Searches for things like endpoints start with Orchestration Site and if not found proceed to search
 * Vendor Site.
 *
 * The class Sites is used internally to look up the Vendor Site from the Orchestration Site (by name)
 *
 *
 * SiteSpec reference the Site through the name attribute.
 * @author bill
 *
 */

// Transaction names are listed in TransactionCollection.groovy
@TypeChecked
class Site implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name = null;
	TransactionCollection transactions = new TransactionCollection(false);
	// There can be only one ODDS, one XDS.b, and one IDS repository in a site.
	// An XDS.b Repository and a ODDS Repository
	// can have the same repositoryUniqueId and endpoint. But
	// they require two entries to identify them.
	TransactionCollection repositories = new TransactionCollection(true);
	String home = null;
	String pifHost = null;
	String pifPort = null;
	private String owner = null;

	String pidAllocateURI = null;
	transient  boolean changed = false;
	private gov.nist.asbestos.simapi.tk.simCommon.TestSession testSession = null;  // required to be valid
	private String orchestrationSiteName = null;
	private boolean isASimulator = false;

	Site() {
	}

	/**
	 * Site linkage is used to combine two sites into one.  Use case: The SUT is defined in a site. We
	 * need to add other actors to the mix through orchestration.  Because the vendor controls the SUT site,
	 * we create a separate site for the orchestration actors. We then link in the SUT site into
	 * the orchestration site to have a single site to target.
	 * @param linkedSite
	 */
	void addLinkedSite(Site linkedSite) {
		transactions.mergeIn(linkedSite.transactions());
		repositories.mergeIn(linkedSite.repositories());
		if (home == null)
			home = linkedSite.home;
		if (pifHost == null)
			pifHost = linkedSite.pifHost;
		if (pifPort == null)
			pifPort = linkedSite.pifPort;
	}

	@Override
	int hashCode() {
		return 41 + ((name == null) ? 0 : name.hashCode());
	}

	@Override
	boolean equals(Object o) {
		if (o == null) return false;
		if (!(o instanceof Site)) return false;
		Site s = (Site) o;
		boolean b =
				((name == null) ? s.name == null : name == s.name) &&
						((testSession == null) ? s.testSession == null : testSession == s.testSession) &&
						((home == null) ? s.home == null : home == s.home) &&
						((pifHost == null) ? s.pifHost == null : pifHost == s.pifHost) &&
						((pifPort == null) ? s.pifPort == null : pifPort == s.pifPort) &&
						((pidAllocateURI == null) ? s.pidAllocateURI == null : pidAllocateURI == s.pidAllocateURI) &&
						transactions == s.transactions &&
						repositories == s.repositories
		b
	}

	boolean isValid() {
		return name != null && !name.equals("") && testSession != null;
	}

	void validate() {
		assert isValid() : "Site " + toString() + " is not valid"
	}

	TransactionCollection transactions() {
		return transactions;
	}

	TransactionCollection repositories() {
		return repositories;
	}

	boolean verify() {
		StringBuffer buf = new StringBuffer();
		validate(buf);
		return buf.length() == 0;
	}

	void validate(StringBuffer buf) {

		if (name.contains("__"))
			buf.append("Site name cannot contain double underscore (__)\n");

		for (TransactionBean b : transactions.transactions) {
			for (TransactionBean c : transactions.transactions) {
				if (b == c)
					continue;
				if (b.hasSameIndex(c) && !b.equals(c)) {
					buf.append("Site ").append(name).append(" has a conflict:\n");
					buf.append("\tThese entries conflict\n");
					buf.append("\t\t").append(b).append("\n");
					buf.append("\t\t").append(c).append("\n");
				}
			}
		}

		for (TransactionBean b : repositories.transactions) {
			if (gov.nist.asbestos.simapi.tk.actors.ActorType.findActor('rep') == b.actorType)
				for (TransactionBean c : repositories.transactions) {
					if (b == c)
						continue;
					if (b.hasSameIndex(c) && !b.equals(c)) {
						buf.append("Site ").append(name).append(" has a conflict:\n");
						buf.append("\tThese entries conflict\n");
						buf.append("\t\t").append(b).append("\n");
						buf.append("\t\t").append(c).append("\n");
					}
				}
		}

		// All Repository transactions must be for the same repositoryUniqueId
		Set<String> repUids = repositoryUniqueIds();
		if (repUids.size() > 1) {
			buf.append("Site ").append(name).append(" contains more than one repositoryUniqueId: " + repUids)
					.append("  A site can define a single Document Repository.");
		}
	}



	void cleanup() {
		repositories.removeEmptyNames();
		transactions.fixTlsEndpoints();
		repositories.fixTlsEndpoints();
	}

	int size() {
		return transactions.size() + repositories.size();
	}

	void addTransaction(String transactionName, String endpoint, boolean isSecure, boolean isAsync) {
		addTransaction(new TransactionBean(transactionName, TransactionBean.RepositoryType.NONE, endpoint, isSecure, isAsync));
	}

	void addTransaction(TransactionBean transbean) {
		transactions.addTransaction(transbean);
	}

	String getPidAllocateURI() {
		return pidAllocateURI;
	}

	boolean hasActor(gov.nist.asbestos.simapi.tk.actors.ActorType actorType) {
		return transactions.hasActor(actorType);
	}

	/**
	 * Get Repository Bean
	 * @param repositoryType
	 * @param isSecure
	 * @return TransactionBean
	 */
	TransactionBean getRepositoryBean(TransactionBean.RepositoryType repositoryType, boolean isSecure) {
		for (TransactionBean b : repositories.transactions) {
			if (b.repositoryType == repositoryType && b.isSecure == isSecure)
				return b;
		}
		return null;
	}

	void addRepository(TransactionBean transbean) {
		repositories.addTransaction(transbean);
	}

	void addRepository(String repositoryUniqueId, TransactionBean.RepositoryType repositoryType, String endpoint, boolean isSecure, boolean isAsync) {
		TransactionBean bean = new TransactionBean(repositoryUniqueId, repositoryType, endpoint, isSecure, isAsync);
		addRepository(bean);
	}

	String getRepositoryUniqueId(TransactionBean.RepositoryType repositoryType) throws Exception {
		if (!hasRepositoryB(repositoryType))
			throw new Exception("Site " + name + " does not define an XDS.b Repository");
		TransactionBean transbean;
		transbean = getRepositoryBean(repositoryType, false);  // try non-secure first
		if (transbean != null) {
			String repUid =  transbean.name;
			if (repUid != null && !repUid.equals(""))
				return repUid;
		}
		transbean = getRepositoryBean(repositoryType, true);  // secure next
		if (transbean != null) {
			String repUid =  transbean.name;
			if (repUid != null && !repUid.equals(""))
				return repUid;
		}
		return null;
	}

	boolean isAllRepositories() {
		return (name != null && name.equals("allRepositories"));
	}

	/**
	 * This counts endpoints.  A repository can have endpoints
	 * for all combinations of secure, async, and type.  There are
	 * three basically different repository types, Document
	 * Repository, On-Demand Document Source, and Image Document Source.
	 * @return
	 */
	int repositoryBCount(TransactionBean.RepositoryType repositoryType) {
		int cnt = 0;
		if (name != null && name.equals("allRepositories")) return 0;
		for (TransactionBean b : repositories.transactions) {
//			if (b.repositoryType == RepositoryType.REPOSITORY || b.repositoryType == RepositoryType.ODDS)
			if (b.repositoryType == repositoryType)
				cnt++;
		}
		return cnt;
	}

	Set<String> repositoryUniqueIds() {
		Set<String> ids = new HashSet<String>();
		for (TransactionBean b : repositories.transactions) {
			if (b.repositoryType == TransactionBean.RepositoryType.REPOSITORY || b.repositoryType == TransactionBean.RepositoryType.ODDS) {
				ids.add(b.name); // repositoryUniqueId since this is a retrieve
			}
		}
		return ids;
	}

	List<TransactionBean> transactionBeansForRepositoryUniqueId(String repuid) {
		List<TransactionBean> tbs = new ArrayList<TransactionBean>();
		if (repuid == null || repuid.equals(""))
			return tbs;
		for (TransactionBean b : repositories.transactions) {
			if (repuid.equals(b.name))
				tbs.add(b);
		}
		return tbs;
	}

	/**
	 * Get TransactionBean matching passed RepositoryType and uid.
	 * @param repuid repository unique id
	 * @param tType Repository Type
	 * @return TransactionBean for match, or null
	 */
	TransactionBean transactionBeanForRepositoryUniqueId(String repuid, TransactionBean.RepositoryType tType) {
		for (TransactionBean bean : repositories.transactions) {
			if (bean.repositoryType == tType && bean.name.equals(repuid)) return bean;
		}
		return null;
	}

	boolean hasRepositoryB() {
		return repositoryBCount(TransactionBean.RepositoryType.REPOSITORY) > 0;
	}

	boolean hasRepositoryB(TransactionBean.RepositoryType repositoryType) {
		return repositoryBCount(repositoryType) > 0;
	}

	int getRepositoryCount() {
		return repositories.size();
	}

	String getHome() {
		return home;
	}

	void setHome(String home) {
		this.home = home;
	}

	String getFullName() {
		return testSession.toString() + "/" + name;
	}

	@Override
	String toString() {
		return getFullName();
	}

	String describe() {
		StringBuffer buf = new StringBuffer();
		buf.append(toString()).append("\n");
		buf.append(transactions.describe());
		return buf.toString();
	}

	Site(gov.nist.asbestos.simapi.tk.simCommon.TestSession testSession) {
		this.testSession = testSession;
	}

	Site(String name, gov.nist.asbestos.simapi.tk.simCommon.TestSession testSession) {
		setName(name);
		assert testSession : "Site: null TestSession"
		this.testSession = testSession;
	}

	void setName(String name) {
		assert name : "Site: null name"
		this.name = name;
		transactions.setName(name);
		repositories.setName(name);
	}

	String getName() {
		return name;
	}


	String getEndpoint(gov.nist.asbestos.simapi.tk.actors.TransactionType transaction, boolean isSecure, boolean isAsync) throws Exception {
		String endpoint = getRawEndpoint(transaction, isSecure, isAsync);
		if (endpoint == null)
			throw new Exception("Site#getEndpoint: no endpoint defined for site=" + name + " transaction=" + transaction + " secure=" + isSecure + " async=" + isAsync);
		return endpoint;
	}

	String getRawEndpoint(gov.nist.asbestos.simapi.tk.actors.TransactionType transaction, boolean isSecure,
                          boolean isAsync) {
		return transactions.get(transaction, isSecure, isAsync);
	}

	String getRetrieveEndpoint(String reposUid, boolean isSecure, boolean isAsync) throws Exception {
		if (reposUid == null || reposUid.equals(""))
			throw new Exception("Site#getRetrieveEndpoint: no repository uid specified");
		String endpoint = null;
		endpoint = repositories.get(reposUid, isSecure, isAsync);
		if (endpoint == null)
			throw new Exception("Site#getRetrieveEndpoint: no endpoint defined for repository uid " + reposUid + " and secure=" + isSecure + " and async=" + isAsync);
		return endpoint;
	}

	String getSiteName() {
		return name;
	}

	SiteSpec siteSpec() {
		gov.nist.asbestos.simapi.tk.simCommon.TestSession thisTestSession = (testSession != null) ? testSession : gov.nist.asbestos.simapi.tk.simCommon.TestSession.DEFAULT_TEST_SESSION;
		SiteSpec siteSpec = new SiteSpec(getSiteName(), thisTestSession);
		siteSpec.orchestrationSiteName = orchestrationSiteName;
		return siteSpec;
	}

	String getOrchestrationSiteName() {
		return orchestrationSiteName;
	}

	void setOrchestrationSiteName(String orchestrationSiteName) {
		this.orchestrationSiteName = orchestrationSiteName;
	}

	Site setSimulator(boolean isa) {
		isASimulator = isa;
		return this;
	}

	boolean isSimulator() { return isASimulator; }

	void setTestSession(gov.nist.asbestos.simapi.tk.simCommon.TestSession testSession) {
		this.testSession = testSession;
	}

	gov.nist.asbestos.simapi.tk.simCommon.TestSession getTestSession() {
		return testSession;
	}

	String getOwner() {
		if (owner == null)
			return gov.nist.asbestos.simapi.tk.simCommon.TestSession.DEFAULT_TEST_SESSION.getValue();
		return owner;
	}

	void setOwner(String owner) {
		this.owner = owner;
	}

	boolean hasOwner() {
		return owner != null;
	}
}
