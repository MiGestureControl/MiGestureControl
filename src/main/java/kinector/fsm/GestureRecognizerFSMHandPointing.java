package kinector.fsm;

import akka.actor.ActorRef;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.HelperEnums.Hand;
import messages.SkeletonStateMessage;

/**
 * Sub-Automat für die Erkennung der Teilgeste "Linke/Rechte Hand zeigt auf Gerät"
 */
public class GestureRecognizerFSMHandPointing extends AbstractGestureRecognizerFSM {

    /**
     * Minimale Z-Distanz (räumliche Tiefe) zwischen den Handknochen, um Fehlgesten zu reduzieren
     */
    private final float minimumZDistanceBetweenHands = 0.1f;

    /**
     * Behandlung der Skelettdaten, "Hand-Kontext" wird nicht verwendet
     * @param skeleton Zu behandelndes Skelett
     * @param hand Betroffene Hand, dieser Parameter stellt eine Zusatzinformation für Sub-Automaten
     *             zur Verfügung, welche einzelne Handknochen verwenden, jedoch sowohl eine Behandlung
     */
    @Override
    protected void handleSkeleton(Skeleton skeleton, Hand hand) {
        // Die linke Hand wird als zeigend angesehen, wenn sie sich weiter vom Körper entfernt (d.h. näher an der
        // Kinect) befindet, als die rechte Hand
        if (isLeftHandNearerAtDeviceThanRightHand(skeleton) && areHandsFarEnoughFromEachOther(skeleton)) {
            getSender().tell(
                    new SkeletonStateMessage(skeleton, Hand.LEFT, GestureRecognizerFSM.Gesture.Pointing),
                    ActorRef.noSender());
        }
        // Die rechte Hand wird als zeigend angesehen, wenn sie sich weiter vom Körper entfernt (d.h. näher an der
        // Kinect) befindet, als die linke Hand
        if (isRightHandNearerAtDeviceThanLeftHand(skeleton) && areHandsFarEnoughFromEachOther(skeleton)) {
            getSender().tell(
                    new SkeletonStateMessage(skeleton, Hand.RIGHT, GestureRecognizerFSM.Gesture.Pointing),
                    ActorRef.noSender());
        }
    }

    /**
     * Ermittelt, ob sich die linke Hand weiter vom Körper entfernt (d.h. näher an der Kinect) befindet,
     * als die rechte Hand
     * @param skeleton Betroffenes Skelett
     * @return true, wenn sich die linke Hand näher an der Kinect und unterhalb des Kopfes befindet
     */
    private boolean isLeftHandNearerAtDeviceThanRightHand(Skeleton skeleton) {
        return skeleton.get3DJointZ(Skeleton.HAND_LEFT) < skeleton.get3DJointZ(Skeleton.HAND_RIGHT) &&
                skeleton.get3DJointY(Skeleton.HAND_LEFT) < skeleton.get3DJointY(Skeleton.HEAD);
    }

    /**
     * Ermittelt, ob sich die rechte Hand weiter vom Körper entfernt (d.h. näher an der Kinect) befindet,
     * als die linke Hand
     * @param skeleton Betroffenes Skelett
     * @return true, wenn sich die rechte Hand näher an der Kinect und unterhalb des Kopfes befindet
     */
    private boolean isRightHandNearerAtDeviceThanLeftHand(Skeleton skeleton) {
        return skeleton.get3DJointZ(Skeleton.HAND_RIGHT) < skeleton.get3DJointZ(Skeleton.HAND_LEFT) &&
                skeleton.get3DJointY(Skeleton.HAND_RIGHT) < skeleton.get3DJointY(Skeleton.HEAD);
    }

    /**
     * Ermittelt, ob sich linke und rechte Hand in einem ausreichenden Abstand (räumliche Tiefe)
     * voneinander befinden
     * @param skeleton Betroffenes Skelett
     * @return true, wenn Distanz in der räumlichen Tiefe zwischen den Händen den Minimalwert überschreitet
     */
    private boolean areHandsFarEnoughFromEachOther(Skeleton skeleton) {
        return getZDistanceBetweenHands(skeleton) >= minimumZDistanceBetweenHands;
    }

    /**
     * Ermittelt die Distanz in der räumlichen Tiefe zwischen den Händen
     * @param skeleton Betroffenes Skelett
     * @return Handabstand in der räumlichen Tiefe
     */
    private float getZDistanceBetweenHands(Skeleton skeleton) {
        return Math.abs(skeleton.get3DJointZ(Skeleton.HAND_LEFT) - skeleton.get3DJointZ((Skeleton.HAND_RIGHT)));
    }
}
