package gov.nist.asbestos.simapi.sim

import gov.nist.asbestos.simapi.tk.simCommon.SimId
import gov.nist.asbestos.simapi.tk.simCommon.TestSession
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class SimStoreCreationTest extends Specification {

    @Rule
    TemporaryFolder tmp = new TemporaryFolder()

    def 'SimStore initialization' () {
        setup:
        File ecHome = tmp.newFolder('echome')
//        File ecHome = new File('/home/bill/tmp')
        File ec = new File(ecHome, 'ec')

        TestSession testSession = new TestSession('default')

        /////////////////////////////////////////////////////
        when:  // under-specified SimId
        SimId simId = new SimId(testSession, 'foo')
        SimStore simStore = new SimStore(ec, simId)

        then:
        AssertionError e = thrown()
        e.message.contains('No actorType')
        e.message.contains('No environmentName')

        /////////////////////////////////////////////////////
        when:  // good SimId
        simId = new SimId(testSession, 'foo', 'reg', 'cat')
        simStore = new SimStore(ec, simId)

        then:
        true // no exception
        !ec.exists()  // ec access not needed yet so not checked

        /////////////////////////////////////////////////////
        when:  // ec directory does not exist - this must error
        // ec must be created manually
        simStore.getStore()

        then:
        e = thrown()
        e.message.contains('External Cache must exist')

        /////////////////////////////////////////////////////
        when: // manually create ec - now SimStore can initialize
        ec.mkdirs()
        simStore = new SimStore(ec, simId)
        simStore.getStore()  // this uses ec

        then: // fsimdb initialized
        new File(new File(ec, 'fsimdb'), 'default').exists()
    }

    def 'event initialization' () {
        setup:
//        File ecHome = new File('/home/bill/tmp')
        File ecHome = tmp.newFolder('echome2')
        File ec = new File(ecHome, 'ec')
        ec.mkdirs()

        TestSession testSession = new TestSession('default')

        /////////////////////////////////////////////////////
        when:
        SimId simId = new SimId(testSession, 'foo', 'reg', 'cat')
        SimStore simStore = new SimStore(ec, simId).withTransaction('store')
        Event event1 = simStore.newEvent()
        Event event2 = simStore.newEvent()

        then:
        event1.root.exists()
        event2.root.exists()
        event1 != event2
    }
}
