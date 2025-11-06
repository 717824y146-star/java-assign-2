package model;

import java.util.Date;

public class ScanEvent {
    protected final String id;
    protected final String parcelBarcode;
    protected final Hub hub;
    protected final Date timestamp;
    protected final String note;

    public ScanEvent(String id, String parcelBarcode, Hub hub, Date timestamp, String note){
        this.id = id;
        this.parcelBarcode = parcelBarcode;
        this.hub = hub;
        this.timestamp = timestamp;
        this.note = note;
    }

    public String getId(){ return id; }
    public String getParcelBarcode(){ return parcelBarcode; }
    public Hub getHub(){ return hub; }
    public Date getTimestamp(){ return timestamp; }
    public String getNote(){ return note; }

    @Override
    public String toString(){
        return String.format("[%s] ScanEvent@%s - hub:%s note:%s",
                id, timestamp, hub.getName(), note==null?"":note);
    }
}