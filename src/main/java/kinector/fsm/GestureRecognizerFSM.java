package kinector.fsm;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;
import messages.GestureMessage;
import messages.HelperEnums.Hand;
import messages.SingleSkeletonMessage;
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

        LEFT_CONTROLLING,
        RIGHT_CONTROLLING,
        
        DEVICE_ON,
        DEVICE_OFF,

        ALL_DEVICES_ON,
        ALL_DEVICES_OFF,
        
        PLAY_SOUND,
    }

    private ActorRef messageTarget;
    private Skeleton[] skeletons;
    private State[] currentState;
    //private List<ActorRef> gestureStateMachines;

    private ActorRef fsmPointing;
    private ActorRef fsmRaising;
    private ActorRef fsmCrossing;
    private ActorRef fsmFlashing;

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
            case DEVICE_ON:
            {
                // send device on message
                Hand pointingHand = current == State.LEFT_CONTROLLING ? Hand.RIGHT : Hand.LEFT;
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.ActivateDevice,
                        skeletons[playerID], pointingHand), ActorRef.noSender());
                next = State.IDLE;
                break;
            }
            
            case DEVICE_OFF:
            {
                // send device on message
                Hand pointingHand = current == State.LEFT_CONTROLLING ? Hand.RIGHT : Hand.LEFT;
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.DeactivateDevice,
                        skeletons[playerID], pointingHand), ActorRef.noSender());
                next = State.IDLE;
                break;
            }

            case ALL_DEVICES_ON:
            {
                // send all devices on message
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.BothHands_ActivateAll,
                        skeletons[playerID], Hand.BOTH), ActorRef.noSender());
                break;
            }

            case ALL_DEVICES_OFF:
            {
                // send all devices off message
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.BothHands_DeactivateAll,
                        skeletons[playerID], Hand.BOTH), ActorRef.noSender());
                break;
            }

            case PLAY_SOUND:
            {
                // send flash message
                messageTarget.tell(new GestureMessage(GestureRecognizer.Gesture.StretchedUp,
                        skeletons[playerID], Hand.UNKNOWN), ActorRef.noSender());
                break;
            }
        }
        return next;
    }

    private void registerGestureStateMachines()
    {
        //gestureStateMachines = new ArrayList<>();
        fsmPointing = (createGestureStateMachine(GestureRecognizerFSMHandPointing.class));
        fsmRaising = (createGestureStateMachine(GestureRecognizerFSMHandRaising.class));
        fsmCrossing = (createGestureStateMachine(GestureRecognizerFSMHandsCrossing.class));
        fsmFlashing = (createGestureStateMachine(GestureRecognizerFSMHandFlashing.class));
        // TODO: more to come...
    }

    /***
     * Creates an actor reference for the given state machine class
     * @param stateMachineClass State machine class to create an instance from
     * @return Actor reference representing the FSM
     */
    private ActorRef createGestureStateMachine(Class<? extends AbstractGestureRecognizerFSM> stateMachineClass) {
        return getContext().system().actorOf(Props.create(stateMachineClass));
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
                if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.Pointing)
                    setState(skeletonID, State.RIGHT_CONTROLLING);
                else if(message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.Pointing)
                    setState(skeletonID, State.LEFT_CONTROLLING);
                else if(message.affectedHand == Hand.BOTH && message.gesture == GestureRecognizer.Gesture.BothHands_ActivateAll)
                    setState(skeletonID, State.ALL_DEVICES_ON);
                else if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.StretchedUp)
                    setState(skeletonID, State.PLAY_SOUND);
                else if(message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.StretchedUp)
                    setState(skeletonID, State.PLAY_SOUND);
                break;
            }

            case LEFT_CONTROLLING:
            {
                if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.ActivateDevice)
                    setState(skeletonID, State.DEVICE_ON);
                else if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.DeactivateDevice)
                    setState(skeletonID, State.DEVICE_OFF);
                else if(message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.StretchedUp)
                    setState(skeletonID, State.PLAY_SOUND);
                break;
            }

            case RIGHT_CONTROLLING:
            {
                if (message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.ActivateDevice)
                    setState(skeletonID, State.DEVICE_ON);
                else if (message.affectedHand == Hand.RIGHT && message.gesture == GestureRecognizer.Gesture.DeactivateDevice)
                    setState(skeletonID, State.DEVICE_OFF);
                else if(message.affectedHand == Hand.LEFT && message.gesture == GestureRecognizer.Gesture.StretchedUp)
                    setState(skeletonID, State.PLAY_SOUND);
                break;
            }

            case ALL_DEVICES_ON:
            {
                if(message.affectedHand == Hand.BOTH && message.gesture == GestureRecognizer.Gesture.BothHands_DeactivateAll)
                    setState(skeletonID, State.ALL_DEVICES_OFF);
                if(message.affectedHand == Hand.BOTH && message.gesture == GestureRecognizer.Gesture.None)
                    setState(skeletonID, State.IDLE);
                break;
            }

            case ALL_DEVICES_OFF:
            {
                if(message.affectedHand == Hand.BOTH && message.gesture == GestureRecognizer.Gesture.BothHands_ActivateAll)
                    setState(skeletonID, State.ALL_DEVICES_ON);
                if(message.affectedHand == Hand.BOTH && message.gesture == GestureRecognizer.Gesture.None)
                    setState(skeletonID, State.IDLE);
                break;
            }
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof SingleSkeletonMessage) {
            Skeleton skeleton = ((SingleSkeletonMessage)message).skeleton;
            State currentState = getState(skeleton.getPlayerID());

            // pass skeleton message to child state machine
            switch(currentState)
            {
                case IDLE:
                {
                    fsmFlashing.tell(message, getSelf());
                    fsmCrossing.tell(message, getSelf());
                    fsmPointing.tell(message, getSelf());
                    break;
                }
                case LEFT_CONTROLLING:
                case RIGHT_CONTROLLING:
                {
                    ((SingleSkeletonMessage)message).hand = currentState == State.LEFT_CONTROLLING ? Hand.LEFT : Hand.RIGHT;
                    fsmFlashing.tell(message, getSelf());
                    fsmRaising.tell(message, getSelf());
                    break;
                }
                case ALL_DEVICES_ON:
                case ALL_DEVICES_OFF:
                {
                    fsmCrossing.tell(message, getSelf());
                    break;
                }
            }

        }
        else if(message instanceof SkeletonStateMessage) {
            handleSkeletonStateMessage((SkeletonStateMessage)message);
        }

        unhandled(message);
    }
}
