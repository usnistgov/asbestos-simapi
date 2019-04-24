package gov.nist.asbestos.simapi.tk.simCommon


import groovy.transform.TypeChecked

/**
 *
 */
@TypeChecked
class TransactionInstance implements Serializable {
    String simId = null;
    String messageId = null;   // message id
    String labelInterpretedAsDate = null;
    String trans = null;    // transaction type code
    gov.nist.asbestos.simapi.tk.actors.TransactionType nameInterpretedAsTransactionType = null;
    gov.nist.asbestos.simapi.tk.actors.ActorType actorType = null;
    String ipAddress;
    boolean isPif = false;

    TransactionInstance() {}

    String toString() {
        return labelInterpretedAsDate + " " + nameInterpretedAsTransactionType + " " + ipAddress;
    }

    TransactionInstance chooseFromList(String label, List<TransactionInstance> instances) {
        String[] parts = label.split(" ");
        if (parts.length != 3) return null;
        String date = parts[0];
        String tt = parts[1];
        String ip = parts[2];

        for (TransactionInstance ti : instances) {
            if (date.equals(ti.labelInterpretedAsDate)) return ti;
        }

        // could select on the other parts - is there a need?
        return null;
    }

    String getTransactionTypeName() { return trans; }
    gov.nist.asbestos.simapi.tk.actors.ActorType getActorType() { return actorType; }

    void setActorType(gov.nist.asbestos.simapi.tk.actors.ActorType actorType) {
        this.actorType = actorType;
    }

    static TransactionInstance copy(TransactionInstance src) {
        TransactionInstance ti = new TransactionInstance();
        ti.simId = src.simId;
        ti.messageId = src.messageId;
        ti.labelInterpretedAsDate = src.labelInterpretedAsDate;
        ti.trans = src.trans;
        ti.nameInterpretedAsTransactionType = src.nameInterpretedAsTransactionType;
        ti.actorType = src.actorType;
        ti.ipAddress = src.ipAddress;
        return ti;
    }
}
