package kinector.fsm;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;
import messages.GestureMessage;
import messages.HelperEnums.Hand;
import messages.SkeletonMessage;
import messages.SkeletonStateMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 23.08.2016.
 */
public class GestureRecognizerFSM extends UntypedActor {

    private enum State
    {
        IDLE,
        LISTEN,

        LEFT_POINTING,
        LEFT_CONTROLLING_ON,
        LEFT_CONTROLLING_OFF,

        RIGHT_POINTING,
        RIGHT_CONTROLLING_ON,
        RIGHT_CONTROLLING_OFF,

        FLASH,
        CROSSED,
        SIDEWARD_OUTSIDE,
    }

    private ActorRef messageTarget;
    private Skeleton[] skeletons;
    private State[] currentState;
    private List<ActorRef> gestureStateMachines;

    public GestureRecognizerFSM(ActorRef _messageTarget)
    {
        messageTarget = _messageTarget;
        skeletons = new Skeleton[7];
        currentState = new State[] {
                State.IDLE, State.IDLE, State.IDLE, State.IDLE,
                State.IDLE, State.IDLE, State.IDLE,
        };

        registerGestureStateMachines();
    }

    private void setState(int playerID, State state)
    {
        if(currentState[playerID] != state) {
            currentState[playerID] = transition(playerID, currentState[playerID], state);
        }
    }

    private State getState(int playerID) {
        return currentState[playerID];
    }

    private State transition(int playerID, State current, State next) {
        switch(next)
        {
            case RIGHT_CONTROLLING_ON:
            case LEFT_CONTROLLING_ON:
            {
                // send device on message
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.Pointing_ActivateDevice,
                        skeletons[playerID]), ActorRef.noSender());
                next = next == State.RIGHT_CONTROLLING_ON ? State.LEFT_POINTING : State.RIGHT_POINTING;
                break;
            }

            case RIGHT_CONTROLLING_OFF:
            case LEFT_CONTROLLING_OFF:
            {
                // send device off message
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.Pointing_DeactiveDevice,
                        skeletons[playerID]), ActorRef.noSender());
                next = next == State.RIGHT_CONTROLLING_OFF ? State.LEFT_POINTING : State.RIGHT_POINTING;
                break;
            }

            case CROSSED:
            {
                // send all devices on message
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.BothHands_ActivateAll,
                        skeletons[playerID]), ActorRef.noSender());
                next = State.IDLE;
                break;
            }

            case SIDEWARD_OUTSIDE:
            {
                // send all devices off message
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.BothHands_DeactivateAll,
                        skeletons[playerID]), ActorRef.noSender());
                next = State.IDLE;
                break;
            }

            case FLASH:
            {
                // send flash message
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.StrechtingUp,
                        skeletons[playerID]), ActorRef.noSender());
                next = State.IDLE;
                break;
            }
        }

        return next;
    }

    private void registerGestureStateMachines()
    {
        gestureStateMachines = new ArrayList<>();
        gestureStateMachines.add(createGestureStateMachine(GestureRecognizerFSMLeftHandPointing.class));
        // TODO: more to come...
    }

    /***
     * Creates an actor reference for the given state machine class
     * @param stateMachineClass State machine class to create an instance from
     * @return Actor reference representing the FSM
     */
    private ActorRef createGestureStateMachine(Class<? extends AbstractGestureRecognizerFSM> stateMachineClass) {
        return getContext().system().actorOf(Props.create(stateMachineClass, getSelf()));
    }

    /***
     * Handles a skeleton message from a child state machine
     * TODO: for ongoing development, make this configurable (not hard coded)
     * @param message
     */
    private void handleSkeletonStateMessage(SkeletonStateMessage message) {
        int skeletonID = message.skeleton.getPlayerID();
        skeletons[skeletonID] = message.skeleton;
        State currentState = getState(skeletonID);

        switch(currentState) {
            case IDLE:
            {
                if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.Pointing_NoAction)
                    setState(skeletonID, State.LEFT_POINTING);
                else if(message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.Pointing_NoAction)
                    setState(skeletonID, State.RIGHT_POINTING);
                else if(message.affectedHand == Hand.BOTH && message.gesture == GestureRecognizer.Gesture.Crossing)
                    setState(skeletonID, State.CROSSED);
                else if(message.affectedHand == Hand.BOTH && message.gesture == GestureRecognizer.Gesture.MovingSidewardOutside)
                    setState(skeletonID, State.SIDEWARD_OUTSIDE);
                else if(message.affectedHand != Hand.BOTH && message.gesture == GestureRecognizer.Gesture.StrechtingUp)
                    setState(skeletonID, State.FLASH);
                break;
            }

            case LEFT_POINTING:
            {
                if(message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.RaisingUp)
                    setState(skeletonID, State.RIGHT_CONTROLLING_ON);
                else if(message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.RaisingDown)
                    setState(skeletonID, State.RIGHT_CONTROLLING_OFF);
            }

            case RIGHT_POINTING:
            {
                if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.RaisingUp)
                    setState(skeletonID, State.LEFT_CONTROLLING_ON);
                else if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.RaisingDown)
                    setState(skeletonID, State.LEFT_CONTROLLING_OFF);
            }
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof SkeletonMessage) {
            // pass skeleton message to all child state machines
            for (ActorRef fsmRef : gestureStateMachines) {
                fsmRef.tell(message, ActorRef.noSender());
            }
        }
        else if(message instanceof SkeletonStateMessage) {
            handleSkeletonStateMessage((SkeletonStateMessage)message);
        }

        unhandled(message);
    }
}
