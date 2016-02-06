package models;

/**
 * Created by hagen on 06/02/16.
 */
public class Device {

    private String Name;
    private String PossibleSets;
    private String PossibleAttrs;
    private Internals Internals;

    @Override
    public String toString() {
        return "Device{" +
                "Name='" + Name + '\'' +
                ", PossibleSets='" + PossibleSets + '\'' +
                ", PossibleAttrs='" + PossibleAttrs + '\'' +
                ", Internals=" + Internals +
                "}\n";
    }
}
