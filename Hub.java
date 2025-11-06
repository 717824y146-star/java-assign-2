package model;

public class Hub {
    private final String id;
    private String name;
    private String location;

    public Hub(String id, String name, String location){
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public String getId(){ return id; }
    public String getName(){ return name; }
    public String getLocation(){ return location; }

    @Override public String toString(){
        return String.format("Hub[%s] %s (%s)", id, name, location);
    }
}