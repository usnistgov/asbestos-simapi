package gov.nist.asbestos.simapi.sim.basic

import gov.nist.asbestos.simapi.tk.simCommon.SimId
import gov.nist.asbestos.simapi.tk.simCommon.TestSession
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

// This is separate because @TypeChecked must be off for new SimConfig(rawConfig)
// Won't compile
class SimStoreBuilder {
    static SimStore builder(File externalCache, SimConfig simConfig) {
        SimStore simStore = new SimStore(externalCache)
        simStore.config = simConfig
        String json = JsonOutput.toJson(simConfig)
        simStore.simId = getSimId(simConfig)
        simStore.getStore(true) // create
        File configFile = new File(simStore.simDir, 'config.json')
        simStore.newlyCreated = !configFile.exists()
        configFile.text = JsonOutput.prettyPrint(json)
        simStore
    }

    static SimStore loader(File externalCache, TestSession testSession, String id) {
        SimStore simStore = new SimStore(externalCache)
        simStore.setSimIdForLoader(new SimId(testSession, id))  // doesn't do Id validation
        Map rawConfig = (Map) new JsonSlurper().parse(new File(simStore.simDir, 'config.json'))
        SimConfig simConfig = new SimConfigMapper(rawConfig).build()
        simStore.simId = getSimId(simConfig)
        simStore.config = simConfig
        simStore
    }

    static SimId getSimId(SimConfig simConfig) {
        new SimId(new TestSession(simConfig.testSession), simConfig.simId, simConfig.actorType, simConfig.environment)
    }

    static SimConfig buildSimConfig(String json) {
        Map rawConfig = (Map) new JsonSlurper().parseText(json)
        new SimConfigMapper(rawConfig).build()
    }

}
