package model;

import enums.ParcelStatus;
import enums.DeliveryOutcome;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parcel {
    private final String barcode;
    private double weight;
    private ParcelStatus status;
    private Hub currentHub;
    private final List<ScanEvent> scanHistory = new ArrayList<>();
    private DeliveryAttempt deliveryAttempt;
    private ProofOfDelivery proofOfDelivery;

    public Parcel(String barcode, double weight){
        this.barcode = barcode;
        this.weight = weight;
        this.status = ParcelStatus.CREATED;
    }

    public String getBarcode(){ return barcode; }
    public double getWeight(){ return weight; }
    public ParcelStatus getStatus(){ return status; }
    public Hub getCurrentHub(){ return currentHub; }
    public List<ScanEvent> getScanHistory(){ return scanHistory; }
    public DeliveryAttempt getDeliveryAttempt(){ return deliveryAttempt; }
    public ProofOfDelivery getProofOfDelivery(){ return proofOfDelivery; }

    public void addScan(ScanEvent scan){
        scanHistory.add(scan);
        this.currentHub = scan.getHub();
        if (scan instanceof DeliveryAttempt){
            DeliveryAttempt da = (DeliveryAttempt) scan;
            this.deliveryAttempt = da;
            if (da.getOutcome() == DeliveryOutcome.SUCCESS){
                this.status = ParcelStatus.DELIVERED;
            } else {
                this.status = ParcelStatus.OUT_FOR_DELIVERY;
            }
        } else {
            if (this.status == ParcelStatus.CREATED) this.status = ParcelStatus.IN_TRANSIT;
        }
    }

    public void attachProofOfDelivery(ProofOfDelivery pod){
        if (pod != null && pod.isValid()){
            this.proofOfDelivery = pod;
            this.status = ParcelStatus.DELIVERED;
        }
    }

    public void markReturned(String reason, ScanEvent scan){
        addScan(scan);
        this.status = ParcelStatus.RETURNED;
    }

    public String getStatusTimeline(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Parcel %s status=%s\n", barcode, status));
        for (ScanEvent s : scanHistory){
            sb.append("  - ").append(s.toString()).append("\n");
        }
        if (proofOfDelivery != null){
            sb.append("  POD: ").append(proofOfDelivery.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override public String toString(){
        return String.format("Parcel[%s] wt=%.2f status=%s hub=%s",
                barcode, weight, status, currentHub == null ? "N/A" : currentHub.getName());
    }
}