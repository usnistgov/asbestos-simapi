package gov.nist.asbestos.simapi.sim

import gov.nist.asbestos.simapi.sim.basic.Event
import gov.nist.asbestos.simapi.sim.basic.SimStore
import gov.nist.asbestos.simapi.sim.headers.HeaderBuilder
import gov.nist.asbestos.simapi.tk.simCommon.SimId
import gov.nist.asbestos.simapi.tk.simCommon.TestSession
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class EventTest extends Specification {

    @Rule
    TemporaryFolder tmp = new TemporaryFolder()

    def 'request response tasks' () {
        setup:
//        File ecHome = new File('/home/bill/tmp')
        File ecHome = tmp.newFolder('echome')
        File ec = new File(ecHome, 'ec')
        ec.mkdirs()

        TestSession testSession = new TestSession('default')
        SimId simId = new SimId(testSession, 'foo', 'reg', 'cat')
        SimStore simStore = new SimStore(ec, simId).withTransaction('store')
        simStore.getStore(true) // create event
        Event event = simStore.newEvent()

        String requestHdr = 'Request Header' + body()
        String requestBody = 'Request Body' + body()
        String task1ReqHdr = 'Task /1/Req/Header' + body()
        String task1ReqBody = 'Task /1/Req/Body' + body()
        String task1ResHdr = 'Task /1/Res/Header' + body()
        String task1ResBody = 'Task /1/Res/Body' + body()
        String task2ReqHdr = 'Task /2/Req/Header' + body()
        String task2ReqBody = 'Task /2/Req/Body' + body()
        String task2ResHdr = 'Task /2/Res/Header' + body()
        String task2ResBody = 'Task /2/Res/Body' + body()
        String responseHdr = 'Response Header' + body()
        String responseBody = 'Response Body' + body()

        /////////////////////////////////////////////////////
        when:  // build request with 2 tasks
        event.selectRequest()
        event.putRequestHeader(HeaderBuilder.rawHeadersFromString(requestHdr))
        event.putRequestBody(requestBody.bytes)

        event.newTask
        event.putRequestHeader(HeaderBuilder.rawHeadersFromString(task1ReqHdr))
        event.putRequestBody(task1ReqBody.bytes)
        event.putResponseHeader(HeaderBuilder.parseHeaders(task1ResHdr))
        event.putResponseBody(task1ResBody.bytes)

        event.newTask
        event.putRequestHeader(HeaderBuilder.rawHeadersFromString(task2ReqHdr))
        event.putRequestBody(task2ReqBody.bytes)
        event.putResponseHeader(HeaderBuilder.parseHeaders(task2ResHdr))
        event.putResponseBody(task2ResBody.bytes)

        event.selectRequest()
        event.putResponseHeader(HeaderBuilder.parseHeaders(responseHdr))
        event.putResponseBody(responseBody.bytes)

        then:
        event.selectTask(0)
        task1ReqHdr == event.getRequestHeader().toString()
        task1ReqBody == event.getRequestBodyAsString()
        task1ResHdr == event.getResponseHeader()
        task1ResBody == event.getResponseBodyAsString()

        event.selectRequest()
        requestHdr == event.getRequestHeader().toString()
        requestBody == event.getRequestBodyAsString()

        event.selectTask(1)
        task2ReqHdr == event.getRequestHeader().toString()
        task2ReqBody == event.getRequestBodyAsString()
        task2ResHdr == event.getResponseHeader()
        task2ResBody == event.getResponseBodyAsString()

        event.selectRequest()
        responseHdr == event.getResponseHeader()
        responseBody == event.getResponseBodyAsString()
    }

    def body() {
        '''\r
content-type: application/json\r
user-agent: Java/1.8.0_191\r
host: localhost:8080\r
accept: text/html, image/gif, image/jpeg, *; q=.2, */*; q=.2\r
connection: keep-alive\r
content-length: 31\r\n'''
    }
}
