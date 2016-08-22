package kinector;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.GestureMessage;
import messages.SkeletonMessage;

import java.util.ArrayList;

/** Aktor-Klasse zum Erkennen von Gesten.
 *
 * Diese Klasse dient als Aktor und behandelt SkeletonMessages. Anhand der erhaltenen Skelett-Daten werden Gesten erkannt
 * und als neue GestureMessage an den zugehörigen GestureInterpreter verschickt.
 */
public class GestureRecognizer extends UntypedActor {

    /** Zugehöriger GestureInterpreter-Aktor, an den bei Erkennen einer Geste eine GestureMessage verschickt wird. */
    private final ActorRef gestureInterpreterActor;

    /** Enums, welche die möglichen erkannten Gesten beschreiben. ("None", wenn keine Geste erkannt wurde.) */
    public enum Gesture { RightHand_PointingTowardsDevice_DefaultActivate, LeftHand_PointingTowardsDevice_DefaultActivate,
        RightHand_PointingTowardsDevice_DefaultDeactivate, LeftHand_PointingTowardsDevice_DefaultDeactivate,
        BothHands_ActivateAll, BothHands_DeactivateAll, RightHand_StretchedUp, LeftHand_StretchedUp, None }

    /** Enums, welche die möglichen erkannten Handzustände beschreiben. */
    public enum HandPosition {
        Unknown_State,
        RightHand_HigherThanHead, LeftHand_HigherThanHead,
        RightHand_LowerThanHead, LeftHand_LowerThanHead,
        RightHand_HigherThanHead_Upward, LeftHand_HigherThanHead_Upward,
        RightHand_LowerThanHead_Downward, LeftHand_LowerThanHead_Downward,
        RightHand_Pointer, LeftHand_Pointer,
        RightHand_Idle, LeftHand_Idle,
        BothHands_HigherThanHead_Crossed, BothHands_HigherThanHead_SidewardOutside, BothHands_HigherThanHead_Outside,
        RightHand_StretchedUp, LeftHand_StretchedUp,
    }

    /** Zweidimensionaler HandPosition[]-Array, in welchem die jeweils zuvor erkannten HandPosition-Zustände von
     * bis zu sechs Skeletten zwischengespeichert werden. */
    private static HandPosition[][] previousHandPosition = new HandPosition[][]{
            {HandPosition.RightHand_Idle, HandPosition.LeftHand_Idle},
            {HandPosition.RightHand_Idle, HandPosition.LeftHand_Idle},
            {HandPosition.RightHand_Idle, HandPosition.LeftHand_Idle},
            {HandPosition.RightHand_Idle, HandPosition.LeftHand_Idle},
            {HandPosition.RightHand_Idle, HandPosition.LeftHand_Idle},
            {HandPosition.RightHand_Idle, HandPosition.LeftHand_Idle}
    };

    private static Gesture[] previousDetectedGesture = new Gesture[] {
            Gesture.None, Gesture.None, Gesture.None, Gesture.None,
            Gesture.None, Gesture.None, Gesture.None
    };

    /** Konstruktor der Klasse GestureRecognizer
     *
     * Dieser Konstruktor erhält eine Referenz zum zugehörigen GestureInterpreter-Aktor
     * und legt diesen zum Versenden GestureMessages ab.
     *
     * @param gestureInterpreterActor Referenz auf den zugehörigen GestureInterpreter-Aktor
     */
    public GestureRecognizer(ActorRef gestureInterpreterActor){
        this.gestureInterpreterActor = gestureInterpreterActor;
    }

    /** Aktor-Methode zum Empfangen von Aktor-Nachrichten.
     *
     * Diese Methode empfängt Aktor-Nachrichten und überprüft, ob es sich bei einer erhaltenen Nachricht um
     * eine SkeletonMessage handelt. Wenn ja, wird für die empfangenen Skelett-Daten der bis zu sechs Skelette
     * die Methode detectGesture aufgerufen. Eine gegebenenfalls erkannte Geste wird dann als neue GestureMessage
     * an den GestureInterpreter zusammen mit dem zugehörigen Skelett versendet.
     *
     * @param message Empfangene Aktor-Nachricht
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SkeletonMessage) {

            ArrayList<Skeleton> skeletons = ((SkeletonMessage) message).skeletons;

            for(Skeleton skeleton : skeletons) {

                // Ausführen der Gestenerkennung
                Gesture detectedGesture = detectGesture(skeleton);

                // Wenn eine Geste erkannt wurde, wird die GestureMessage an den GestureInterpreter versendet.
                if(detectedGesture != Gesture.None && detectedGesture != previousDetectedGesture[skeleton.getPlayerID()]){
                    gestureInterpreterActor.tell(new GestureMessage(detectedGesture, skeleton), ActorRef.noSender());
                }

                previousDetectedGesture[skeleton.getPlayerID()] = detectedGesture;
            }
        }
        unhandled(message);
    }

    /** Methode zum Ermitteln einer Geste.
     *
     * Diese Methode ermittelt anhand der gelieferten Skelett-Daten eine mögliche Geste. Hierfür werden
     * die Positionen der Hände ermittelt. Die jeweiligen HandPosition-Positionen dienen hierbei zum Ermitteln einer Geste.
     *
     * @param skeleton Skelett-Daten zum Ermitteln einer Geste
     * @return Ermittelte Geste
     */
    public static Gesture detectGesture(Skeleton skeleton)
    {
        // Geste wird mit None gekennzeichnet, sofern keine Geste erkannt wurde.
        Gesture gesture = Gesture.None;

        // Bestimmen der HandPosition-Positionen
        HandPosition[] handPosition = getHandPosition(skeleton);

        // Wenn die rechte HandPosition als zeigende HandPosition erkannt wurde
        if(handPosition[0] == HandPosition.RightHand_Pointer){

            // Wenn die linke HandPosition sich aufwärts über die Höhe des Kopf-Knochens des Skelette bewegt hat
            if(handPosition[1] == HandPosition.LeftHand_HigherThanHead_Upward){

                // Geste: Mit rechter HandPosition gezeigt, mit linker HandPosition "aktiviert"
                gesture = Gesture.RightHand_PointingTowardsDevice_DefaultActivate;
            }

            // Wenn die linke HandPosition sich abwärts unterhalb die Höhe des Kopf-Knochens des Skelette bewegt hat
            else if(handPosition[1] == HandPosition.LeftHand_LowerThanHead_Downward){

                // Geste: Mit rechter HandPosition gezeigt, mit linker HandPosition "deaktiviert"
                gesture = Gesture.RightHand_PointingTowardsDevice_DefaultDeactivate;
            }
        }

        // Wenn die linke HandPosition als zeigende HandPosition erkannt wurde
        else if(handPosition[1] == HandPosition.LeftHand_Pointer){

            // Wenn die rechte HandPosition sich aufwärts über die Höhe des Kopf-Knochens des Skelette bewegt hat
            if(handPosition[0] == HandPosition.RightHand_HigherThanHead_Upward){

                // Geste: Mit linker HandPosition gezeigt, mit rechter HandPosition "aktiviert"
                gesture = Gesture.LeftHand_PointingTowardsDevice_DefaultActivate;
            }

            // Wenn die rechte HandPosition sich abwärts unterhalb die Höhe des Kopf-Knochens des Skelette bewegt hat
            else if(handPosition[0] == HandPosition.RightHand_LowerThanHead_Downward){

                // Geste: Mit linker HandPosition gezeigt, mit rechter HandPosition "deaktiviert"
                gesture = Gesture.LeftHand_PointingTowardsDevice_DefaultDeactivate;
            }
        }

        // Wenn beide Hände über dem Kopf gekreuzt werden
        else if(handPosition[0] == HandPosition.BothHands_HigherThanHead_Crossed &&
                handPosition[1] == HandPosition.BothHands_HigherThanHead_Crossed){
                gesture = Gesture.BothHands_ActivateAll;
        }

        // Wenn beide Hände zuvor über dem Kopf gekreuzt wurden und jeweils zur Seite bewegt wurden
        else if(handPosition[0] == HandPosition.BothHands_HigherThanHead_SidewardOutside &&
                handPosition[1] == HandPosition.BothHands_HigherThanHead_SidewardOutside){
            gesture = Gesture.BothHands_DeactivateAll;
        }

        else if(handPosition[0] == HandPosition.RightHand_StretchedUp && handPosition[1] == HandPosition.LeftHand_Idle){
            gesture = Gesture.RightHand_StretchedUp;
        }

        else if(handPosition[1] == HandPosition.LeftHand_StretchedUp && handPosition[0] == HandPosition.RightHand_Idle){
            gesture = Gesture.LeftHand_StretchedUp;
        }

        return gesture;
    }

    /** Methode zum Ermitteln von HandPosition-Positionen.
     *
     * Diese Methode ermittelt anhand der gelieferten Skelett-Daten die Positionen der linken und rechten HandPosition.
     * Die HandPosition-Positionen werden als HandPosition[]-Array zurückgegeben, wobei [0] die rechte HandPosition und [1] die linke HandPosition
     * kennzeichnet.
     *
     * @param skeleton Skelett-Daten zum Ermitteln von HandPosition-Positionen
     * @return Ermittelte HandPosition-Positionen
     */
    public static HandPosition[] getHandPosition(Skeleton skeleton){
        // ID des erhaltenen Skelettes
        int skeletonID = skeleton.getPlayerID();

        HandPosition[] handPosition = new HandPosition[2];

        // HandPosition-Positionen werden grundsätzlich zuerst als Idle gekennzeichnet
        handPosition[0] = HandPosition.Unknown_State;
        handPosition[1] = HandPosition.Unknown_State;


        // Wenn sich beiden Hände unterhalb der Wirbelsäulen-Mitte befinden, bleiben die Handpositionen auf Idle
        if((skeleton.get3DJointY(Skeleton.HAND_RIGHT) <= skeleton.get3DJointY(Skeleton.SPINE_MID))){
            handPosition[0] = HandPosition.RightHand_Idle;
        }

        if(skeleton.get3DJointY(Skeleton.HAND_LEFT) <= skeleton.get3DJointY(Skeleton.SPINE_MID)){
            handPosition[1] = HandPosition.LeftHand_Idle;
        }

        // Wenn sich beide Hände oberhalb des Kopfes befinden
        if((skeleton.get3DJointY(Skeleton.HAND_RIGHT) >= skeleton.get3DJointY(Skeleton.HEAD)) &&
                (skeleton.get3DJointY(Skeleton.HAND_LEFT) >= skeleton.get3DJointY(Skeleton.HEAD))){

            // Wenn sich beide Hände kreuzen (Hände stehen weiterhin auf Unknown, wenn dieser Fall nicht eintritt)
            if((skeleton.get3DJointX(Skeleton.HAND_RIGHT) <= skeleton.get3DJointX(Skeleton.HAND_LEFT)) &&
                    (skeleton.get3DJointX(Skeleton.HAND_LEFT) >= skeleton.get3DJointX(Skeleton.HAND_RIGHT))){

                handPosition[0] = HandPosition.BothHands_HigherThanHead_Crossed;
                handPosition[1] = HandPosition.BothHands_HigherThanHead_Crossed;
            }

            // Wenn sich beide Hände jeweils links und rechts von der jeweiligen Schulter befinden
            else if((skeleton.get3DJointX(Skeleton.ELBOW_RIGHT) > skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) + 0.1) &&
                    (skeleton.get3DJointX(Skeleton.ELBOW_LEFT) < skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) - 0.1)){

                // Wenn die Hände zuvor als Idle gekennzeichnet waren
                if((previousHandPosition[skeletonID][0] == HandPosition.BothHands_HigherThanHead_Crossed) &&
                        previousHandPosition[skeletonID][1] == HandPosition.BothHands_HigherThanHead_Crossed){
                    handPosition[0] = HandPosition.BothHands_HigherThanHead_SidewardOutside;
                    handPosition[1] = HandPosition.BothHands_HigherThanHead_SidewardOutside;
                }

                // Wenn die Hände zuvor nicht als Idle gekennzeichnet waren
                else{
                    handPosition[0] = HandPosition.BothHands_HigherThanHead_Outside;
                    handPosition[1] = HandPosition.BothHands_HigherThanHead_Outside;
                }
            }
        }

        // Wenn sich die rechte HandPosition oberhalb der Höhe des Nackens befindet
        else if(skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.NECK)) {
            // Wenn die linke HandPosition weiter vom Körper entfernt ist als die rechte HandPosition,
            // wird die linke HandPosition als zeigende HandPosition angesehen
            if (skeleton.get3DJointZ(Skeleton.HAND_LEFT) < skeleton.get3DJointZ(Skeleton.HAND_RIGHT) && handPosition[1] != HandPosition.LeftHand_Idle) {

                // Wenn die rechte HandPosition sich oberhalb der Höhe des Kopfes befindet
                if (skeleton.get3DJointY(Skeleton.HAND_RIGHT) >= skeleton.get3DJointY(Skeleton.HEAD)) {
                    handPosition[1] = HandPosition.LeftHand_Pointer;

                    // Wenn die rechte HandPosition zuvor unterhalb des Kopfes war
                    if (previousHandPosition[skeletonID][0] == HandPosition.RightHand_LowerThanHead) {
                        handPosition[0] = HandPosition.RightHand_HigherThanHead_Upward;
                    } else  {
                        handPosition[0] = HandPosition.RightHand_HigherThanHead;
                    }
                }

                // Wenn die rechte HandPosition sich unterhalb der Höhe des Kopfes befindet
                else if (skeleton.get3DJointY(Skeleton.HAND_RIGHT) < skeleton.get3DJointY(Skeleton.HEAD)) {
                    handPosition[1] = HandPosition.LeftHand_Pointer;

                    // Wenn die HandPosition zuvor oberhalb des Kopfes war
                    if (previousHandPosition[skeletonID][0] == HandPosition.RightHand_HigherThanHead) {
                        handPosition[0] = HandPosition.RightHand_LowerThanHead_Downward;
                    } else {
                        handPosition[0] = HandPosition.RightHand_LowerThanHead;
                    }
                }
            }

            else if((skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.SHOULDER_RIGHT)) &&
                    (skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.ELBOW_RIGHT)) &&
                    (skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.HEAD)) &&
                    handPosition[1] ==  HandPosition.LeftHand_Idle){



                if(((skeleton.get3DJointX(Skeleton.HAND_RIGHT)) > skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) - 0.1) &&
                        ((skeleton.get3DJointX(Skeleton.HAND_RIGHT)) < skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) + 0.1) &&
                        ((skeleton.get3DJointX(Skeleton.ELBOW_RIGHT)) > skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) - 0.1) &&
                        ((skeleton.get3DJointX(Skeleton.ELBOW_RIGHT)) < skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) + 0.1)){

                    //System.out.println(skeleton.get3DJointZ(Skeleton.HAND_RIGHT));
                    if(((skeleton.get3DJointZ(Skeleton.HAND_RIGHT)) < skeleton.get3DJointZ(Skeleton.SHOULDER_RIGHT) + 0.1) &&
                            ((skeleton.get3DJointZ(Skeleton.HAND_RIGHT)) > skeleton.get3DJointZ(Skeleton.SHOULDER_RIGHT) - 0.1) &&
                            ((skeleton.get3DJointZ(Skeleton.ELBOW_RIGHT)) < skeleton.get3DJointZ(Skeleton.SHOULDER_RIGHT) + 0.1) &&
                            ((skeleton.get3DJointZ(Skeleton.ELBOW_RIGHT)) > skeleton.get3DJointZ(Skeleton.SHOULDER_RIGHT) - 0.1)){
                        //System.out.println("Rechte HandPosition ausgestreckt im Toleranzbereich");
                        handPosition[0] = HandPosition.RightHand_StretchedUp;
                    }
                }

            }
        }

        // Wenn sich die linke HandPosition oberhalb der Höhe des Nackens befindet
        else if(skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.NECK)) {

            // Wenn die rechte HandPosition weiter vom Körper entfernt ist als die linke HandPosition,
            // wird die rechte HandPosition als zeigende HandPosition angesehen
            if (skeleton.get3DJointZ(Skeleton.HAND_RIGHT) < skeleton.get3DJointZ(Skeleton.HAND_LEFT) && handPosition[0] != HandPosition.RightHand_Idle) {

                // Wenn die linke HandPosition sich oberhalb der Höhe des Kopfes befindet
                if (skeleton.get3DJointY(Skeleton.HAND_LEFT) >= skeleton.get3DJointY(Skeleton.HEAD)) {
                    handPosition[0] = HandPosition.RightHand_Pointer;

                    // Wenn die linke HandPosition zuvor unterhalb des Kopfes war
                    if (previousHandPosition[skeletonID][1] == HandPosition.LeftHand_LowerThanHead) {
                        handPosition[1] = HandPosition.LeftHand_HigherThanHead_Upward;
                    } else {
                        handPosition[1] = HandPosition.LeftHand_HigherThanHead;
                    }
                }

                // Wenn die linke HandPosition sich unterhalb der Höhe des Kopfes befindet
                else if (skeleton.get3DJointY(Skeleton.HAND_LEFT) < skeleton.get3DJointY(Skeleton.HEAD)) {
                    handPosition[0] = HandPosition.RightHand_Pointer;

                    // Wenn die linke HandPosition zuvor oberhalb des Kopfes war
                    if (previousHandPosition[skeletonID][1] == HandPosition.LeftHand_HigherThanHead) {
                        handPosition[1] = HandPosition.LeftHand_LowerThanHead_Downward;
                    } else {
                        handPosition[1] = HandPosition.LeftHand_LowerThanHead;
                    }
                }
            }

            else if((skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.SHOULDER_LEFT)) &&
                    (skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.ELBOW_LEFT)) &&
                    (skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.HEAD)) &&
                    handPosition[0] ==  HandPosition.RightHand_Idle){

                if(((skeleton.get3DJointX(Skeleton.HAND_LEFT)) > skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) - 0.1) &&
                        ((skeleton.get3DJointX(Skeleton.HAND_LEFT)) < skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) + 0.1) &&
                        ((skeleton.get3DJointX(Skeleton.ELBOW_LEFT)) > skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) - 0.1) &&
                        ((skeleton.get3DJointX(Skeleton.ELBOW_LEFT)) < skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) + 0.1)){

                    //System.out.println(skeleton.get3DJointZ(Skeleton.HAND_RIGHT));
                    if(((skeleton.get3DJointZ(Skeleton.HAND_LEFT)) < skeleton.get3DJointZ(Skeleton.SHOULDER_LEFT) + 0.1) &&
                            ((skeleton.get3DJointZ(Skeleton.HAND_LEFT)) > skeleton.get3DJointZ(Skeleton.SHOULDER_LEFT) - 0.1) &&
                            ((skeleton.get3DJointZ(Skeleton.ELBOW_LEFT)) < skeleton.get3DJointZ(Skeleton.SHOULDER_LEFT) + 0.1) &&
                            ((skeleton.get3DJointZ(Skeleton.ELBOW_LEFT)) > skeleton.get3DJointZ(Skeleton.SHOULDER_LEFT) - 0.1)){
                        //System.out.println("Linke HandPosition ausgestreckt im Toleranzbereich");
                        handPosition[1] = HandPosition.LeftHand_StretchedUp;
                    }

                }
            }
        }
        if(handPosition[0] != HandPosition.Unknown_State){
            previousHandPosition[skeletonID][0] = handPosition[0];
        }

        if(handPosition[1] != HandPosition.Unknown_State){
            previousHandPosition[skeletonID][1] = handPosition[1];
        }

        return handPosition;
    }
}

