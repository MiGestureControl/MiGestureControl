package kinector.fsm;

import akka.actor.ActorRef;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.HelperEnums.Hand;
import messages.SkeletonStateMessage;

/**
 * Sub-Automat für die Erkennung der Teilgeste "Sound aktivieren"
 */
public class GestureRecognizerFSMHandFlashing extends AbstractGestureRecognizerFSM {

    /**
     * Behandlung der Skelettdaten, "Hand-Kontext" wird nicht verwendet
     * @param skeleton Zu behandelndes Skelett
     * @param hand Betroffene Hand, dieser Parameter stellt eine Zusatzinformation für Sub-Automaten
     *             zur Verfügung, welche einzelne Handknochen verwenden, jedoch sowohl eine Behandlung
     */
    @Override
    protected void handleSkeleton(Skeleton skeleton, Hand hand) {
        if (isLeftHandIsAboveHead(skeleton)) {
            // Zeigt die linke Hand gerade aufwärts über den Kopf hinweg, wird der Controller-Automat
            // über die Aktivieren der Soundsteuerung informiert
            getSender().tell(
                    new SkeletonStateMessage(skeleton, Hand.LEFT, GestureRecognizerFSM.Gesture.StretchedUp),
                    ActorRef.noSender());
        }
        if (isRightHandIsAboveHead(skeleton)) {
            // Zeigt die linke Hand gerade aufwärts über den Kopf hinweg, wird der Controller-Automat
            // über die Aktivieren der Soundsteuerung informiert
            getSender().tell(
                    new SkeletonStateMessage(skeleton, Hand.RIGHT, GestureRecognizerFSM.Gesture.StretchedUp),
                    ActorRef.noSender());
        }
    }

    /**
     * Ermittelt, ob sich der rechte Handknochen gerade gestreckt nach oben oberhalb des Kopfes befindet
     * @param skeleton Betroffenes Skelett
     * @return true, wenn sich der rechte Handknochen oberhalb von rechter Schulter, rechtem Ellenbogen
     *         und Kopf befindet
     */
    private boolean isRightHandIsAboveHead(Skeleton skeleton) {
        return (skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.SHOULDER_RIGHT)) &&
                (skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.ELBOW_RIGHT)) &&
                (skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.HEAD));
    }

    /**
     * Ermittelt, ob sich der linke Handknochen gerade gestreckt nach oben oberhalb des Kopfes befindet
     * @param skeleton Betroffenes Skelett
     * @return true, wenn sich der linke Handknochen oberhalb von linker Schulter, linkem Ellenbogen
     *         und Kopf befindet
     */
    private boolean isLeftHandIsAboveHead(Skeleton skeleton) {
        return (skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.SHOULDER_LEFT)) &&
                (skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.ELBOW_LEFT)) &&
                (skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.HEAD));
    }
}
