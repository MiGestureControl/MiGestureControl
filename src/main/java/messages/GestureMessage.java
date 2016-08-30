package messages;

import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;

import java.io.Serializable;
import messages.HelperEnums.Hand;

/**
 * Created by johan on 20.03.2016.
 */
public class GestureMessage implements Serializable {
    public final GestureRecognizer.Gesture gesture;
    public final Skeleton skeleton;
    public final Hand hand;

    public GestureMessage(GestureRecognizer.Gesture gesture, Skeleton skeleton, Hand hand) {
        this.gesture = gesture;
        this.skeleton = skeleton;
        this.hand = hand;
    }
}
