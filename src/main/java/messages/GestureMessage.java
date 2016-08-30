package messages;

import edu.ufl.digitalworlds.j4k.Skeleton;
import java.io.Serializable;
import kinector.fsm.GestureRecognizerFSM;
import messages.HelperEnums.Hand;

/**
 * Created by johan on 20.03.2016.
 */
public class GestureMessage implements Serializable {
    public final GestureRecognizerFSM.Gesture gesture;
    public final Skeleton skeleton;
    public final Hand hand;

    public GestureMessage(GestureRecognizerFSM.Gesture gesture, Skeleton skeleton, Hand hand) {
        this.gesture = gesture;
        this.skeleton = skeleton;
        this.hand = hand;
    }
}
