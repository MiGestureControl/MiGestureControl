package messages;

import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;
import messages.HelperEnums.Hand;

/**
 * Created by Marc on 25.08.2016.
 */
public class SkeletonStateMessage {

    public Skeleton skeleton;
    public Hand affectedHand;
    public GestureRecognizer.Gesture gesture;

    public SkeletonStateMessage(Skeleton _skeleton, Hand _affectedHand, GestureRecognizer.Gesture _gesture) {
        skeleton = _skeleton;
        affectedHand = _affectedHand;
        gesture = _gesture;
    }

}
