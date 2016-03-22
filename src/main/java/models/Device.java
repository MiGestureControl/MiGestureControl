package models;

/**
 * Created by hagen on 06/02/16.
 */
public class Device {

    private String id;
    private String name;
    private String possibleSets;
    private String possibleAttrs;
    private Internals internals;
    private double[] devicePoint;

    public boolean deviceOn = false; // nur zum Testen

    public Device(String name, double[] devicePoint){
        this.name = name;
        this.devicePoint = devicePoint;
    }

    public void turnOn()
    {
        // Gerät anschalten
        System.out.println(name + ": On");
        deviceOn = true;
    }

    public void turnOff() {
        System.out.println(name + ": Off");
        deviceOn = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double[] getDevicePoint() {
        return devicePoint;
    }

    @Override
    public String toString() {
        return "Device{" +
                "Name='" + name + '\'' +
                ", PossibleSets='" + possibleSets + '\'' +
                ", PossibleAttrs='" + possibleAttrs + '\'' +
                ", Internals=" + internals +
                "}\n";
    }
}
