package kinector.fsm;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import kinector.GestureInterpreter;
import messages.GestureMessage;
import messages.SkeletonMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 23.08.2016.
 */
public class GestureRecognizerFSM extends UntypedActor {

    private ActorRef messageTarget;
    private List<ActorRef> gestureStateMachines;

    public GestureRecognizerFSM(ActorRef _messageTarget)
    {
        messageTarget = _messageTarget;
        RegisterGestureStateMachines();
    }

    private void RegisterGestureStateMachines()
    {
        gestureStateMachines = new ArrayList<>();
        gestureStateMachines.add(CreateGestureStateMachine(GestureRecognizerFSMActivateAll.class));
        // TODO: more to come...
    }

    /***
     * Creates an actor reference for the given state machine class
     * @param stateMachineClass State machine class to create an instance from
     * @return Actor reference representing the FSM
     */
    private ActorRef CreateGestureStateMachine(Class<? extends AbstractGestureRecognizerFSM> stateMachineClass) {
        return getContext().system().actorOf(Props.create(stateMachineClass, getSelf()));
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof SkeletonMessage) {
            // pass skeleton message to all child state machines
            for (ActorRef fsmRef : gestureStateMachines) {
                fsmRef.tell(message, ActorRef.noSender());
            }
        }
        else if(message instanceof GestureMessage) {
            // TODO: handle gestures, for now we simply pass them to the real message target
            messageTarget.tell(message, ActorRef.noSender());
        }

        unhandled(message);
    }
}
