package gov.nist.asbestos.simapi.sim


import gov.nist.asbestos.simapi.tk.simCommon.SimId
import groovy.transform.TypeChecked

/**
 * Store is organized as:
 * EC/testSession/fsimdb/simId/actor/transaction/event/event_files
 * The content starting from the event/ is handed off to the class Event
 */
@TypeChecked
class SimStore {
    File externalCache
    private File _simStoreLocation = null
    private File _actorDir = null
    private File _transactionDir = null
    private File _eventDir = null
    SimId simId
    String transaction = null
    String eventId = null // within transaction

    SimStore(File externalCache, SimId simId) {
        assert externalCache : "SimStore: initialized with externalCache == null"
        assert !simId.validateState() : "SimStore: initialized with SimId with problems:\n${simId.validateState()}\n"
        this.externalCache = externalCache
        this.simId = simId
    }

    File getStore() {
        assert externalCache.exists() : "SimStore: External Cache must exist (${externalCache})\n"
        if (!_simStoreLocation) {
            _simStoreLocation = new File(new File(externalCache, 'fsimdb'), simId.testSession.value)
            _simStoreLocation.mkdirs()
            assert _simStoreLocation.exists() && _simStoreLocation.canWrite() && _simStoreLocation.isDirectory() :
                    "SimStore: cannot create writable simdb directory at ${_simStoreLocation}"
        }
        _simStoreLocation
    }

    String getActor() {
        simId.actorType
    }

    File getActorDir() {
        assert actor : "SimStore: actor is null"
        if (!_actorDir)
            _actorDir = new File(store, actor)
        _actorDir
    }

    File getTransactionDir() {
        assert transaction : 'SimStore: transaction is null'
        if (!_transactionDir)
            _transactionDir = new File(actorDir, transaction)
        _transactionDir
    }

    File getEventDir() {
        assert eventId : "SimStore: eventId is null"
        if (!_eventDir)
            _eventDir = new File(transactionDir, eventId)
        _eventDir
    }

    Event newEvent() {
        createEvent()
        new Event(this, _eventDir)
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

    SimStore withTransaction(String transaction) {
        this.transaction = transaction
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
