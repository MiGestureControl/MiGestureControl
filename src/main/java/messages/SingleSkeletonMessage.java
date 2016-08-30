package messages;

import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.HelperEnums.Hand;

import java.util.ArrayList;

/**
 * Created by Norman on 30.08.2016.
 */
public class SingleSkeletonMessage {
    public final Skeleton skeleton;
    public Hand hand;

    public SingleSkeletonMessage(Skeleton skeleton) {
        this.skeleton = skeleton;
        this.hand = Hand.UNKNOWN;
    }

    @Override
    public String toString() {
        return "SingleSkeletonMessage{" +
                "skeleton=" + skeleton +
                '}';
    }
}
