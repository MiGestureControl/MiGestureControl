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
        BothHands_ActivateAll, BothHands_DeactivateAll, RightHand_StretchedUp, LeftHand_StretchedUp, None,
        
        /* NEU */
        Pointing,           // Zeigende Hand erkannt
        ActivateDevice,     // "Gerät aktivieren" erkannt
        DeactivateDevice,   // "Gerät deaktivieren" erkannt
        StretchedUp,        // Nach oben gestreckter Arm erkannt
    }

    /** Enums, welche die möglichen erkannten Handzustände beschreiben. */
    public enum Hand{
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

    /** Zweidimensionaler Hand[]-Array, in welchem die jeweils zuvor erkannten Hand-Zustände von
     * bis zu sechs Skeletten zwischengespeichert werden. */
    private static Hand[][] previousHandGestures = new Hand[][]{
            {Hand.RightHand_Idle, Hand.LeftHand_Idle},
            {Hand.RightHand_Idle, Hand.LeftHand_Idle},
            {Hand.RightHand_Idle, Hand.LeftHand_Idle},
            {Hand.RightHand_Idle, Hand.LeftHand_Idle},
            {Hand.RightHand_Idle, Hand.LeftHand_Idle},
            {Hand.RightHand_Idle, Hand.LeftHand_Idle}
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
                    gestureInterpreterActor.tell(new GestureMessage(detectedGesture, skeleton, 
                            messages.HelperEnums.Hand.UNKNOWN), ActorRef.noSender());
                }

                previousDetectedGesture[skeleton.getPlayerID()] = detectedGesture;
            }
        }
        unhandled(message);
    }

    /** Methode zum Ermitteln einer neuen Line.
     *
     * Diese Methode ermittelt eine neue Line der jeweils zeigenden Hand.
     * Hierfür werden entweder linker oder rechter Ellbogen- und Hand-Knochen der Skelett-Daten verwendet,
     * abhängig davon welche Hand als zeigende Hand erkannt wurde.
     *
     * @param skeleton Skelett-Daten zum Ermitteln einer Line
     * @return Neue Line, die anhand der Skelett-Daten ermittelt wurde
     */
    public static Line getPointingLine(Skeleton skeleton, Hand handGesture)
    {
        // Bestimmen der Hand-Position anhand des Skelettes
        //Hand[] handGestures = getHandPosition(skeleton);

        // Bestimmen der neuen Line, abhängig davon welcher Hand als zeigende Hand erkannt wurde.
        /*if (handGestures[0] == Hand.RightHand_Pointer) {
            return new Line(skeleton.get3DJoint(Skeleton.ELBOW_RIGHT), skeleton.get3DJoint(Skeleton.HAND_RIGHT));
        }
        else if (handGestures[1] == Hand.LeftHand_Pointer) {
            return new Line(skeleton.get3DJoint(Skeleton.ELBOW_LEFT), skeleton.get3DJoint(Skeleton.HAND_LEFT));
        }*/

        if (handGesture == Hand.RightHand_Pointer) {
            return new Line(skeleton.get3DJoint(Skeleton.ELBOW_RIGHT), skeleton.get3DJoint(Skeleton.HAND_RIGHT));
        }
        else if (handGesture == Hand.LeftHand_Pointer) {
            return new Line(skeleton.get3DJoint(Skeleton.ELBOW_LEFT), skeleton.get3DJoint(Skeleton.HAND_LEFT));
        }

        return null;
    }

    /** Methode zum Ermitteln einer Geste.
     *
     * Diese Methode ermittelt anhand der gelieferten Skelett-Daten eine mögliche Geste. Hierfür werden
     * die Positionen der Hände ermittelt. Die jeweiligen Hand-Positionen dienen hierbei zum Ermitteln einer Geste.
     *
     * @param skeleton Skelett-Daten zum Ermitteln einer Geste
     * @return Ermittelte Geste
     */
    public static Gesture detectGesture(Skeleton skeleton)
    {
        //int skeletonID = skeleton.getPlayerID();

        // Geste wird mit None gekennzeichnet, sofern keine Geste erkannt wurde.
        Gesture gesture = Gesture.None;

        // Bestimmen der Hand-Positionen
        Hand[] handGestures = getHandPosition(skeleton);

        // Wenn die rechte Hand als zeigende Hand erkannt wurde
        if(handGestures[0] == Hand.RightHand_Pointer){

            // Wenn die linke Hand sich aufwärts über die Höhe des Kopf-Knochens des Skelette bewegt hat
            if(handGestures[1] == Hand.LeftHand_HigherThanHead_Upward){

                // Geste: Mit rechter Hand gezeigt, mit linker Hand "aktiviert"
                gesture = Gesture.RightHand_PointingTowardsDevice_DefaultActivate;
            }

            // Wenn die linke Hand sich abwärts unterhalb die Höhe des Kopf-Knochens des Skelette bewegt hat
            else if(handGestures[1] == Hand.LeftHand_LowerThanHead_Downward){

                // Geste: Mit rechter Hand gezeigt, mit linker Hand "deaktiviert"
                gesture = Gesture.RightHand_PointingTowardsDevice_DefaultDeactivate;
            }
        }

        // Wenn die linke Hand als zeigende Hand erkannt wurde
        else if(handGestures[1] == Hand.LeftHand_Pointer){

            // Wenn die rechte Hand sich aufwärts über die Höhe des Kopf-Knochens des Skelette bewegt hat
            if(handGestures[0] == Hand.RightHand_HigherThanHead_Upward){

                // Geste: Mit linker Hand gezeigt, mit rechter Hand "aktiviert"
                gesture = Gesture.LeftHand_PointingTowardsDevice_DefaultActivate;
            }

            // Wenn die rechte Hand sich abwärts unterhalb die Höhe des Kopf-Knochens des Skelette bewegt hat
            else if(handGestures[0] == Hand.RightHand_LowerThanHead_Downward){

                // Geste: Mit linker Hand gezeigt, mit rechter Hand "deaktiviert"
                gesture = Gesture.LeftHand_PointingTowardsDevice_DefaultDeactivate;
            }
        }

        // Wenn beide Hände über dem Kopf gekreuzt werden
        else if(handGestures[0] == Hand.BothHands_HigherThanHead_Crossed &&
                handGestures[1] == Hand.BothHands_HigherThanHead_Crossed){
                gesture = Gesture.BothHands_ActivateAll;
        }

        // Wenn beide Hände zuvor über dem Kopf gekreuzt wurden und jeweils zur Seite bewegt wurden
        else if(handGestures[0] == Hand.BothHands_HigherThanHead_SidewardOutside &&
                handGestures[1] == Hand.BothHands_HigherThanHead_SidewardOutside){
            gesture = Gesture.BothHands_DeactivateAll;
        }

        else if(handGestures[0] == Hand.RightHand_StretchedUp && handGestures[1] == Hand.LeftHand_Idle){
            gesture = Gesture.RightHand_StretchedUp;
        }

        else if(handGestures[1] == Hand.LeftHand_StretchedUp && handGestures[0] == Hand.RightHand_Idle){
            gesture = Gesture.LeftHand_StretchedUp;
        }

        /*if (gesture != Gesture.None)
        {
            previousHandGestures[skeletonID][0] = Hand.RightHand_Idle;
            previousHandGestures[skeletonID][1] = Hand.LeftHand_Idle;
        }*/

        return gesture;
    }

    /** Methode zum Ermitteln von Hand-Positionen.
     *
     * Diese Methode ermittelt anhand der gelieferten Skelett-Daten die Positionen der linken und rechten Hand.
     * Die Hand-Positionen werden als Hand[]-Array zurückgegeben, wobei [0] die rechte Hand und [1] die linke Hand
     * kennzeichnet.
     *
     * @param skeleton Skelett-Daten zum Ermitteln von Hand-Positionen
     * @return Ermittelte Hand-Positionen
     */
    /*public static Hand[] getHandPosition(Skeleton skeleton){
        // ID des erhaltenen Skelettes
        int skeletonID = skeleton.getPlayerID();

        Hand[] handGestures = new Hand[2];

        // Hand-Positionen werden grundsätzlich zuerst als Idle gekennzeichnet
        handGestures[0] = Hand.Unknown_State;
        handGestures[1] = Hand.Unknown_State;


        // Wenn sich beiden Hände unterhalb der Wirbelsäulen-Mitte befinden, bleiben die Handpositionen auf Idle
        if((skeleton.get3DJointY(Skeleton.HAND_RIGHT) <= skeleton.get3DJointY(Skeleton.SPINE_MID))){
            handGestures[0] = Hand.RightHand_Idle;
        }

        if(skeleton.get3DJointY(Skeleton.HAND_LEFT) <= skeleton.get3DJointY(Skeleton.SPINE_MID)){
            handGestures[1] = Hand.LeftHand_Idle;
        }

        // Wenn sich beide Hände oberhalb des Kopfes befinden
        if((skeleton.get3DJointY(Skeleton.HAND_RIGHT) >= skeleton.get3DJointY(Skeleton.HEAD)) &&
                (skeleton.get3DJointY(Skeleton.HAND_LEFT) >= skeleton.get3DJointY(Skeleton.HEAD))){

            // Wenn sich beide Hände kreuzen (Hände stehen weiterhin auf Unknown, wenn dieser Fall nicht eintritt)
            if((skeleton.get3DJointX(Skeleton.HAND_RIGHT) <= skeleton.get3DJointX(Skeleton.HAND_LEFT)) &&
                    (skeleton.get3DJointX(Skeleton.HAND_LEFT) >= skeleton.get3DJointX(Skeleton.HAND_RIGHT))){

                handGestures[0] = Hand.BothHands_HigherThanHead_Crossed;
                handGestures[1] = Hand.BothHands_HigherThanHead_Crossed;
            }

            // Wenn sich beide Hände jeweils links und rechts von der jeweiligen Schulter befinden
            else if((skeleton.get3DJointX(Skeleton.ELBOW_RIGHT) > skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) + 0.1) &&
                    (skeleton.get3DJointX(Skeleton.ELBOW_LEFT) < skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) - 0.1)){

                // Wenn die Hände zuvor als Idle gekennzeichnet waren
                if((previousHandGestures[skeletonID][0] == Hand.BothHands_HigherThanHead_Crossed) &&
                        previousHandGestures[skeletonID][1] == Hand.BothHands_HigherThanHead_Crossed){
                    handGestures[0] = Hand.BothHands_HigherThanHead_SidewardOutside;
                    handGestures[1] = Hand.BothHands_HigherThanHead_SidewardOutside;
                }

                // Wenn die Hände zuvor nicht als Idle gekennzeichnet waren
                else{
                    handGestures[0] = Hand.BothHands_HigherThanHead_Outside;
                    handGestures[1] = Hand.BothHands_HigherThanHead_Outside;
                }
            }
        }

        // Wenn sich die rechte Hand oberhalb der Höhe des Nackens befindet
        else if(skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.SPINE_MID)) {
            // Wenn die linke Hand weiter vom Körper entfernt ist als die rechte Hand,
            // wird die linke Hand als zeigende Hand angesehen
            if (skeleton.get3DJointZ(Skeleton.HAND_LEFT) < skeleton.get3DJointZ(Skeleton.HAND_RIGHT) && handGestures[1] != Hand.LeftHand_Idle) {

                // Wenn die rechte Hand sich oberhalb der Höhe des Kopfes befindet
                if (skeleton.get3DJointY(Skeleton.HAND_RIGHT) >= skeleton.get3DJointY(Skeleton.NECK)) {
                    handGestures[1] = Hand.LeftHand_Pointer;

                    // Wenn die rechte Hand zuvor unterhalb des Kopfes war
                    if (previousHandGestures[skeletonID][0] == Hand.RightHand_LowerThanHead) {
                        handGestures[0] = Hand.RightHand_HigherThanHead_Upward;
                    } else  {
                        handGestures[0] = Hand.RightHand_HigherThanHead;
                    }
                }

                // Wenn die rechte Hand sich unterhalb der Höhe des Kopfes befindet
                else if (skeleton.get3DJointY(Skeleton.HAND_RIGHT) < skeleton.get3DJointY(Skeleton.NECK)) {
                    handGestures[1] = Hand.LeftHand_Pointer;

                    // Wenn die Hand zuvor oberhalb des Kopfes war
                    if (previousHandGestures[skeletonID][0] == Hand.RightHand_HigherThanHead) {
                        handGestures[0] = Hand.RightHand_LowerThanHead_Downward;
                    } else {
                        handGestures[0] = Hand.RightHand_LowerThanHead;
                    }
                }
            }

            else if((skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.SHOULDER_RIGHT)) &&
                    (skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.ELBOW_RIGHT)) &&
                    (skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.HEAD)) &&
                    handGestures[1] ==  Hand.LeftHand_Idle){



                if(((skeleton.get3DJointX(Skeleton.HAND_RIGHT)) > skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) - 0.1) &&
                        ((skeleton.get3DJointX(Skeleton.HAND_RIGHT)) < skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) + 0.1) &&
                        ((skeleton.get3DJointX(Skeleton.ELBOW_RIGHT)) > skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) - 0.1) &&
                        ((skeleton.get3DJointX(Skeleton.ELBOW_RIGHT)) < skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) + 0.1)){

                    //System.out.println(skeleton.get3DJointZ(Skeleton.HAND_RIGHT));
                    if(((skeleton.get3DJointZ(Skeleton.HAND_RIGHT)) < skeleton.get3DJointZ(Skeleton.SHOULDER_RIGHT) + 0.1) &&
                            ((skeleton.get3DJointZ(Skeleton.HAND_RIGHT)) > skeleton.get3DJointZ(Skeleton.SHOULDER_RIGHT) - 0.1) &&
                            ((skeleton.get3DJointZ(Skeleton.ELBOW_RIGHT)) < skeleton.get3DJointZ(Skeleton.SHOULDER_RIGHT) + 0.1) &&
                            ((skeleton.get3DJointZ(Skeleton.ELBOW_RIGHT)) > skeleton.get3DJointZ(Skeleton.SHOULDER_RIGHT) - 0.1)){
                        System.out.println("Rechte Hand ausgestreckt im Toleranzbereich");
                        handGestures[0] = Hand.RightHand_StretchedUp;
                    }
                }

            }
        }

        // Wenn sich die linke Hand oberhalb der Höhe des Nackens befindet
        else if(skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.SPINE_MID)) {
            // Wenn die rechte Hand weiter vom Körper entfernt ist als die linke Hand,
            // wird die rechte Hand als zeigende Hand angesehen
            if (skeleton.get3DJointZ(Skeleton.HAND_RIGHT) < skeleton.get3DJointZ(Skeleton.HAND_LEFT) && handGestures[0] != Hand.RightHand_Idle) {

                // Wenn die linke Hand sich oberhalb der Höhe des Kopfes befindet
                if (skeleton.get3DJointY(Skeleton.HAND_LEFT) >= skeleton.get3DJointY(Skeleton.NECK)) {
                    handGestures[0] = Hand.RightHand_Pointer;



                    // Wenn die linke Hand zuvor unterhalb des Kopfes war
                    if (previousHandGestures[skeletonID][1] == Hand.LeftHand_LowerThanHead) {
                        handGestures[1] = Hand.LeftHand_HigherThanHead_Upward;
                    } else {
                        handGestures[1] = Hand.LeftHand_HigherThanHead;
                    }
                }

                // Wenn die linke Hand sich unterhalb der Höhe des Kopfes befindet
                else if (skeleton.get3DJointY(Skeleton.HAND_LEFT) < skeleton.get3DJointY(Skeleton.NECK)) {
                    handGestures[0] = Hand.RightHand_Pointer;

                    // Wenn die linke Hand zuvor oberhalb des Kopfes war
                    if (previousHandGestures[skeletonID][1] == Hand.LeftHand_HigherThanHead) {
                        handGestures[1] = Hand.LeftHand_LowerThanHead_Downward;
                    } else {
                        handGestures[1] = Hand.LeftHand_LowerThanHead;
                    }
                }
            }

            else if((skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.SHOULDER_LEFT)) &&
                    (skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.ELBOW_LEFT)) &&
                    (skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.HEAD)) &&
                    handGestures[0] ==  Hand.RightHand_Idle){

                if(((skeleton.get3DJointX(Skeleton.HAND_LEFT)) > skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) - 0.1) &&
                        ((skeleton.get3DJointX(Skeleton.HAND_LEFT)) < skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) + 0.1) &&
                        ((skeleton.get3DJointX(Skeleton.ELBOW_LEFT)) > skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) - 0.1) &&
                        ((skeleton.get3DJointX(Skeleton.ELBOW_LEFT)) < skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) + 0.1)){

                    //System.out.println(skeleton.get3DJointZ(Skeleton.HAND_RIGHT));
                    if(((skeleton.get3DJointZ(Skeleton.HAND_LEFT)) < skeleton.get3DJointZ(Skeleton.SHOULDER_LEFT) + 0.1) &&
                            ((skeleton.get3DJointZ(Skeleton.HAND_LEFT)) > skeleton.get3DJointZ(Skeleton.SHOULDER_LEFT) - 0.1) &&
                            ((skeleton.get3DJointZ(Skeleton.ELBOW_LEFT)) < skeleton.get3DJointZ(Skeleton.SHOULDER_LEFT) + 0.1) &&
                            ((skeleton.get3DJointZ(Skeleton.ELBOW_LEFT)) > skeleton.get3DJointZ(Skeleton.SHOULDER_LEFT) - 0.1)){
                        System.out.println("Linke Hand ausgestreckt im Toleranzbereich");
                        handGestures[1] = Hand.LeftHand_StretchedUp;
                    }

                }
            }
        }
        if(handGestures[0] != Hand.Unknown_State){
            previousHandGestures[skeletonID][0] = handGestures[0];
        }

        if(handGestures[1] != Hand.Unknown_State){
            previousHandGestures[skeletonID][1] = handGestures[1];
        }

        return handGestures;
    }*/

    /** Methode zum Ermitteln von Hand-Positionen.
     *
     * Diese Methode ermittelt anhand der gelieferten Skelett-Daten die Positionen der linken und rechten Hand.
     * Die Hand-Positionen werden als Hand[]-Array zurückgegeben, wobei [0] die rechte Hand und [1] die linke Hand
     * kennzeichnet.
     *
     * @param skeleton Skelett-Daten zum Ermitteln von Hand-Positionen
     * @return Ermittelte Hand-Positionen
     */
    public static Hand[] getHandPosition(Skeleton skeleton){
        // ID des erhaltenen Skelettes
        int skeletonID = skeleton.getPlayerID();

        Hand[] handGestures = new Hand[2];

        // Hand-Positionen werden grundsätzlich zuerst als Idle gekennzeichnet
        handGestures[0] = Hand.Unknown_State;
        handGestures[1] = Hand.Unknown_State;


        // Wenn sich beiden Hände unterhalb der Wirbelsäulen-Mitte befinden, bleiben die Handpositionen auf Idle
        if((skeleton.get3DJointY(Skeleton.HAND_RIGHT) <= skeleton.get3DJointY(Skeleton.SPINE_MID))){
            handGestures[0] = Hand.RightHand_Idle;
        }

        if(skeleton.get3DJointY(Skeleton.HAND_LEFT) <= skeleton.get3DJointY(Skeleton.SPINE_MID)){
            handGestures[1] = Hand.LeftHand_Idle;
        }

        // Wenn sich beide Hände oberhalb des Kopfes befinden
        if((skeleton.get3DJointY(Skeleton.HAND_RIGHT) >= skeleton.get3DJointY(Skeleton.HEAD)) &&
                (skeleton.get3DJointY(Skeleton.HAND_LEFT) >= skeleton.get3DJointY(Skeleton.HEAD))){

            // Wenn sich beide Hände kreuzen (Hände stehen weiterhin auf Unknown, wenn dieser Fall nicht eintritt)
            if((skeleton.get3DJointX(Skeleton.HAND_RIGHT) <= skeleton.get3DJointX(Skeleton.HAND_LEFT)) &&
                    (skeleton.get3DJointX(Skeleton.HAND_LEFT) >= skeleton.get3DJointX(Skeleton.HAND_RIGHT))){

                handGestures[0] = Hand.BothHands_HigherThanHead_Crossed;
                handGestures[1] = Hand.BothHands_HigherThanHead_Crossed;
            }

            // Wenn sich beide Hände jeweils links und rechts von der jeweiligen Schulter befinden
            else if((skeleton.get3DJointX(Skeleton.ELBOW_RIGHT) > skeleton.get3DJointX(Skeleton.SHOULDER_RIGHT) + 0.1) &&
                    (skeleton.get3DJointX(Skeleton.ELBOW_LEFT) < skeleton.get3DJointX(Skeleton.SHOULDER_LEFT) - 0.1)){

                // Wenn die Hände zuvor als Idle gekennzeichnet waren
                if((previousHandGestures[skeletonID][0] == Hand.BothHands_HigherThanHead_Crossed) &&
                        previousHandGestures[skeletonID][1] == Hand.BothHands_HigherThanHead_Crossed){
                    handGestures[0] = Hand.BothHands_HigherThanHead_SidewardOutside;
                    handGestures[1] = Hand.BothHands_HigherThanHead_SidewardOutside;
                }

                // Wenn die Hände zuvor nicht als Idle gekennzeichnet waren
                else{
                    handGestures[0] = Hand.BothHands_HigherThanHead_Outside;
                    handGestures[1] = Hand.BothHands_HigherThanHead_Outside;
                }
            }
        }

        // Wenn sich die rechte Hand oberhalb der Höhe des Nackens befindet
        else if(skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.NECK)) {
            // Wenn die linke Hand weiter vom Körper entfernt ist als die rechte Hand,
            // wird die linke Hand als zeigende Hand angesehen
            if (skeleton.get3DJointZ(Skeleton.HAND_LEFT) < skeleton.get3DJointZ(Skeleton.HAND_RIGHT) && handGestures[1] != Hand.LeftHand_Idle) {

                // Wenn die rechte Hand sich oberhalb der Höhe des Kopfes befindet
                if (skeleton.get3DJointY(Skeleton.HAND_RIGHT) >= skeleton.get3DJointY(Skeleton.HEAD)) {
                    handGestures[1] = Hand.LeftHand_Pointer;

                    // Wenn die rechte Hand zuvor unterhalb des Kopfes war
                    if (previousHandGestures[skeletonID][0] == Hand.RightHand_LowerThanHead) {
                        handGestures[0] = Hand.RightHand_HigherThanHead_Upward;
                    } else  {
                        handGestures[0] = Hand.RightHand_HigherThanHead;
                    }
                }

                // Wenn die rechte Hand sich unterhalb der Höhe des Kopfes befindet
                else if (skeleton.get3DJointY(Skeleton.HAND_RIGHT) < skeleton.get3DJointY(Skeleton.HEAD)) {
                    handGestures[1] = Hand.LeftHand_Pointer;

                    // Wenn die Hand zuvor oberhalb des Kopfes war
                    if (previousHandGestures[skeletonID][0] == Hand.RightHand_HigherThanHead) {
                        handGestures[0] = Hand.RightHand_LowerThanHead_Downward;
                    } else {
                        handGestures[0] = Hand.RightHand_LowerThanHead;
                    }
                }
            }

            else if((skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.SHOULDER_RIGHT)) &&
                    (skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.ELBOW_RIGHT)) &&
                    (skeleton.get3DJointY(Skeleton.HAND_RIGHT) > skeleton.get3DJointY(Skeleton.HEAD)) &&
                    handGestures[1] ==  Hand.LeftHand_Idle){

                handGestures[0] = Hand.RightHand_StretchedUp;
            }
        }

        // Wenn sich die linke Hand oberhalb der Höhe des Nackens befindet
        else if(skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.NECK)) {

            // Wenn die rechte Hand weiter vom Körper entfernt ist als die linke Hand,
            // wird die rechte Hand als zeigende Hand angesehen
            if (skeleton.get3DJointZ(Skeleton.HAND_RIGHT) < skeleton.get3DJointZ(Skeleton.HAND_LEFT) && handGestures[0] != Hand.RightHand_Idle) {

                // Wenn die linke Hand sich oberhalb der Höhe des Kopfes befindet
                if (skeleton.get3DJointY(Skeleton.HAND_LEFT) >= skeleton.get3DJointY(Skeleton.HEAD)) {
                    handGestures[0] = Hand.RightHand_Pointer;

                    // Wenn die linke Hand zuvor unterhalb des Kopfes war
                    if (previousHandGestures[skeletonID][1] == Hand.LeftHand_LowerThanHead) {
                        handGestures[1] = Hand.LeftHand_HigherThanHead_Upward;
                    } else {
                        handGestures[1] = Hand.LeftHand_HigherThanHead;
                    }
                }

                // Wenn die linke Hand sich unterhalb der Höhe des Kopfes befindet
                else if (skeleton.get3DJointY(Skeleton.HAND_LEFT) < skeleton.get3DJointY(Skeleton.HEAD)) {
                    handGestures[0] = Hand.RightHand_Pointer;

                    // Wenn die linke Hand zuvor oberhalb des Kopfes war
                    if (previousHandGestures[skeletonID][1] == Hand.LeftHand_HigherThanHead) {
                        handGestures[1] = Hand.LeftHand_LowerThanHead_Downward;
                    } else {
                        handGestures[1] = Hand.LeftHand_LowerThanHead;
                    }
                }
            }

            else if((skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.SHOULDER_LEFT)) &&
                    (skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.ELBOW_LEFT)) &&
                    (skeleton.get3DJointY(Skeleton.HAND_LEFT) > skeleton.get3DJointY(Skeleton.HEAD)) &&
                    handGestures[0] ==  Hand.RightHand_Idle){

                handGestures[1] = Hand.LeftHand_StretchedUp;

            }
        }
        if(handGestures[0] != Hand.Unknown_State){
            previousHandGestures[skeletonID][0] = handGestures[0];
        }

        if(handGestures[1] != Hand.Unknown_State){
            previousHandGestures[skeletonID][1] = handGestures[1];
        }

        return handGestures;
    }
}

