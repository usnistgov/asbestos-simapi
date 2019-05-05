package gov.nist.asbestos.simapi.sim.basic


import gov.nist.asbestos.simapi.tk.simCommon.SimId
import groovy.transform.TypeChecked

/**
 * Store is organized as:
 * EC/testSession/psimdb/simId/actor/resource/event/event_files
 * The content starting from the event/ is handed off to the class Event
 */
@TypeChecked
class SimStore {
    File externalCache
    private File _simStoreLocation = null
    private File _simIdDir = null
    private File _resourceDir = null
    private File _actorDir
    private File _eventDir = null
    SimId simId
    String resource = null
    String eventId = null // within resource
    boolean newlyCreated = false
    static String PSIMDB = 'psimdb'
    Event event
    SimConfig config

    SimStore(File externalCache, SimId simId) {
        assert externalCache : "SimStore: initialized with externalCache == null"
        assert !simId.validateState() : "SimStore: cannot open SimId ${simId}:\n${simId.validateState()}\n"
        this.externalCache = externalCache
        this.simId = simId
    }

    SimStore(File externalCache) {
        assert externalCache : "SimStore: initialized with externalCache == null"
        this.externalCache = externalCache
    }


    File getStore(boolean create) {
        assert externalCache.exists() : "SimStore: External Cache must exist (${externalCache})\n"
        if (!_simStoreLocation) {
            _simStoreLocation = testSessionDir(externalCache, simId)
            if (create) {
                newlyCreated = !_simStoreLocation.exists()
                // assert !_simStoreLocation.exists() : "SimStore:Create: sim ${simId} at ${_simStoreLocation} already exists\n"
                _simStoreLocation.mkdirs()
                assert _simStoreLocation.exists() && _simStoreLocation.canWrite() && _simStoreLocation.isDirectory():
                        "SimStore: cannot create writable simdb directory at ${_simStoreLocation}\n"
            } else {
                assert _simStoreLocation.exists() && _simStoreLocation.canWrite() && _simStoreLocation.isDirectory():
                        "SimStore: Sim ${simId.toString()} does not exist\n"
            }
        }
        if (!simId.actorType && config)
            simId.actorType = config.actorType
        _simStoreLocation
    }

    File getStore() {
        getStore(false)
    }

    boolean expectingEvent() {
        simId && simId.actorType && resource
    }

    void deleteSim() {
        simDir.deleteDir()
    }

    void setSimId(SimId simId) {
        assert !simId.validateState() : "SimStore: cannot open SimId ${simId}:\n${simId.validateState()}\n"
        this.simId = simId
    }

    void setSimIdForLoader(SimId simId) {
        this.simId = simId
    }

    File testSessionDir(File externalCache, SimId simId) {
        new File(new File(externalCache, PSIMDB), simId.testSession.value)
    }

    String getActor() {
        simId.actorType
    }

    void setActor(String actor) {
        simId.actorType = actor
    }

    File getSimDir() {
        assert simId : "SimStore: simId is null"
        if (!_simIdDir)
            _simIdDir = new File(store, simId.id)
        _simIdDir.mkdirs()
        _simIdDir
    }

    File getActorDir() {
        assert actor : 'SimStore: actor is null'
        if (!_actorDir)
            _actorDir = new File(simDir, actor)
        _actorDir.mkdirs()
        _actorDir
    }


    File getResourceDir() {
        assert resource : 'SimStore: resource is null'
        if (!_resourceDir)
            _resourceDir = new File(actorDir, resource)
        _resourceDir.mkdirs()
        _resourceDir
    }

    File getEventDir() {
        assert eventId : "SimStore: eventId is null"
        if (!_eventDir)
            _eventDir = new File(resourceDir, eventId)
       // _eventDir.mkdirs()  // breaks createEvent(date)
        _eventDir
    }

    Event newEvent() {
        createEvent()
        event = new Event(this, _eventDir)
        event
    }

    File createEvent() {
        createEvent(new Date())
    }

    File createEvent(Date date) {
        File f = createEventDir(getEventIdFromDate(date))
        f.mkdirs()
        f
    }

    File useEvent(String eventId) {
        createEventDir(eventId)
    }

    SimStore withResource(String resource) {
        this.resource = resource
        this
    }

    SimStore withActorType(String actor) {
        this.simId.actorType = actor
        this
    }

    String getEventIdFromDate(Date date) {
        asFilenameBase(date)
    }

    /**
     * Given base name of a new event - extend it to ensure it is unique.
     * Does not create the directory, just the File
     * @param eventBase - usually date/time stamp
     * @return
     */
    private File createEventDir(String eventBase) {
        int incr = 0;
        _eventDir = null  // restart
        while (true) {
            eventId = eventBase
            if (incr != 0)
                eventId = eventBase + '_' + incr    // make unique
            if (eventDir.exists()) {
                // must be fresh new dir - try again
                incr++
            }
            else
                break
        }
        eventDir
    }

    String getEndpoint() {
        if (!config.fhirBase.endsWith('/'))
            config.fhirBase = "${config.fhirBase}/"
        "${config.fhirBase}${resource}"
    }


    static String asFilenameBase(Date date) {
        Calendar c  = Calendar.getInstance();
        c.setTime(date);

        String year = Integer.toString(c.get(Calendar.YEAR));
        String month = Integer.toString(c.get(Calendar.MONTH) + 1);
        if (month.length() == 1)
            month = "0" + month;
        String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
        if (day.length() == 1 )
            day = "0" + day;
        String hour = Integer.toString(c.get(Calendar.HOUR_OF_DAY));
        if (hour.length() == 1)
            hour = "0" + hour;
        String minute = Integer.toString(c.get(Calendar.MINUTE));
        if (minute.length() == 1)
            minute = "0" + minute;
        String second = Integer.toString(c.get(Calendar.SECOND));
        if (second.length() == 1)
            second = "0" + second;
        String mili = Integer.toString(c.get(Calendar.MILLISECOND));
        if (mili.length() == 2)
            mili = "0" + mili;
        else if (mili.length() == 1)
            mili = "00" + mili;

        String dot = "_";

        String val =
                year +
                        dot +
                        month +
                        dot +
                        day +
                        dot +
                        hour +
                        dot +
                        minute +
                        dot +
                        second +
                        dot +
                        mili
        ;
        return val;
    }
}
