package gov.nist.asbestos.simapi.sim

import gov.nist.asbestos.simapi.tk.simCommon.SimId
import gov.nist.asbestos.simapi.tk.simCommon.TestSession
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class EventTest extends Specification {

    @Rule
    TemporaryFolder tmp = new TemporaryFolder()

    def 'request' () {
        setup:
//       File ecHome = tmp.newFolder('echome')
        File ecHome = new File('/home/bill/tmp')
        File ec = new File(ecHome, 'ec')
        ec.mkdirs()

        TestSession testSession = new TestSession('default')
        SimId simId = new SimId(testSession, 'foo', 'reg', 'cat')
        SimStore simStore = new SimStore(ec, simId).withTransaction('store')
        Event event = simStore.newEvent()

        String requestHdr = 'Request Header'
        String requestBody = 'Request Body'
        String task1ReqHdr = 'Task 1 Req Header'
        String task1ReqBody = 'Task 1 Req Body'
        String task1ResHdr = 'Task 1 Res Header'
        String task1ResBody = 'Task 1 Res Body'
        String task2ReqHdr = 'Task 2 Req Header'
        String task2ReqBody = 'Task 2 Req Body'
        String task2ResHdr = 'Task 2 Res Header'
        String task2ResBody = 'Task 2 Res Body'
        String responseHdr = 'Response Header'
        String responseBody = 'Response Body'

        /////////////////////////////////////////////////////
        when:  // build request with 2 tasks
        event.selectRequest()
        event.putRequestHeader(requestHdr)
        event.putRequestBody(requestBody.bytes)

        event.newTask
        event.putRequestHeader(task1ReqHdr)
        event.putRequestBody(task1ReqBody.bytes)
        event.putResponseHeader(task1ResHdr)
        event.putResponseBody(task1ResBody.bytes)

        event.newTask
        event.putRequestHeader(task2ReqHdr)
        event.putRequestBody(task2ReqBody.bytes)
        event.putResponseHeader(task2ResHdr)
        event.putResponseBody(task2ResBody.bytes)

        event.selectRequest()
        event.putResponseHeader(responseHdr)
        event.putResponseBody(responseBody.bytes)

        then:
        event.selectTask(0)
        task1ReqHdr == event.getRequestHeader()
        task1ReqBody == event.getRequestBodyAsString()
        task1ResHdr == event.getResponseHeader()
        task1ResBody == event.getResponseBodyAsString()

        event.selectRequest()
        requestHdr == event.getRequestHeader()
        requestBody == event.getRequestBodyAsString()

        event.selectTask(1)
        task2ReqHdr == event.getRequestHeader()
        task2ReqBody == event.getRequestBodyAsString()
        task2ResHdr == event.getResponseHeader()
        task2ResBody == event.getResponseBodyAsString()

        event.selectRequest()
        responseHdr == event.getResponseHeader()
        responseBody == event.getResponseBodyAsString()
    }
}
