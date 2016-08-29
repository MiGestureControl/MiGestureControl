package deviceManagement.models;

/**
 * Created by hagen on 20/05/16.
 */
public class ActivSets {

    public ActivSet activSetTurnOffGesture = new ActivSet();
    public ActivSet activSetTurnOnGesture = new ActivSet();

    public ActivSets(ActivSet turnOffGestureSet, ActivSet turnOnGestureSet) {
        this.activSetTurnOffGesture = turnOffGestureSet;
        this.activSetTurnOnGesture = turnOnGestureSet;
    }

    public ActivSets() {
    }

    @Override
    public String toString() {
        return "ActivSets{" +
                "activSetTurnOffGesture=" + activSetTurnOffGesture +
                ", activSetTurnOnGesture=" + activSetTurnOnGesture +
                '}';
    }
}
