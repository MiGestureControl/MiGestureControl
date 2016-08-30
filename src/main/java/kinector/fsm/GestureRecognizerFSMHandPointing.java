package kinector.fsm;

import akka.actor.ActorRef;
import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;
import messages.HelperEnums.Hand;
import messages.SkeletonStateMessage;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Marc on 25.08.2016.
 */
public class GestureRecognizerFSMHandPointing extends AbstractGestureRecognizerFSM {

    /**
     * Minmal distance between hands, to avoid spamming messages
     */
    private final float minimumZDistanceBetweenHands = 0.1f;

    @Override
    protected void handleSkeleton(Skeleton skeleton, Hand hand) {
        // left hand is declared as pointing when it is more far away from the
        // body than the right hand (nearer at device)
        if (isLeftHandNearerAtDeviceThanRightHand(skeleton) && areHandsFarEnoughFromEachOther(skeleton)) {
            //System.out.println("Left hand pointing, dist=" + getZDistanceBetweenHands(skeleton));
            getSender().tell(
                    new SkeletonStateMessage(skeleton, Hand.LEFT, GestureRecognizer.Gesture.Pointing),
                    ActorRef.noSender());
        }
        // right hand is declared as pointing when it is more fare away from
        // the body than the left hand (nearer at device)
        if (isRightHandNearerAtDeviceThanLeftHand(skeleton) && areHandsFarEnoughFromEachOther(skeleton)) {
            //System.out.println("Right hand pointing, dist=" + getZDistanceBetweenHands(skeleton));
            getSender().tell(
                    new SkeletonStateMessage(skeleton, Hand.RIGHT, GestureRecognizer.Gesture.Pointing),
                    ActorRef.noSender());
        }
    }

    private boolean isLeftHandNearerAtDeviceThanRightHand(Skeleton skeleton) {
        return skeleton.get3DJointZ(Skeleton.HAND_LEFT) < skeleton.get3DJointZ(Skeleton.HAND_RIGHT) &&
                skeleton.get3DJointY(Skeleton.HAND_LEFT) < skeleton.get3DJointY(Skeleton.HEAD);
    }
    
    private boolean isRightHandNearerAtDeviceThanLeftHand(Skeleton skeleton) {
        return skeleton.get3DJointZ(Skeleton.HAND_RIGHT) < skeleton.get3DJointZ(Skeleton.HAND_LEFT) &&
                skeleton.get3DJointY(Skeleton.HAND_RIGHT) < skeleton.get3DJointY(Skeleton.HEAD);
    }

    private boolean areHandsFarEnoughFromEachOther(Skeleton skeleton) {
        return getZDistanceBetweenHands(skeleton) >= minimumZDistanceBetweenHands;
    }

    private float getZDistanceBetweenHands(Skeleton skeleton) {
        return Math.abs(skeleton.get3DJointZ(Skeleton.HAND_LEFT) - skeleton.get3DJointZ((Skeleton.HAND_RIGHT)));
    }
}
