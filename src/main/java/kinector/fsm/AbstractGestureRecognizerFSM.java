package kinector.fsm;

import akka.actor.UntypedActor;
import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;
import messages.HelperEnums.Hand;
import messages.SingleSkeletonMessage;
import messages.SkeletonMessage;

import java.util.List;

/**
 * Basisklasse aller Sub-Automaten für die Erkennung von Gesten
 */
abstract class AbstractGestureRecognizerFSM extends UntypedActor
{
    /**
     * Behandlungsroutine für Skelettinformationen
     * @param skeleton Zu behandelndes Skelett
     * @param hand Betroffene Hand, dieser Parameter stellt eine Zusatzinformation für Sub-Automaten
     *             zur Verfügung, welche einzelne Handknochen verwenden, jedoch sowohl eine Behandlung
     *             für die linke als auch die rechte Hand besitzen (bspw. {@link GestureRecognizerFSMHandRaising})
     */
    protected abstract void handleSkeleton(Skeleton skeleton, Hand hand);

    /**
     * Nachrichten-Empfänger-Funktion des Aktors
     * Behandelt werden Skelettdaten des Kinectors, die durch den übergeordneten Controller-Aktor
     * zur Verfügung gestellt werden
     * @param message Nachrichten-Objekt
     * @throws Exception
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof SingleSkeletonMessage) {
            handleSkeleton(((SingleSkeletonMessage)message).skeleton, ((SingleSkeletonMessage)message).hand);
        }

        unhandled(message);
    }
}
