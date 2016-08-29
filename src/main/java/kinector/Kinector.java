package kinector;

import akka.actor.ActorRef;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.SkeletonMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/** Klasse zum Herstellen der Verbindung mit der Sensortechnologie Kinect 2.0.
 *
 * Diese Klasse stellt die Verbindung zu der Sensortechnologie Kinect 2.0 her.
 * Sie ist von der J4K-Klasse J4KSDK abgeleitet und erhält bei Initialisierung eine Referenz auf einen Aktor des
 * Aktorsystems. Zudem stellt die Klasse mehrere Methoden-Implementierungen zur Verfügung, welche bei eintretenden Ereignissen
 * durch die Programmbibliothek aufgerufen werden.
 */
public class Kinector extends J4KSDK {

    /** Referenz auf Aktor des Aktorsystems */
    private final ActorRef actor;

    /** Konstruktor der Klasse Kinector.
     *
     *  Der Konstruktor der Klasse Kinector erhält für die spätere Behandlung der J4K-Ereignisse eine Referenz auf
     *  einen Aktor des Aktorsystems, an welchen Nachrichten mit Daten zur weiteren Bearbeitung geschickt werden können.
     *  Zudem wird die Verbindung zu der Kinect hergestellt.
     *
     * @param actor Referenz auf Aktor des Aktorsystems
     */
    public Kinector(ActorRef actor){
        this.actor = actor;

        start(J4KSDK.COLOR|J4KSDK.DEPTH|J4KSDK.SKELETON);
    }

    @Override
    public void onColorFrameEvent(byte[] arg0) {
        // Nicht implementiert. Wird nicht weiter verwendet
    }

    @Override
    public void onDepthFrameEvent(short[] depthFrame, byte[] arg1, float[] arg2, float[] arg3) {
        // Nicht implementiert. Wird nicht weiter verwendet
    }

    /** Methode zum Behandeln von SkeletonFrameEvents.
     *
     * Die Methode wird im Falle eines SkeletonFrameEvent durch J4K aufgerufen. Sie erhält als Parameter die
     * Tracking-Zustände der Skelette ebenso wie deren Gelenk-Positionen, -Ausrichtungen und -Zustände.
     * Auf Grund der unvorteilhaften Struktur der Parameter wird auf ihre Verwendung verzichtet. Stattdessen
     * werden die erkannten Skelette über eine hierfür bereitgestellte Methode direkt abgerufen. Eine Prüfung
     * auf Tracking-Zustände sowie eine mögliche Überschreitung des Erkennungsbereiches wird durchgeführt.
     * Skelette, die möglicherweise den Erkennungsbereich verlassen, also im Pufferbereich sind, oder nicht getrackt sind
     * werden entfernt. Die übriggebliebenen Skelette werden mit IDs versehen und zur weiteren Bearbeitung weitergeleitet.
     *
     * @param flags Flags, welche den Tracking-Zustand der Skelette kennzeichnen
     * @param positions Position der Skelett-Gelenke
     * @param orientations Ausrichtung der Skelett-Gelenke
     * @param state Zustand der einzelnen Skelett-Gelenke
     */
    @Override
    public void onSkeletonFrameEvent(boolean[] flags, float[] positions, float[] orientations, byte[] state) {

        ArrayList<Skeleton> skeletons = new ArrayList<Skeleton>(Arrays.asList(this.getSkeletons()));



        Iterator<Skeleton> skelIterator = skeletons.iterator();
        while (skelIterator.hasNext()) {
            Skeleton skeleton = skelIterator.next();
            if(!skeleton.isTracked()) {
                skelIterator.remove();
            }

            else if(skeleton.get3DJointZ(Skeleton.HEAD) < 1.4){
                skelIterator.remove();
            }

            else if(skeleton.get3DJointX(Skeleton.SPINE_MID) < -1.83 || skeleton.get3DJointX(Skeleton.SPINE_MID) > 2.0){
                skelIterator.remove();
            }
        }

        for(int i = 0; i < skeletons.size(); i++){
            skeletons.get(i).setPlayerID(i);

        }
            actor.tell(new SkeletonMessage(skeletons), ActorRef.noSender());
    }
}
