package kinector.fsm;

import akka.actor.ActorRef;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.HelperEnums.Hand;
import messages.SkeletonStateMessage;

/**
 * Sub-Automat für die Erkennung der Teilgeste "Hände gekreuzt/entkreuzt" (De-/Aktivierung aller Geräte)
 */
public class GestureRecognizerFSMHandsCrossing extends AbstractGestureRecognizerFSM {

    /**
     * Aufzählung der Automaten-Zustände
     */
    private enum State
    {
        IDLE,
        CROSSED,
        UNCROSSED,
        TOWARDS_CROSSED,
        TOWARDS_UNCROSSED,
    }

    /**
     * Zustände der Skelette im Bezug auf die zu erkennende Geste
     */
    private State[] states = {
            State.IDLE, State.IDLE, State.IDLE, State.IDLE,
            State.IDLE, State.IDLE, State.IDLE
    };

    /**
     * Behandlung der Skelettdaten, "Hand-Kontext" wird nicht verwendet
     * @param skeleton Zu behandelndes Skelett
     * @param hand Betroffene Hand, dieser Parameter stellt eine Zusatzinformation für Sub-Automaten
     *             zur Verfügung, welche einzelne Handknochen verwenden, jedoch sowohl eine Behandlung
     */
    @Override
    protected void handleSkeleton(Skeleton skeleton, Hand hand) {
        int id = skeleton.getPlayerID();
        State currentState = states[id];
        State newState = states[id];

        // Wenn sich mindestens eine Hand unterhalb des Kopfes befindet, kehrt der Automat in den
        // Ruhezustand zurück
        if(!areBothHandsAboveHead(skeleton)) {
            newState = State.IDLE;
        } else {
            switch (currentState) {
                case IDLE:
                {
                    // Aus dem Ruhezustand heraus kann nur mit gekreuzten Armen begonnen werden
                    // (Ist-Zustand im zugrunde liegenden Quellenstand des Jahresprojekts)
                    if (areBothHandsCross(skeleton))
                        newState = State.CROSSED;
                    break;
                }
                case CROSSED:
                {
                    // Wenn die Hände gekreuzt waren und nun entkreuzt werden, wechsele in Zustand UNCROSSED
                    if(!areBothHandsCross(skeleton))
                        newState = State.UNCROSSED;
                    break;
                }
                case UNCROSSED:
                {
                    // Wenn die Hand entkreuzt waren und nun gekreuzt werden, wechsele in Zustand CROSSED
                    if(areBothHandsCross(skeleton))
                        newState = State.CROSSED;
                    break;
                }
            }
        }

        // Aktualierung des Skelett-Zustandes
        states[id] = newState;
        // Gleicher Start- und Endzustand führen zu keiner Nachricht
        if(newState != currentState) {
            if (newState == State.CROSSED) {
                // Wurden die Hände gekreuzt, wird der Controller-Automat über den Befehl zum Aktivieren
                // aller Geräte informiert
                getSender().tell(new SkeletonStateMessage(skeleton,
                        Hand.BOTH,
                        GestureRecognizerFSM.Gesture.BothHands_ActivateAll), ActorRef.noSender());
            } else if (newState == State.UNCROSSED) {
                // Wurden die Hände entkreuzt, wird der Controller-Automat über den Befehl zum Deaktivieren
                // aller Geräte informiert
                getSender().tell(new SkeletonStateMessage(skeleton,
                        Hand.BOTH,
                        GestureRecognizerFSM.Gesture.BothHands_DeactivateAll), ActorRef.noSender());
            } else if(newState == State.IDLE) {
                // Wurden die Hände herunter genommen, wird der Controller-Automat über das Ende dieser Geste
                // informiert
                getSender().tell(new SkeletonStateMessage(skeleton,
                        Hand.BOTH, GestureRecognizerFSM.Gesture.None), ActorRef.noSender());
            }
        }
    }

    /**
     * Ermittelt, ob sich beide Hände oberhalb des Kopfes befinden
     * @param skeleton Betroffenes Skelett
     * @return true, wenn sich beide Hände oberhalb des Kopfknochens befinden
     */
    private boolean areBothHandsAboveHead(Skeleton skeleton) {
        return (skeleton.get3DJointY(Skeleton.HAND_RIGHT) >= skeleton.get3DJointY(Skeleton.HEAD)) &&
                (skeleton.get3DJointY(Skeleton.HAND_LEFT) >= skeleton.get3DJointY(Skeleton.HEAD));
    }

    /**
     * Ermittelt, ob beide Hände gekreuzt wurden (Skelett mit Sicht zur Kinect!)
     * @param skeleton Betroffenes Skelett
     * @return true, wenn sich die rechte Hand weiter links befindet als die linke Hand
     */
    private boolean areBothHandsCross(Skeleton skeleton) {
        return (skeleton.get3DJointX(Skeleton.HAND_RIGHT) <= skeleton.get3DJointX(Skeleton.HAND_LEFT));
    }
}
