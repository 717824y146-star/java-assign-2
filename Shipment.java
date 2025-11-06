package model;

import enums.ParcelStatus;
import java.util.ArrayList;
import java.util.List;

public class Shipment {
    private final String shipmentId;
    private final Customer sender;
    private final Customer receiver;
    private final List<Parcel> parcels = new ArrayList<>();
    private final List<Hub> route = new ArrayList<>();
    private boolean closed = false;

    public Shipment(String shipmentId, Customer sender, Customer receiver){
        this.shipmentId = shipmentId;
        this.sender = sender;
        this.receiver = receiver;
    }

    public String getShipmentId(){ return shipmentId; }
    public List<Parcel> getParcels(){ return parcels; }
    public List<Hub> getRoute(){ return route; }
    public boolean isClosed(){ return closed; }

    public void addParcel(Parcel p){ if (!closed) parcels.add(p); }
    public void addHubToRoute(Hub h){ route.add(h); }

    public boolean canClose(){
        for (Parcel p : parcels){
            if (!(p.getStatus() == ParcelStatus.DELIVERED || p.getStatus() == ParcelStatus.RETURNED))
                return false;
        }
        return true;
    }

    public boolean close(){
        if (canClose()){
            closed = true;
            return true;
        }
        return false;
    }

    public String summary(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Shipment %s (closed=%s)\n", shipmentId, closed));
        sb.append(" Sender: ").append(sender).append("\n");
        sb.append(" Receiver: ").append(receiver).append("\n");
        sb.append(" Route: ");
        for (Hub h : route) sb.append(h.getName()).append(" -> ");
        sb.append("\n Parcels:\n");
        for (Parcel p : parcels) sb.append("  ").append(p.toString()).append("\n");
        return sb.toString();
    }

    @Override public String toString(){ return summary(); }
}