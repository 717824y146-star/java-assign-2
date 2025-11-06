package model;

import enums.DeliveryOutcome;
import java.util.Date;

public class DeliveryAttempt extends ScanEvent {
    private DeliveryOutcome outcome;
    private String attemptedBy;
    private String reason;

    public DeliveryAttempt(String id, String parcelBarcode, Hub hub, Date timestamp, String note,
                           DeliveryOutcome outcome, String attemptedBy, String reason){
        super(id, parcelBarcode, hub, timestamp, note);
        this.outcome = outcome;
        this.attemptedBy = attemptedBy;
        this.reason = reason;
    }

    public DeliveryOutcome getOutcome(){ return outcome; }
    public void setOutcome(DeliveryOutcome outcome){ this.outcome = outcome; }
    public String getAttemptedBy(){ return attemptedBy; }
    public String getReason(){ return reason; }

    @Override
    public String toString(){
        return String.format("[%s] DeliveryAttempt@%s hub:%s outcome:%s by:%s reason:%s note:%s",
                id, timestamp, hub.getName(), outcome, attemptedBy, reason==null?"":reason, note==null?"":note);
    }
}