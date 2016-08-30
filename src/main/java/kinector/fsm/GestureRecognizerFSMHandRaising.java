package kinector.fsm;

import akka.actor.ActorRef;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.HelperEnums.Hand;
import messages.SkeletonStateMessage;

/**
 * Sub-Automat für die Erkennung der Teilgeste "Gerät an/aus"
 */
public class GestureRecognizerFSMHandRaising extends AbstractGestureRecognizerFSM {

    /**
     * Aufzählung der Automaten-Zustände
     */
    private enum State
    {
        IDLE,                       // Ruhezustand
        LOWER_THAN_HEAD,            // Hand unterhalb des Kopfes (bleibend)
        LOWER_THAN_HEAD_SINKING,    // Hand unterhalb des Kopfes gesenkt (erstmalig)
        HIGHER_THAN_HEAD,           // Hand oberhalb des Kopfes (bleibend)
        HIGHER_THAN_HEAD_RISING,    // Hand oberhalb des Kopfes gehoben (erstmalig)
    }

    /**
     * Zustände der linken Handknochen der Skelette
     */
    private State[] leftHandStates = {
        State.IDLE, State.IDLE, State.IDLE, State.IDLE,
        State.IDLE, State.IDLE, State.IDLE
    };

    /**
     * Zustände der rechten Handknochen der Skelette
     */
    private State[] rightHandStates = {
        State.IDLE, State.IDLE, State.IDLE, State.IDLE,
        State.IDLE, State.IDLE, State.IDLE
    };

    /**
     * Behandlung der Skelettdaten abhängig vom "Hand-Kontext", der Zusatzparameter definiert,
     * welche Hand das Gerät in diesem Augenblick steuern soll
     * @param skeleton Zu behandelndes Skelett
     * @param hand Betroffene Hand, dieser Parameter stellt eine Zusatzinformation für Sub-Automaten
     *             zur Verfügung, welche einzelne Handknochen verwenden, jedoch sowohl eine Behandlung
     */
    @Override
    protected void handleSkeleton(Skeleton skeleton, Hand hand) {
        int id = skeleton.getPlayerID();
        if(hand == Hand.LEFT)
            leftHandStates[id] = handleHandState(skeleton, leftHandStates[id], Skeleton.HAND_LEFT);
        else if(hand == Hand.RIGHT)
            rightHandStates[id] = handleHandState(skeleton, rightHandStates[id], Skeleton.HAND_RIGHT);
    }

    /**
     * Behandlung des Hand-Zustands, Zustandsübergangsfunktion für den Sub-Automaten
     * @param skeleton Betroffenes Skelett
     * @param currentState Aktueller Zustand des Subautomaten
     * @param bone Konstante, welche den Knochen-Index im Skelett darstellt,
     *             definiert in {@link edu.ufl.digitalworlds.j4k.J4KSDK}
     * @return Neuer Handzustand des betroffenen Skeletts
     */
    private State handleHandState(Skeleton skeleton, State currentState, int bone) {
        switch(currentState)
        {
            case IDLE:
            {
                // Im Ruhezustand wird abhängig von der Handposition der Initialzustand des Automaten
                // für die weitere Betrachtung der Handbewegung gesetzt
                if(isBoneLowerThanHead(skeleton, bone))
                    currentState = State.LOWER_THAN_HEAD;
                else if(isBoneHigherThanHead(skeleton, bone))
                    currentState = State.HIGHER_THAN_HEAD;
                break;
            }
            case LOWER_THAN_HEAD:
            {
                // Befand sich die Hand unterhalb des Kopfes und ist nun oberhalb des Kopfes, wurde
                // eine Aufwärtsbewegung der Hand durchgeführt (HIGHER_THAN_HEAD_RISING)
                if(isBoneHigherThanHead(skeleton, bone))
                    currentState = State.HIGHER_THAN_HEAD_RISING;
                break;
            }
            case LOWER_THAN_HEAD_SINKING:
            {
                // Befand die Hand in der Abwärtsbewegung und ist sie weiterhin unterhalb des Kopfes,
                // wird keine Änderung des Gerätezustands ausgelöst, andernsfalls befindet sich
                // die Hand nun in einer Aufwartsbewegung (HIGHER_THAN_HEAD_RISING)
                if(isBoneLowerThanHead(skeleton, bone))
                    currentState = State.LOWER_THAN_HEAD;
                else if(isBoneHigherThanHead(skeleton, bone))
                    currentState = State.HIGHER_THAN_HEAD_RISING;
                break;
            }
            case HIGHER_THAN_HEAD:
            {
                // Befand sich die Hand oberhalb des Kopfes und ist nun unterhalb des Kopfes, wurde
                // eine Abwärtsbewegung der Hand durchgeführt (LOWER_THAN_HEAD_SINKING)
                if(isBoneLowerThanHead(skeleton, bone))
                    currentState = State.LOWER_THAN_HEAD_SINKING;
                break;
            }
            case HIGHER_THAN_HEAD_RISING:
            {
                // Befand die Hand in der Aufwärtsbewegung und ist sie weiterhin oberhalb des Kopfes,
                // wird keine Änderung des Gerätezustands ausgelöst, andernsfalls befindet sich
                // die Hand nun in einer Abwärtsbewegung (LOWER_THAN_HEAD_SINKING)
                if(isBoneHigherThanHead(skeleton, bone))
                    currentState = State.HIGHER_THAN_HEAD;
                else if(isBoneLowerThanHead(skeleton, bone))
                    currentState = State.LOWER_THAN_HEAD_SINKING;
                break;
            }
        }

        if(currentState == State.HIGHER_THAN_HEAD_RISING) {
            // Befindet sich die Hand in einer Aufwärtsbewegung wird der ursprüngliche Sender der
            // Skelettnachricht (der Controller-Automat) über den Befehl zur Geräteaktivierung informiert
            getSender().tell(new SkeletonStateMessage(skeleton,
                    bone == Skeleton.HAND_LEFT ? Hand.LEFT : Hand.RIGHT,
                    GestureRecognizerFSM.Gesture.ActivateDevice), ActorRef.noSender());
        }
        else if(currentState == State.LOWER_THAN_HEAD_SINKING) {
            // Befindet sich die Hand in einer Abwärtsbewegung wird der ursprüngliche Sender der
            // Skelettnachricht (der Controller-Automat) über den Befehl zur Gerätedeaktivierung informiert
            currentState = State.LOWER_THAN_HEAD;
            getSender().tell(new SkeletonStateMessage(skeleton,
                    bone == Skeleton.HAND_LEFT ? Hand.LEFT : Hand.RIGHT,
                    GestureRecognizerFSM.Gesture.DeactivateDevice), ActorRef.noSender());
        }
        return currentState;
    }

    /**
     * Ermittelt, ob sich der Knochen mit dem übergebenen Index unterhalb des Kopfknochens
     * befindet
     * @param skeleton Betroffenes Skelett
     * @param bone Knochen-Index, definiert in {@link edu.ufl.digitalworlds.j4k.J4KSDK}
     * @return true, wenn Knochen unterhalb des Kopfes, sonst false
     */
    private boolean isBoneLowerThanHead(Skeleton skeleton, int bone) {
        return skeleton.get3DJointY(bone) < skeleton.get3DJointY(Skeleton.HEAD);
    }

    /**
     * Ermittelt, ob sich der Knochen mit dem übergebenen Index oberhalb des Kopfknochens
     * befindet
     * @param skeleton Betroffenes Skelett
     * @param bone Knochen-Index, definiert in {@link edu.ufl.digitalworlds.j4k.J4KSDK}
     * @return true, wenn Knochen oberhalb des Kopfes, sonst false
     */
    private boolean isBoneHigherThanHead(Skeleton skeleton, int bone) {
        return skeleton.get3DJointY(bone) > skeleton.get3DJointY(Skeleton.HEAD);
    }
}
