package deviceManagement.models;

/**
 * Created by hagen on 20/05/16.
 */
public class ActivSets {

    public ActivSet activSetTrunOffGesture = new ActivSet();
    public ActivSet activSetTrunOnGesture = new ActivSet();

    public ActivSets(ActivSet trunOffGestureSet, ActivSet trunOnGestureSet) {
        this.activSetTrunOffGesture = trunOffGestureSet;
        this.activSetTrunOnGesture = trunOnGestureSet;
    }

    public ActivSets() {
    }

    @Override
    public String toString() {
        return "ActivSets{" +
                "activSetTrunOffGesture=" + activSetTrunOffGesture +
                ", activSetTrunOnGesture=" + activSetTrunOnGesture +
                '}';
    }
}
