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
    protected void handleSkeletons(List<Skeleton> skeletons) {
        leftHandStates = handleHandStates(skeletons, leftHandStates, Skeleton.HAND_LEFT);
        rightHandStates = handleHandStates(skeletons, rightHandStates, Skeleton.HAND_RIGHT);
    }
    
    private State[] handleHandStates(List<Skeleton> skeletons, State[] currentStates, int bone) {
        for(Skeleton skeleton : skeletons) {
            int i = skeleton.getPlayerID();
            switch(currentStates[i])
            {
                case IDLE:
                {
                    if(isBoneLowerThanHead(skeleton, bone))
                        currentStates[i] = State.LOWER_THAN_HEAD;
                    else if(isBoneHigherThanHead(skeleton, bone))
                        currentStates[i] = State.HIGHER_THAN_HEAD;
                    break;
                }
                case LOWER_THAN_HEAD:
                {
                    if(isBoneHigherThanHead(skeleton, bone))
                        currentStates[i] = State.HIGHER_THAN_HEAD_RISING;
                    break;
                }
                case LOWER_THAN_HEAD_SINKING:
                {
                    if(isBoneLowerThanHead(skeleton, bone))
                        currentStates[i] = State.LOWER_THAN_HEAD;
                    else if(isBoneHigherThanHead(skeleton, bone))
                        currentStates[i] = State.HIGHER_THAN_HEAD_RISING;
                    break;
                }
                case HIGHER_THAN_HEAD:
                {
                    if(isBoneLowerThanHead(skeleton, bone))
                        currentStates[i] = State.LOWER_THAN_HEAD_SINKING;
                    break;
                }
                case HIGHER_THAN_HEAD_RISING:
                {
                    if(isBoneHigherThanHead(skeleton, bone))
                        currentStates[i] = State.HIGHER_THAN_HEAD;
                    else if(isBoneLowerThanHead(skeleton, bone))
                        currentStates[i] = State.LOWER_THAN_HEAD_SINKING;
                    break;
                }
            }
            
            if(currentStates[i] == State.HIGHER_THAN_HEAD_RISING) {
                getSender().tell(new SkeletonStateMessage(skeleton, 
                        bone == Skeleton.HAND_LEFT ? Hand.LEFT : Hand.RIGHT, 
                        GestureRecognizer.Gesture.ActivateDevice), ActorRef.noSender());
            }
            else if(currentStates[i] == State.LOWER_THAN_HEAD_SINKING) {
                currentStates[i] = State.LOWER_THAN_HEAD;
                getSender().tell(new SkeletonStateMessage(skeleton, 
                        bone == Skeleton.HAND_LEFT ? Hand.LEFT : Hand.RIGHT, 
                        GestureRecognizer.Gesture.DeactivateDevice), ActorRef.noSender());
            }
        }
        return currentStates;
    }
    
    private boolean isBoneLowerThanHead(Skeleton skeleton, int bone) {
        return skeleton.get3DJointY(bone) < skeleton.get3DJointY(Skeleton.HEAD);
    }
    
    private boolean isBoneHigherThanHead(Skeleton skeleton, int bone) {
        return skeleton.get3DJointY(bone) > skeleton.get3DJointY(Skeleton.HEAD);
    }
}
