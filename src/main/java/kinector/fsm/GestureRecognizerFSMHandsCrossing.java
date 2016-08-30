package kinector.fsm;

import akka.actor.ActorRef;
import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;
import messages.HelperEnums.Hand;
import messages.SkeletonStateMessage;

/**
 * Created by Norman on 30.08.2016.
 */
public class GestureRecognizerFSMHandsCrossing extends AbstractGestureRecognizerFSM {

    private final double minimumXDistanceBetweenHands = 0.3f;

    private enum State
    {
        IDLE,
        CROSSED,
        UNCROSSED,
        TOWARDS_CROSSED,
        TOWARDS_UNCROSSED,
    }

    private State[] states = {
            State.IDLE, State.IDLE, State.IDLE, State.IDLE,
            State.IDLE, State.IDLE, State.IDLE
    };

    @Override
    protected void handleSkeleton(Skeleton skeleton, Hand hand) {
        int id = skeleton.getPlayerID();
        State currentState = states[id];
        State newState = states[id];

        if(!areBothHandsAboveHead(skeleton)) {
            newState = State.IDLE;
        } else {
            switch (currentState) {
                case IDLE:
                {
                    if (areBothHandsCross(skeleton))
                        newState = State.CROSSED;
                    break;
                }
                case CROSSED:
                {
                    if(!areBothHandsCross(skeleton)) // && areBothHandsFarAwayFromEachOther(skeleton))
                        newState = State.UNCROSSED;
                    break;
                }
                case UNCROSSED:
                {
                    if(areBothHandsCross(skeleton))
                        newState = State.CROSSED;
                    break;
                }
            }
        }

        states[id] = newState;
        if(newState != currentState) {
            if (newState == State.CROSSED) {
                getSender().tell(new SkeletonStateMessage(skeleton,
                        Hand.BOTH,
                        GestureRecognizer.Gesture.BothHands_ActivateAll), ActorRef.noSender());
            } else if (newState == State.UNCROSSED) {
                getSender().tell(new SkeletonStateMessage(skeleton,
                        Hand.BOTH,
                        GestureRecognizer.Gesture.BothHands_DeactivateAll), ActorRef.noSender());
            } else if(newState == State.IDLE) {
                getSender().tell(new SkeletonStateMessage(skeleton,
                        Hand.BOTH, GestureRecognizer.Gesture.None), ActorRef.noSender());
            }
        }
    }

    private boolean areBothHandsAboveHead(Skeleton skeleton) {
        return (skeleton.get3DJointY(Skeleton.HAND_RIGHT) >= skeleton.get3DJointY(Skeleton.HEAD)) &&
                (skeleton.get3DJointY(Skeleton.HAND_LEFT) >= skeleton.get3DJointY(Skeleton.HEAD));
    }

    private boolean areBothHandsCross(Skeleton skeleton) {
        return (skeleton.get3DJointX(Skeleton.HAND_RIGHT) <= skeleton.get3DJointX(Skeleton.HAND_LEFT));
    }

    private boolean areBothHandsFarAwayFromEachOther(Skeleton skeleton) {
        return getXDistanceBetweenHands(skeleton) > minimumXDistanceBetweenHands;
    }

    private double getXDistanceBetweenHands(Skeleton skeleton) {
        return Math.abs(skeleton.get3DJointX(Skeleton.HAND_RIGHT) - skeleton.get3DJointX(Skeleton.HAND_LEFT));
    }
}
