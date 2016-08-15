package kinector.fsm;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;
import messages.GestureMessage;
import messages.SkeletonMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marc on 13.08.2016.
 */
public class GestureRecognizerFSMActivateAll extends UntypedActor {

    protected enum State
    {
        IDLE, ABOVE_HEAD, SIDEWARD_OUTSIDE, CROSSED;
    }

    private State state = State.IDLE;
    private ActorRef target;
    private List<Object> queue;
    private Skeleton currentSkeleton;

    protected void init(ActorRef target)
    {
        this.target = target;
        queue = new ArrayList<Object>();
    }

    protected void setState(State state) {
        if(this.state != state)
        {
            transition(this.state, state);
            this.state = state;
        }
    }

    protected void enqueue(Object o)
    {
        if(queue != null)
            queue.add(o);
    }

    protected List<Object> drainQueue()
    {
        final List<Object> q = queue;
        if(q == null)
            throw new IllegalStateException("drainQueue(): not yet initialized");
        queue = new ArrayList<Object>();
        return q;
    }

    protected boolean isInitialized()
    {
        return target != null;
    }

    protected State getState() {
        return state;
    }

    protected ActorRef getTarget() {
        if(target == null)
            throw new IllegalStateException("getTarget(): not yet initialized");
        return target;
    }

    protected void transition(State old, State next)
    {
        if(old == State.CROSSED && next == State.SIDEWARD_OUTSIDE)
        {
            // Von gekreuzten Armen in die Auswärtsbewegung
            target.tell(
                    new GestureMessage(GestureRecognizer.Gesture.BothHands_ActivateAll, currentSkeleton),
                    ActorRef.noSender());
            // reset state machine
            setState(State.IDLE);
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof SkeletonMessage) {
            for (Skeleton skeleton : ((SkeletonMessage) message).skeletons) {
                currentSkeleton = skeleton;

                if(getState() == State.IDLE)
                {
                    // Sobald sich beide Hände oberhalb des Kopfes befinden, Zustandsübergang nach ABOVE_HEAD auslösen
                    if ((skeleton.get3DJointY(Skeleton.HAND_RIGHT) >= skeleton.get3DJointY(Skeleton.HEAD)) &&
                            (skeleton.get3DJointY(Skeleton.HAND_LEFT) >= skeleton.get3DJointY(Skeleton.HEAD))) {
                        setState(State.ABOVE_HEAD);
                    }
                }
                else if(getState() == State.ABOVE_HEAD)
                {
                    // Sobald sich beide Hände überkreuzen, Zustandsübergang nach CROSSED auslösen
                    if ((skeleton.get3DJointX(Skeleton.HAND_RIGHT) <= skeleton.get3DJointX(Skeleton.HAND_LEFT)) &&
                            (skeleton.get3DJointX(Skeleton.HAND_LEFT) >= skeleton.get3DJointX(Skeleton.HAND_RIGHT))) {
                        setState(State.CROSSED);
                    }
                }
                else if(getState() == State.CROSSED)
                {
                    // Sobald die Ellenbogen weiter vom Körper entfernt sind als die Schultern, Zustandsübergang
                    // nach SIDEWARE_OUTSIDE auslösen und Geste übermitteln
                    if ((skeleton.get3DJointX(Skeleton.ELBOW_RIGHT) > skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) + 0.1) &&
                            (skeleton.get3DJointX(Skeleton.ELBOW_LEFT) < skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) - 0.1)) {
                        setState(State.SIDEWARD_OUTSIDE);
                    }
                }
            }
        }

        unhandled(message);
    }
}
