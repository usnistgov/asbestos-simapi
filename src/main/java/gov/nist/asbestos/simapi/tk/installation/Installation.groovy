package gov.nist.asbestos.simapi.tk.installation

import gov.nist.asbestos.simapi.tk.simCommon.TestSession
import groovy.transform.TypeChecked

@TypeChecked
class Installation {
    private static Installation me = null;
    private File externalCache
    String servletContextName = 'asbestos'
    PropertyServiceManager propertyServiceManager = new PropertyServiceManager()
    File defaultEnvironmentFile = new File("${externalCache}/environment/default")
    String toolkitBaseUrl = 'http://localhost:8080/xdstools'

    File  externalCache() {
        assert externalCache : "External Cache location not set"
        assert externalCache.exists() : "External Cache does not exist - ${externalCache}"
        externalCache
    }

    void setExternalCache(File externalCache) {
        this.externalCache = externalCache
    }

    File fsimDbFile() {
        File f = new File(externalCache(), 'fsimDb')
        f.mkdirs()
        f
    }

    static Installation instance() {
        if (me == null) {
            me = new Installation();
        }
        return me;
    }

    private Installation() {
    }

    PropertyServiceManager propertyServiceManager() {
        propertyServiceManager
    }

    String toString() {
        "External Cache is ${externalCache()}"
    }

    File fhirSimDbFile(TestSession testSession) {
        return simDbFile(testSession);
    }

    File simDbFile(TestSession testSession) {
        assert testSession : "TestSession is null"
        return new File(simDbFile(), testSession.getValue());
    }

    File simDbFile() {
        return new File(externalCache(), "fsimdb");
    }

    File environmentDbFile() {
        new File(externalCache(), 'environment')
    }

    File environmentFile(String environment) {
        new File(environmentDbFile(), environment)
    }

    boolean environmentExists(String environment) {
        environmentFile(environment).exists()
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

    File actorsDir() {
        return new File(externalCache(), File.separator + "actors");
    }

    File actorsDir(TestSession testSession) {
        assert testSession : "TestSession is null"
        File f = new File(actorsDir(), testSession.getValue());
        f.mkdirs();
        return f;
    }

    List<TestSession> getTestSessions() {
        Set<TestSession> ts = new HashSet<>();
        File tlsFile

        tlsFile = simDbFile();
        if (tlsFile.exists()) {
            for (File tlFile : tlsFile.listFiles()) {
                if (tlFile.isDirectory() && !tlFile.getName().startsWith("."))
                    ts.add(new TestSession(tlFile.getName()));
            }
        }

        tlsFile = actorsDir();
        if (tlsFile.exists()) {
            for (File tlFile : tlsFile.listFiles()) {
                if (tlFile.isDirectory() && !tlFile.getName().startsWith("."))
                    ts.add(new TestSession(tlFile.getName()));
            }
        }
        List<TestSession> testSessions = new ArrayList<>();
        testSessions.addAll(ts);
        return testSessions;
    }

    boolean testSessionExists(TestSession testSession) {
        return getTestSessions().contains(testSession);
    }


}
