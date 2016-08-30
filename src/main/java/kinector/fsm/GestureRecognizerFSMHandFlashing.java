package kinector.fsm;

import akka.actor.ActorRef;
import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;
import messages.HelperEnums.Hand;
import messages.SkeletonStateMessage;

/**
 * Created by Norman on 30.08.2016.
 */
public class GestureRecognizerFSMHandFlashing extends AbstractGestureRecognizerFSM {

    @Override
    protected void handleSkeleton(Skeleton skeleton, Hand hand) {
        if (isLeftHandIsAboveHead(skeleton)) {
            //System.out.println("Left hand pointing, dist=" + getZDistanceBetweenHands(skeleton));
            getSender().tell(
                    new SkeletonStateMessage(skeleton, Hand.LEFT, GestureRecognizer.Gesture.StretchedUp),
                    ActorRef.noSender());
        }
        if (isRightHandIsAboveHead(skeleton)) {
            //System.out.println("Left hand pointing, dist=" + getZDistanceBetweenHands(skeleton));
            getSender().tell(
                    new SkeletonStateMessage(skeleton, Hand.RIGHT, GestureRecognizer.Gesture.StretchedUp),
                    ActorRef.noSender());
        }
    }

    private boolean isRightHandIsAboveHead(Skeleton skeleton) {
        return (skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.SHOULDER_RIGHT)) &&
                (skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.ELBOW_RIGHT)) &&
                (skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.HEAD));
    }

    private boolean isLeftHandIsAboveHead(Skeleton skeleton) {
        return (skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.SHOULDER_LEFT)) &&
                (skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.ELBOW_LEFT)) &&
                (skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.HEAD));
    }
}
