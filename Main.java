package app;

import model.*;
import enums.*;
import util.IDGenerator;
import java.util.*;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static final Map<String, Customer> customers = new HashMap<>();
    private static final Map<String, Hub> hubs = new HashMap<>();
    private static final Map<String, Parcel> parcels = new HashMap<>();
    private static final Map<String, Shipment> shipments = new HashMap<>();

    public static void main(String[] args){
        seedDemoData();
        boolean running = true;
        while (running){
            printMenu();
            String choice = scanner.nextLine().trim();
            switch (choice){
                case "1": createParcel(); break;
                case "2": createShipment(); break;
                case "3": addHub(); break;
                case "4": recordScan(); break;
                case "5": recordDeliveryAttempt(); break;
                case "6": showParcelStatus(); break;
                case "7": showShipmentSummary(); break;
                case "8": closeShipment(); break;
                case "9": running = false; break;
                default: System.out.println("Invalid option.");
            }
        }
        System.out.println("Exiting. Goodbye!");
    }

    private static void printMenu(){
        System.out.println("\n--- Logistics Console ---");
        System.out.println("1. Create Parcel");
        System.out.println("2. Create Shipment");
        System.out.println("3. Add Hub");
        System.out.println("4. Record Scan");
        System.out.println("5. Record Delivery Attempt");
        System.out.println("6. Show Parcel Status");
        System.out.println("7. Show Shipment Summary");
        System.out.println("8. Close Shipment");
        System.out.println("9. Exit");
        System.out.print("Choose: ");
    }

    private static void createParcel(){
        String id = IDGenerator.next("P");
        System.out.print("Weight (kg): ");
        double w = Double.parseDouble(scanner.nextLine().trim());
        Parcel p = new Parcel(id, w);
        parcels.put(id, p);
        System.out.println("Created " + p);
    }

    private static void createShipment(){
        String sid = IDGenerator.next("S");
        System.out.print("Sender name: ");
        String sname = scanner.nextLine().trim();
        System.out.print("Sender address: ");
        String saddr = scanner.nextLine().trim();
        Customer sender = new Customer(IDGenerator.next("C"), sname, saddr);
        customers.put(sender.getId(), sender);

        System.out.print("Receiver name: ");
        String rname = scanner.nextLine().trim();
        System.out.print("Receiver address: ");
        String raddr = scanner.nextLine().trim();
        Customer receiver = new Customer(IDGenerator.next("C"), rname, raddr);
        customers.put(receiver.getId(), receiver);

        Shipment sh = new Shipment(sid, sender, receiver);

        System.out.println("Add parcels (barcodes, comma-separated or blank): ");
        String line = scanner.nextLine().trim();
        if (!line.isEmpty()){
            for (String c : line.split(",")){
                Parcel p = parcels.get(c.trim());
                if (p != null) sh.addParcel(p);
                else System.out.println("Parcel not found: " + c.trim());
            }
        }
        System.out.println("Add hubs (ids, comma-separated or blank): ");
        line = scanner.nextLine().trim();
        if (!line.isEmpty()){
            for (String h : line.split(",")){
                Hub hub = hubs.get(h.trim());
                if (hub != null) sh.addHubToRoute(hub);
                else System.out.println("Hub not found: " + h.trim());
            }
        }
        shipments.put(sid, sh);
        System.out.println("Created shipment: " + sid);
    }

    private static void addHub(){
        String id = IDGenerator.next("H");
        System.out.print("Hub name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Location: ");
        String loc = scanner.nextLine().trim();
        Hub h = new Hub(id, name, loc);
        hubs.put(id, h);
        System.out.println("Added " + h);
    }

    private static void recordScan(){
        System.out.print("Parcel barcode: ");
        String pcode = scanner.nextLine().trim();
        Parcel p = parcels.get(pcode);
        if (p == null){ System.out.println("Parcel not found."); return; }

        System.out.print("Hub id: ");
        String hid = scanner.nextLine().trim();
        Hub h = hubs.get(hid);
        if (h == null){ System.out.println("Hub not found."); return; }

        System.out.print("Note (optional): ");
        String note = scanner.nextLine().trim();

        String sid = IDGenerator.next("SE");
        ScanEvent se = new ScanEvent(sid, pcode, h, new Date(), note);
        p.addScan(se);
        System.out.println("Recorded scan: " + se);
    }

    private static void recordDeliveryAttempt(){
        System.out.print("Parcel barcode: ");
        String pcode = scanner.nextLine().trim();
        Parcel p = parcels.get(pcode);
        if (p == null){ System.out.println("Parcel not found."); return; }

        System.out.print("Hub id: ");
        String hid = scanner.nextLine().trim();
        Hub h = hubs.get(hid);
        if (h == null){ System.out.println("Hub not found."); return; }

        System.out.print("Attempted by (driver): ");
        String by = scanner.nextLine().trim();

        System.out.print("Outcome (SUCCESS / FAILED): ");
        String outcomeStr = scanner.nextLine().trim();
        DeliveryOutcome outcome = DeliveryOutcome.FAILED;
        try { outcome = DeliveryOutcome.valueOf(outcomeStr.toUpperCase()); }
        catch (Exception e){ System.out.println("Invalid outcome, defaulting to FAILED."); }

        System.out.print("Reason (optional): ");
        String reason = scanner.nextLine().trim();

        String sid = IDGenerator.next("DA");
        DeliveryAttempt da = new DeliveryAttempt(sid, pcode, h, new Date(), "delivery attempt", outcome, by, reason);
        p.addScan(da);

        if (outcome == DeliveryOutcome.SUCCESS){
            System.out.print("POD signer name: ");
            String signer = scanner.nextLine().trim();
            System.out.print("POD code/signature: ");
            String code = scanner.nextLine().trim();
            ProofOfDelivery pod = new ProofOfDelivery(signer, code, new Date());
            if (pod.isValid()){
                p.attachProofOfDelivery(pod);
                System.out.println("POD attached: " + pod);
            } else {
                System.out.println("Invalid POD.");
            }
        } else {
            System.out.println("Delivery attempt recorded: " + da);
        }
    }

    private static void showParcelStatus(){
        System.out.print("Parcel barcode: ");
        String pcode = scanner.nextLine().trim();
        Parcel p = parcels.get(pcode);
        if (p == null) { System.out.println("Parcel not found."); return; }
        System.out.println(p.getStatusTimeline());
    }

    private static void showShipmentSummary(){
        System.out.print("Shipment id: ");
        String sid = scanner.nextLine().trim();
        Shipment sh = shipments.get(sid);
        if (sh == null) { System.out.println("Shipment not found."); return; }
        System.out.println(sh.summary());
    }

    private static void closeShipment(){
        System.out.print("Shipment id: ");
        String sid = scanner.nextLine().trim();
        Shipment sh = shipments.get(sid);
        if (sh == null) { System.out.println("Shipment not found."); return; }
        if (sh.close()){
            System.out.println("Shipment closed.");
        } else {
            System.out.println("Shipment cannot be closed. Ensure all parcels are DELIVERED or RETURNED.");
        }
    }

    private static void seedDemoData(){
        Hub h1 = new Hub(IDGenerator.next("H"), "Origin Hub", "City A");
        Hub h2 = new Hub(IDGenerator.next("H"), "Sortation Hub", "City B");
        Hub h3 = new Hub(IDGenerator.next("H"), "Local Hub", "City C");
        hubs.put(h1.getId(), h1); hubs.put(h2.getId(), h2); hubs.put(h3.getId(), h3);

        Parcel p1 = new Parcel(IDGenerator.next("P"), 2.5);
        Parcel p2 = new Parcel(IDGenerator.next("P"), 1.1);
        parcels.put(p1.getBarcode(), p1); parcels.put(p2.getBarcode(), p2);

        Customer s = new Customer(IDGenerator.next("C"), "Alice", "12 Sender St");
        Customer r = new Customer(IDGenerator.next("C"), "Bob", "99 Receiver Ave");
        customers.put(s.getId(), s); customers.put(r.getId(), r);

        Shipment sh = new Shipment(IDGenerator.next("S"), s, r);
        sh.addParcel(p1); sh.addParcel(p2);
        sh.addHubToRoute(h1); sh.addHubToRoute(h2); sh.addHubToRoute(h3);
        shipments.put(sh.getShipmentId(), sh);

        System.out.println("Demo Shipment id: " + sh.getShipmentId()
                + " | Example Parcels: " + p1.getBarcode() + ", " + p2.getBarcode());
    }
}