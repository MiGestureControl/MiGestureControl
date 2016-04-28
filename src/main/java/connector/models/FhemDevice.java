package connector.models;

/**
 * Klasse dient zum kapseln der Eigenschaften eines Geräts
 */
public class FhemDevice {

    private String Name;
    private String PossibleSets;
    private String PossibleAttrs;
    private Internals Internals;

    public String getName() {
        return Name;
    }

    public String getPossibleSets() {
        return PossibleSets;
    }

    public String getPossibleAttrs() {
        return PossibleAttrs;
    }

    public connector.models.Internals getInternals() {
        return Internals;
    }


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
