package gov.nist.asbestos.simapi.toolkit.configDatatypes.client;

import java.io.Serializable;

/**
 *
 */
public class PatientError implements Serializable {
    Pid patientId;
    String errorCode;
    private static final long serialVersionUID = 1L;

    public PatientError() {}

    public Pid getPatientId() {
        return patientId;
    }

    public void setPatientId(Pid patientId) {
        this.patientId = patientId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return patientId.asString() + " ==> " + errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatientError that = (PatientError) o;

        if (!patientId.equals(that.patientId)) return false;
        return errorCode.equals(that.errorCode);

    }

    @Override
    public int hashCode() {
        int result = patientId.hashCode();
        result = 31 * result + errorCode.hashCode();
        return result;
    }
}
