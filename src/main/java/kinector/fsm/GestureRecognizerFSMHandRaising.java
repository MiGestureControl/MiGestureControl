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
public class GestureRecognizerFSMHandRaising extends AbstractGestureRecognizerFSM {

    private enum State
    {
        IDLE,
        LOWER_THAN_HEAD,
        LOWER_THAN_HEAD_SINKING,
        HIGHER_THAN_HEAD,
        HIGHER_THAN_HEAD_RISING,
    }
    
    private State[] leftHandStates = {
        State.IDLE, State.IDLE, State.IDLE, State.IDLE,
        State.IDLE, State.IDLE, State.IDLE
    };
    
    private State[] rightHandStates = {
        State.IDLE, State.IDLE, State.IDLE, State.IDLE,
        State.IDLE, State.IDLE, State.IDLE
    };
    
    @Override
    protected void handleSkeleton(Skeleton skeleton, Hand hand) {
        int id = skeleton.getPlayerID();
        if(hand == Hand.LEFT)
            leftHandStates[id] = handleHandState(skeleton, leftHandStates[id], Skeleton.HAND_LEFT);
        else if(hand == Hand.RIGHT)
            rightHandStates[id] = handleHandState(skeleton, rightHandStates[id], Skeleton.HAND_RIGHT);
    }
    
    private State handleHandState(Skeleton skeleton, State currentState, int bone) {
        int i = skeleton.getPlayerID();
        switch(currentState)
        {
            case IDLE:
            {
                if(isBoneLowerThanHead(skeleton, bone))
                    currentState = State.LOWER_THAN_HEAD;
                else if(isBoneHigherThanHead(skeleton, bone))
                    currentState = State.HIGHER_THAN_HEAD;
                break;
            }
            case LOWER_THAN_HEAD:
            {
                if(isBoneHigherThanHead(skeleton, bone))
                    currentState = State.HIGHER_THAN_HEAD_RISING;
                break;
            }
            case LOWER_THAN_HEAD_SINKING:
            {
                if(isBoneLowerThanHead(skeleton, bone))
                    currentState = State.LOWER_THAN_HEAD;
                else if(isBoneHigherThanHead(skeleton, bone))
                    currentState = State.HIGHER_THAN_HEAD_RISING;
                break;
            }
            case HIGHER_THAN_HEAD:
            {
                if(isBoneLowerThanHead(skeleton, bone))
                    currentState = State.LOWER_THAN_HEAD_SINKING;
                break;
            }
            case HIGHER_THAN_HEAD_RISING:
            {
                if(isBoneHigherThanHead(skeleton, bone))
                    currentState = State.HIGHER_THAN_HEAD;
                else if(isBoneLowerThanHead(skeleton, bone))
                    currentState = State.LOWER_THAN_HEAD_SINKING;
                break;
            }
        }

        if(currentState == State.HIGHER_THAN_HEAD_RISING) {
            getSender().tell(new SkeletonStateMessage(skeleton,
                    bone == Skeleton.HAND_LEFT ? Hand.LEFT : Hand.RIGHT,
                    GestureRecognizer.Gesture.ActivateDevice), ActorRef.noSender());
        }
        else if(currentState == State.LOWER_THAN_HEAD_SINKING) {
            currentState = State.LOWER_THAN_HEAD;
            getSender().tell(new SkeletonStateMessage(skeleton,
                    bone == Skeleton.HAND_LEFT ? Hand.LEFT : Hand.RIGHT,
                    GestureRecognizer.Gesture.DeactivateDevice), ActorRef.noSender());
        }
        return currentState;
    }
    
    private boolean isBoneLowerThanHead(Skeleton skeleton, int bone) {
        return skeleton.get3DJointY(bone) < skeleton.get3DJointY(Skeleton.HEAD);
    }
    
    private boolean isBoneHigherThanHead(Skeleton skeleton, int bone) {
        return skeleton.get3DJointY(bone) > skeleton.get3DJointY(Skeleton.HEAD);
    }
}
