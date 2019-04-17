package gov.nist.tk.simCommon

import gov.nist.tk.actors.ActorType
import gov.nist.tk.actors.TransactionType
import gov.nist.tk.installation.Installation
import gov.nist.tk.installation.PropertyServiceManager
import gov.nist.tk.siteManagement.Site
import gov.nist.tk.util.UuidAllocator
import gov.nist.toolkit.configDatatypes.client.FhirVerb
import gov.nist.toolkit.configDatatypes.client.PatientErrorMap
import gov.nist.toolkit.configDatatypes.server.SimulatorProperties
import gov.nist.toolkit.envSettings.EnvSetting;
import groovy.transform.TypeChecked;
import org.apache.log4j.Logger;


/**
 * Factory class for simulators.  Technically ActorFactry is no longer accurate
 * since simulators are being built for things that are not IHE actors.
 * Oh well!
 * @author bill
 *
 */
@TypeChecked
 abstract class AbstractActorFactory {
	static Logger logger = Logger.getLogger(AbstractActorFactory.class);

	/*
	 *
	 *  Abstracts
	 *
	 */
	protected abstract Simulator buildNew(SimManager simm, SimId simId, String environment, boolean configureBase)
	protected abstract void verifyActorConfigurationOptions(SimulatorConfig config)
	 abstract Site buildActorSite(SimulatorConfig asc, Site site)
	 abstract List<TransactionType> getIncomingTransactions();

	private static boolean initialized = false;
	/**
	 * ActorType.name ==> ActorFactory
	 */
	static private Map<String, AbstractActorFactory> theFactories = null;
	private static Map<String, AbstractActorFactory> factories() {
	 	if (theFactories != null) return theFactories;
		theFactories = new HashMap<>();

		 logger.info("Loading Actor Factories");

		// for this loader to work, the following requirements must be met by the factory class:
		// 1. Extend class AbstractActorFactory


		for (ActorType actorType : ActorType.types) {
			String factoryClassName = actorType.getSimulatorFactoryName();
			if (!factoryClassName) continue;
				Class c = Class.forName(factoryClassName);
				AbstractActorFactory inf = (AbstractActorFactory) c.newInstance();
				logger.info("Loading ActorType " + actorType.getName());
				theFactories.put(actorType.getName(), inf);
		}
		initialized = true;
		return theFactories;
	}

	Site getActorSite(SimulatorConfig asc, Site site)  {
		Site finalSite = buildActorSite(asc, site);
		if (finalSite == null) return null;
		finalSite.setOwner(asc.getTestSession().getValue());
		return finalSite;
	}

	 static boolean isInitialized() {
		return initialized;
	}

	 AbstractActorFactory getActorFactory(ActorType at) {
		return factories().get(at.getName());
	}



    static final String name = "Name";
	static final String isTls = "UseTLS";
	static final String owner = "Owner";
	static final String description = "Description";

	private boolean transactionOnly = false;
	 boolean isSimProxy = false;

	 AbstractActorFactory asSimProxy() {
		isSimProxy = true;
		return this;
	}

	 AbstractActorFactory asNotSimProxy() {
		isSimProxy = false;
		return this;
	}

	PropertyServiceManager propertyServiceMgr = null;

	static  ActorType getActorTypeFromName(String name) {
		return ActorType.findActor(name);
	}

	protected SimulatorConfig configureBaseElements(ActorType simType, TestSession testSession, String environment) {
		return configureBaseElements(simType, null, testSession, environment);
	}

	protected SimulatorConfig configureBaseElements(ActorType simType, SimId newId, TestSession testSession, String environment) {
		if (newId == null)
			newId = getNewId(testSession);
		SimulatorConfig sc = new SimulatorConfig(newId, simType.getShortName(), SimDb.getNewExpiration(SimulatorConfig.class), environment);

		return configureBaseElements(sc);
	}

	protected void configEnv(SimManager simm, SimId simId, SimulatorConfig sc) {
		if (simId.getEnvironmentName() != null) {
			EnvSetting es = new EnvSetting(simId.getEnvironmentName());
			File codesFile = es.getCodesFile();
			addEditableConfig(sc, SimulatorProperties.codesEnvironment, ParamType.SELECTION, codesFile.toString());
		} else {
			File codesFile = EnvSetting.getEnvSetting(simm.sessionId()).getCodesFile();
			addEditableConfig(sc, SimulatorProperties.codesEnvironment, ParamType.SELECTION, codesFile.toString());
		}
	}

	SimulatorConfig configureBaseElements(SimulatorConfig sc) {
		SimulatorConfigElement ele;

		ele = new SimulatorConfigElement();
		ele.name = SimulatorProperties.creationTime;
		ele.type = ParamType.TIME;
		ele.setStringValue(new Date().toString());
		addFixed(sc, ele);

		ele = new SimulatorConfigElement();
		ele.name = name;
		ele.type = ParamType.TEXT;
		ele.setStringValue(sc.getId().toString());
		addFixed(sc, ele);

		addEditableConfig(sc, SimulatorProperties.locked, ParamType.BOOLEAN, false);
		addEditableConfig(sc, SimulatorProperties.requiresStsSaml, ParamType.BOOLEAN, false);
        addEditableConfig(sc, SimulatorProperties.FORCE_FAULT, ParamType.BOOLEAN, false);
		addFixedConfig(sc, SimulatorProperties.environment, ParamType.TEXT, sc.getEnvironmentName());

        return sc;
	}

	protected AbstractActorFactory() {

	}

	// Returns list since multiple simulators could be built as a grouping/cluster
	// only used by SimulatorFactory to offer a generic API for building sims
	 Simulator buildNewSimulator(SimManager simm, String simtype, SimId simID, String environment, boolean save) throws Exception {
        logger.info("Build New Simulator " + simtype);
		ActorType at = ActorType.findActor(simtype);

		 assert at : "Simulator type [" + simtype + "] does not exist"

		return buildNewSimulator(simm, at, simID, environment, save);

	}

	private ActorType actorType = null;

	 ActorType getActorType() {
		return actorType;
	}

	 Simulator buildNewSimulator(SimManager simm, ActorType at, SimId simID, String environment, boolean save) throws Exception {
		logger.info("Build new Simulator of type " + getClass().getSimpleName() + " simID: " + simID);

		// This is the simulator-specific factory
        String actorTypeName;
		actorTypeName = at.getName();
		AbstractActorFactory af = factories().get(actorTypeName);
		actorType = ActorType.findActor(actorTypeName);

		if (af == null)
			throw new Exception(String.format("Cannot build simulator of type %s - cannot find Factory for ActorType", actorTypeName));

		af.actorType = actorType;

        if (simID.getId().contains("__"))
            throw new Exception("Simulator ID cannot contain double underscore (__)");

		Simulator simulator = af.buildNew(simm, simID, environment,true);

		if (simulator.size() > 1) {
			List<String> simIdsInGroup = new ArrayList<>();
			for (SimulatorConfig conf : simulator.getConfigs())
				simIdsInGroup.add(conf.getId().toString());
			for (SimulatorConfig conf : simulator.getConfigs()) {
				SimulatorConfigElement ele = new SimulatorConfigElement(SimulatorProperties.simulatorGroup, ParamType.LIST, simIdsInGroup);
				conf.add(ele);
			}
		}

		// This is out here instead of being attached to a simulator-specific factory - why?
		if (save) {
			for (SimulatorConfig conf : simulator.getConfigs()) {
				AbstractActorFactory actorFactory = getActorFactory(conf);
				saveConfiguration(conf);

				BaseActorSimulator sim = RuntimeManager.getSimulatorRuntime(conf.getId());
				logger.info("calling onCreate:" + conf.getId().toString());
				sim.onCreate(conf);
			}

			if (isSimProxy) {
				for (SimulatorConfig conf : simulator.getConfigs()) {
					conf.getId().forFhir();  // label it FHIR so it gets re-saved there
					AbstractActorFactory actorFactory = getActorFactory(conf);
					saveConfiguration(conf);

					BaseActorSimulator sim = RuntimeManager.getSimulatorRuntime(conf.getId());
					logger.info("calling onCreate:" + conf.getId().toString());
					sim.onCreate(conf);
				}
			}
		}

		return simulator;
	}


	//
	// End of hooks


	// A couple of utility classes that get around a client class calling a server class - awkward
	static  ActorType getActorType(SimulatorConfig config) {
		return ActorType.findActor(config.getActorType());
	}

	static  AbstractActorFactory getActorFactory(SimulatorConfig config) {
		ActorType actorType = getActorType(config);
		String actorTypeName = actorType.getName();
		AbstractActorFactory actorFactory = factories().get(actorTypeName);
		return actorFactory;
	}

	 List<SimulatorConfig> checkExpiration(List<SimulatorConfig> configs) {
		List<SimulatorConfig> remove = new ArrayList<SimulatorConfig>();

		for (SimulatorConfig sc : configs) {
			if (sc.checkExpiration())
				remove.add(sc);
		}
		configs.removeAll(remove);
		return configs;
	}


	private SimId getNewId(TestSession testSession) {
		String id = UuidAllocator.allocate();
		String[] parts = id.split(":");
		id = parts[2];

            return new SimId(testSession, id);
	}

	String mkEndpoint(SimulatorConfig asc, SimulatorConfigElement ele, boolean isTLS) throws Exception {
		return mkEndpoint(asc, ele, asc.getActorType().toLowerCase(), isTLS);
	}

	protected String mkEndpoint(SimulatorConfig asc, SimulatorConfigElement ele, String actor, boolean isTLS) throws Exception {
		return mkEndpoint(asc, ele, actor, isTLS, false);
	}

	protected String mkEndpoint(SimulatorConfig asc, SimulatorConfigElement ele, String actor, boolean isTLS, boolean isProxy) throws Exception {
		String transtype = SimDb.getTransactionDirName(ele.transType);

		String contextName = Installation.instance().getServletContextName();

		return "http"
		+ ((isTLS) ? "s" : "")
		+ "://"
		+ Installation.instance().propertyServiceManager().getToolkitHost()
		+ ":"
				+ getEndpointPort(isTLS, isProxy)
//		+ ((isTLS) ? Installation.instance().propertyServiceManager().getToolkitTlsPort() : Installation.instance().propertyServiceManager().getToolkitPort())
//		+ "/"  context name includes preceding /
		+ contextName
		+ (ele.transType.isHttpOnly() ? "/httpsim/" : "/sim/" )
		+ asc.getId()
		+ "/" +
		actor           //asc.getActorType().toLowerCase()
		+ "/"
		+ transtype;
	}

	protected String mkFhirEndpoint(SimulatorConfig asc, SimulatorConfigElement ele, String actor, boolean isTLS) throws Exception {
		return mkFhirEndpoint(asc, ele, actor, null, isTLS, false);
	}

	protected String mkFhirEndpoint(SimulatorConfig asc, SimulatorConfigElement ele, String actor, TransactionType transactionType, boolean isTLS, boolean isProxy) throws Exception {
//		String transtype = SimDb.getTransactionDirName(ele.transType);

		String contextName = Installation.instance().getServletContextName();

		return "http"
				+ ((isTLS) ? "s" : "")
				+ "://"
				+ Installation.instance().propertyServiceManager().getToolkitHost()
				+ ":"
				+ getEndpointPort(isTLS, isProxy)
//				+ ((isTLS) ? Installation.instance().propertyServiceManager().getToolkitTlsPort() : Installation.instance().propertyServiceManager().getToolkitPort())
//		+ "/"  context name includes preceding /
				+ contextName
//				+ "/sim/"
				+ ((isSimProxy) ? "/sim/" : "/fsim/")
				+ asc.getId()
				+ "/" + actor
	//			+ "/fhir"
				+ ((transactionType != null && transactionType.getFhirVerb() == FhirVerb.TRANSACTION ? "/" + transactionType.getShortName() : ""))
				;
	}

	private String getEndpointPort(boolean isTLS, boolean isProxy) throws Exception {
		if (isTLS && isProxy)
			throw new Exception("Proxy does not support TLS");
		if (isProxy)
			return Installation.instance().propertyServiceManager().getProxyPort();
		return (isTLS) ? Installation.instance().propertyServiceManager().getToolkitTlsPort() : Installation.instance().propertyServiceManager().getToolkitPort();
	}

	 void saveConfiguration(SimulatorConfig config) throws Exception {
		verifyActorConfigurationOptions(config);

		SimDb simdb = new SimDb().mkSim(config.getId(), config.getActorType());
		File simCntlFile = simdb.getSimulatorControlFile();
		SimulatorConfigIoFactory.impl().save(config, simCntlFile.toString());
	}

	static  void delete(SimulatorConfig config) throws Exception {
        delete(config.getId());
    }

    static  void delete(SimId simId) {
        logger.info("delete simulator " + simId);
		SimDb simdb;
			BaseActorSimulator sim = RuntimeManager.getSimulatorRuntime(simId);
			SimulatorConfig config = loadSimulator(simId, true);
			if (config != null)
				sim.onDelete(config);

			simdb = new SimDb(simId);
			simdb.delete();
	}

	static  List<TransactionInstance> getTransInstances(SimId simid, String xactor, String trans)
	{
		SimDb simdb;
		simdb = new SimDb(simid);
		ActorType actor = simdb.getSimulatorActorType();
		return simdb.getTransInstances(actor.toString(), trans);
	}

	// update internal to sim to align with current simId
	static  void updateSimConfiguration(SimId simId) throws Exception {
		SimulatorConfig config = loadSimulator(simId, false);

		config.setId(simId);

		SimulatorConfigElement ele = config.getConfigEle(name);
		ele.setStringValue(simId.toString());

		new GenericSimulatorFactory().saveConfiguration(config);

		new SimDb(simId).updateSimConfiguration();
	}

	static  void renameSimFile(String simFileSpec, String newSimFileSpec)
			throws Exception {
		throw new Exception("Not Implemented");
	}

	static  List<SimulatorConfig> getSimConfigs(ActorType actorType, TestSession testSession) {
		return getSimConfigs(actorType.getName(), testSession);
	}

	static  List<SimulatorConfig> getSimConfigs(String actorTypeName, TestSession testSession) {
		List<SimId> allSimIds = SimDb.getAllSimIds(testSession);
		List<SimulatorConfig> simConfigs = new ArrayList<>();

			for (SimulatorConfig simConfig : loadSimulators(allSimIds)) {
				if (actorTypeName.equals(simConfig.getActorType()))
					simConfigs.add(simConfig);
			}

		return simConfigs;
	}


	/**
	 * Load simulators - IOException if sim not found
	 * @param ids
	 * @return
	 */
	static  List<SimulatorConfig> loadSimulators(List<SimId> ids)  {
		List<SimulatorConfig> configs = new ArrayList<SimulatorConfig>();

		for (SimId id : ids) {
			SimDb simdb = new SimDb(id);
			File simCntlFile = simdb.getSimulatorControlFile();
			SimulatorConfig config = restoreSimulator(simCntlFile.toString());
			configs.add(config);
		}

		return configs;
	}

	/**
	 * Load simulators - ignore sims not found (length(simlist) &lt; length(idlist))
	 * @param ids
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	 List<SimulatorConfig> loadAvailableSimulators(List<SimId> ids) throws Exception {
		List<SimulatorConfig> configs = new ArrayList<SimulatorConfig>();

		for (SimId id : ids) {
				if (!SimDb.exists(id)) continue
				SimDb simdb = new SimDb(id);
				File simCntlFile = simdb.getSimulatorControlFile();
				SimulatorConfig config = restoreSimulator(simCntlFile.toString());
				configs.add(config);
		}

		return configs;
	}

	private static SimulatorConfig restoreSimulator(String filename) throws Exception {
		return SimulatorConfigIoFactory.impl().restoreSimulator(filename);
	}

	 static SimulatorConfig loadSimulator(SimId simid, boolean okifNotExist) throws Exception {
		SimDb simdb;
		File simCntlFile;
		try {
			if (SimDb.exists(simid)) {
				simdb = new SimDb(simid);
				simCntlFile = simdb.getSimulatorControlFile();
				SimulatorConfig config = restoreSimulator(simCntlFile.toString());
				return config;
			} else {
				return null;
			}
		} catch (Exception e) {
			if (okifNotExist) return null;
			throw e;
		}

	}

	static  SimulatorConfig getSimConfig(SimId simulatorId)  {
		assert SimDb.exists(simulatorId) : "No simulator for simId: " + simulatorId.toString()
			SimDb simdb = new SimDb(simulatorId);
			File simCntlFile = simdb.getSimulatorControlFile();
			return restoreSimulator(simCntlFile.toString());
	}

	protected boolean isEndpointSecure(String endpoint) {
		return endpoint.startsWith("https");
	}

	protected List<SimulatorConfig> asList(SimulatorConfig asc) {
		List<SimulatorConfig> ascs = new ArrayList<SimulatorConfig>();
		ascs.add(asc);
		return ascs;
	}

	 void addFixed(SimulatorConfig sc, SimulatorConfigElement ele) {
		ele.setEditable(false);
		sc.elements().add(ele);
	}

	private void addUser(SimulatorConfig sc, SimulatorConfigElement ele) {
		ele.setEditable(true);
		sc.elements().add(ele);
	}

	 void addEditableConfig(SimulatorConfig sc, String name, ParamType type, Boolean value) {
		addUser(sc, new SimulatorConfigElement(name, type, value));
	}

	 void addEditableConfig(SimulatorConfig sc, String name, ParamType type, String value) {
		addUser(sc, new SimulatorConfigElement(name, type, value));
	}

	 void addEditableConfig(SimulatorConfig sc, String name, ParamType type, List<String> value) {
		addUser(sc, new SimulatorConfigElement(name, type, value));
	}

	 void addEditableConfig(SimulatorConfig sc, String name, ParamType type, List<String> values, boolean isMultiSelect) {
        addUser(sc, new SimulatorConfigElement(name, type, values, isMultiSelect));
    }

     void addEditableConfig(SimulatorConfig sc, String name, ParamType type, PatientErrorMap value) {
        addUser(sc, new SimulatorConfigElement(name, type, value));
    }

	 void addFixedConfig(SimulatorConfig sc, String name, ParamType type, Boolean value) {
		addFixed(sc, new SimulatorConfigElement(name, type, value));
	}

	 void addFixedConfig(SimulatorConfig sc, String name, ParamType type, List<String> value) {
		addFixed(sc, new SimulatorConfigElement(name, type, value));
	}

	 void addFixedConfig(SimulatorConfig sc, String name, ParamType type, String value) {
		addFixed(sc, new SimulatorConfigElement(name, type, value));
	}

	 void setConfig(SimulatorConfig sc, String name, String value) {
		SimulatorConfigElement ele = sc.getUserByName(name);
		 assert ele : "Simulator " + sc.getId() + " does not have a parameter named " + name + " or cannot set its value"
		ele.setStringValue(value);
	}

	 void setConfig(SimulatorConfig sc, String name, Boolean value) {
		SimulatorConfigElement ele = sc.getUserByName(name);
		 assert ele : "Simulator " + sc.getId() + " does not have a parameter named " + name + " or cannot set its value"
		ele.setBooleanValue(value);
	}

	 void addEditableEndpoint(SimulatorConfig sc, String endpointName, ActorType actorType, TransactionType transactionType, boolean tls) throws Exception {
		SimulatorConfigElement ele = new SimulatorConfigElement();
		ele.name = endpointName;
		ele.type = ParamType.ENDPOINT;
		ele.transType = transactionType;
		ele.setTls(tls);
		ele.setStringValue(mkEndpoint(sc, ele, actorType.getShortName(), tls));
		addUser(sc, ele);
	}

	 void addEditableNullEndpoint(SimulatorConfig sc, String endpointName, ActorType actorType, TransactionType transactionType, boolean tls) {
		SimulatorConfigElement ele = new SimulatorConfigElement();
		ele.name = endpointName;
		ele.type = ParamType.ENDPOINT;
		ele.transType = transactionType;
		ele.setTls(tls);
		ele.setStringValue("");
		addUser(sc, ele);
	}

	 void addFixedEndpoint(SimulatorConfig sc, String endpointName, ActorType actorType, TransactionType transactionType, boolean tls) throws Exception {
		SimulatorConfigElement ele = new SimulatorConfigElement();
		ele.name = endpointName;
		ele.type = ParamType.ENDPOINT;
		ele.transType = transactionType;
		ele.setStringValue(mkEndpoint(sc, ele, actorType.getShortName(), tls));
		ele.setTls(tls);
		addFixed(sc, ele);
	}

	 void addFixedFhirEndpoint(SimulatorConfig sc, String endpointName, ActorType actorType, TransactionType transactionType, boolean tls) throws Exception {
		addFixedFhirEndpoint(sc, endpointName, actorType, transactionType, tls, false);
	}

	 void addFixedFhirEndpoint(SimulatorConfig sc, String endpointName, ActorType actorType, TransactionType transactionType, boolean tls, boolean proxy) throws Exception {
		SimulatorConfigElement ele = new SimulatorConfigElement();
		ele.name = endpointName;
		ele.type = ParamType.ENDPOINT;
		ele.transType = transactionType;
		ele.setStringValue(mkFhirEndpoint(sc, ele, actorType.getShortName(), transactionType, tls, proxy));
		ele.setTls(tls);
		addFixed(sc, ele);
	}

	 boolean isTransactionOnly() {
		return transactionOnly;
	}

	 void setTransactionOnly(boolean transactionOnly) {
		this.transactionOnly = transactionOnly;
	}



}
