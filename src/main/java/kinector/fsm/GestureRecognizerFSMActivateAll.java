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
public class GestureRecognizerFSMActivateAll extends UntypedActor { // extends AbstractGestureRecognizerFSM {

    private enum State
    {
        IDLE, ABOVE_HEAD, SIDEWARD_OUTSIDE, CROSSED
    }

    private ActorRef target;
    private State state = State.IDLE;
    private Skeleton currentSkeleton;

    public GestureRecognizerFSMActivateAll(ActorRef target)
    {
        this.target = target;
    }

    protected void setState(State state) {
        if(this.state != state) {
            this.state = transition(this.state, state);
        }
    }

    protected State getState() {
        return state;
    }

    protected State transition(State old, State next)
    {
        if(old == State.CROSSED && next == State.SIDEWARD_OUTSIDE)
        {
            // reset state machine
            next = State.IDLE;
            // Von gekreuzten Armen in die Auswärtsbewegung
            target.tell(
                    new GestureMessage(GetRecognizableGesture(), currentSkeleton),
                    ActorRef.noSender());
        }

        return next;
    }

    //@Override
    public GestureRecognizer.Gesture GetRecognizableGesture() {
        return GestureRecognizer.Gesture.BothHands_ActivateAll;
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
