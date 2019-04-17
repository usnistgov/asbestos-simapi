package gov.nist.tk.simCommon


import gov.nist.toolkit.configDatatypes.client.Pid
import gov.nist.toolkit.configDatatypes.client.PidBuilder
import groovy.transform.TypeChecked
import org.apache.log4j.Logger

/**
 * Manager PIDs within a Simulator
 */
@TypeChecked
class PidDb {
    static Logger logger = Logger.getLogger(PidDb.class);
    private final SimDb simDb;

    PidDb(SimDb simDb) {
        this.simDb = simDb;
    }

    File getPatientIDFeedDir() {
        File regDir = new File(simDb.getSimDir(), 'reg');
        File pifDir = new File(regDir, "pif");
        return pifDir;
    }

    List<Pid> getAllPatientIds() {
        List<Pid> pids = new ArrayList<>();
        File pidDir = getPatientIDFeedDir();
        File[] aaDirs = pidDir.listFiles();
        if (aaDirs == null) return pids;
        for (int aai = 0; aai < aaDirs.length; aai++) {
            File aaDir = aaDirs[aai];
            if (!aaDir.isDirectory()) continue;
            String aaString = aaDir.getName();
            File[] pidFiles = aaDir.listFiles();
            if (pidFiles == null) continue;
            for (int pidi = 0; pidi < pidFiles.length; pidi++) {
                File pidFile = pidFiles[pidi];
                if (!pidFile.getName().endsWith(".txt")) continue;
                String pid = simDb.stripFileType(pidFile.getName(), "txt");
                pids.add(new Pid(aaString, pid));
            }
        }
        return pids;
    }

    File getAffinityDomainDir(String adOid) {
        File regDir = new File(simDb.getSimDir(), 'reg');
        File pifDir = new File(regDir, "pif");
        File adDir = new File(pifDir, adOid);
        adDir.mkdirs();
        return adDir;
    }

    File getPidFile(Pid pid) {
        File adFile = getAffinityDomainDir(pid.getAd());
        File pidFile = new File(adFile, pid.getId() + ".txt");
        return pidFile;
    }

    void addPatientId(String patientId) throws IOException {
        addPatientId(PidBuilder.createPid(patientId));
    }

    void addPatientId(Pid pid) throws IOException {
        logger.debug("storing Patient ID " + pid + " to " + getPidFile(pid));
        if (patientIdExists(pid)) return;
        getPidFile(pid).text = pid.asString()
    }

    boolean patientIdExists(Pid pid) {
        return getPidFile(pid).exists();
    }

    boolean deletePatientIds(List<Pid> toDelete) {
        boolean ok = true;
        for (Pid pid : toDelete) {
            ok = ok & getPidFile(pid).delete();
        }
        return ok;
    }
}
