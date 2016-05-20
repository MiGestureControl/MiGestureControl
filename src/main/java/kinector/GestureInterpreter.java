package kinector;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import deviceManagement.models.Device;
import messages.DevicesMessage;
import deviceManagement.models.FS20State;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.*;

import javax.sound.sampled.*;
import java.io.File;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import static kinector.GestureRecognizer.getPointingLine;

/** Aktor-Klasse zum Interpretieren von erkannten Gesten.
 *
 * Diese Klasse dient als Aktor und behandelt GestureMessages. Anhand der erhaltenen Gesten-Daten werden Methode-Aufrufe
 * zum Ändern von Geräte-Zuständen durchgeführt. Zudem können neue Geräte-Positionen im Konfigurationsmodus ermittelt werden.
 */
public class GestureInterpreter extends UntypedActor {

    ActorRef dispatcher;

    Hashtable<String, Device> devices;

    /**
     * Boolean, der Kennzeichnet, ob sich der Interpreter im Konfigurationsmodus befindet.
     */
    boolean HandConfigModeActive_RightHand = false;
    boolean HandConfigModeActive_LeftHand = false;

    /**
     * Zwischengespeicherte Line zum Bestimmen einer Geräte-Position.
     */
    private Line savedConfigLine_Right;
    private Line savedConfigLine_Left;

    ActorRef tempConfigActor;

    String currentConfigDevice;



    /**
     * Aktor-Methode zum Empfangen von Aktor-Nachrichten.
     * <p/>
     * Diese Methode empfängt Aktor-Nachrichten und überprüft, ob es sich bei einer erhaltenen Nachricht um
     * eine GestureMessage handelt. Ist der GestureInterpreter im Konfigurationsmodus, wird eine Methode zum Bestimmen
     * einer Geräte-Position aufgerufen. Ist dies nicht der Fall wird die Geste mitsamt des zugehörigen Skelettes an
     * eine Methode zum Auslösen Geräte-Statusänderungen weitergegeben.
     *
     * @param message Empfangene Aktor-Nachricht
     */
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof GestureMessage) {

            // Empfangene Skelett-Daten
            Skeleton skeleton = ((GestureMessage) message).skeleton;

            // Empfangene Gesten-Daten
            GestureRecognizer.Gesture detectedGesture = ((GestureMessage) message).gesture;

            // Abrufen der Hand-Gesten
            GestureRecognizer.Hand[] handGestures = GestureRecognizer.getHandPosition(skeleton);

            if (HandConfigModeActive_RightHand &&
                    (detectedGesture != GestureRecognizer.Gesture.BothHands_ActivateAll ||
                    detectedGesture != GestureRecognizer.Gesture.BothHands_DeactivateAll)) {
                getDevicePosition(skeleton, handGestures);
            } else if(HandConfigModeActive_LeftHand &&
                    (detectedGesture != GestureRecognizer.Gesture.BothHands_ActivateAll ||
                            detectedGesture != GestureRecognizer.Gesture.BothHands_DeactivateAll)) {
                getDevicePosition(skeleton, handGestures);
            } else {
                interpretGesture(skeleton, detectedGesture, handGestures);
            }
        } else if(message instanceof DevicesMessage){
            if(dispatcher == null){
                dispatcher = getSender();
            }
            devices = ((DevicesMessage) message).devices;
        } else if(message instanceof ConfigureDeviceWithIDMessage){
            this.currentConfigDevice = ((ConfigureDeviceWithIDMessage) message).id;
            tempConfigActor = getSender();

            if(((ConfigureDeviceWithIDMessage) message).hand == Hand.RIGHT){
                HandConfigModeActive_RightHand = true;
            }

            else if(((ConfigureDeviceWithIDMessage) message).hand == Hand.LEFT){
                HandConfigModeActive_LeftHand = true;
            }
        }
    }

    /**
     * Methode zum Auslösen Geräte-Statusänderungen.
     * <p/>
     * Diese Methode erhält erkannte Gesten sowie das zugehörige Skelett und führt auf Basis dieser Informationen eine
     * Geräte-Statusänderung durch. Hierbei werden die unterschiedlichen auslösenden Gesten-Typen unterschieden und
     * entsprechend eine Statusänderung ausgelöst.
     *
     * @param skeleton Empfangene Skelett-Daten
     * @param gesture  Empfangene Gesten-Daten
     */
    private void interpretGesture(Skeleton skeleton, GestureRecognizer.Gesture gesture, GestureRecognizer.Hand[] handGestures) {
        if (gesture != GestureRecognizer.Gesture.BothHands_ActivateAll && gesture != GestureRecognizer.Gesture.BothHands_DeactivateAll) {
            if (gesture == GestureRecognizer.Gesture.RightHand_StretchedUp) {

                dispatcher.tell(new FlashMessage(), getSelf());

            } else if (gesture == GestureRecognizer.Gesture.LeftHand_StretchedUp) {
                dispatcher.tell(new FlashMessage(), getSelf());
            } else {
                Device device = getDevice(skeleton, handGestures);
                if (device != null) {
                    switch (gesture) {
                        case LeftHand_PointingTowardsDevice_DefaultActivate:
                        case RightHand_PointingTowardsDevice_DefaultActivate: {
                            System.out.println("on");
                            dispatcher.tell(device.turnOn(), getSelf());
                            break;
                        }
                        case LeftHand_PointingTowardsDevice_DefaultDeactivate:
                        case RightHand_PointingTowardsDevice_DefaultDeactivate: {
                            System.out.println("off");
                            dispatcher.tell(device.turnOff(), getSelf());
                            break;
                        }

                        default:
                            break;
                    }
                }
            }
        } else if (gesture == GestureRecognizer.Gesture.BothHands_ActivateAll) {
                 dispatcher.tell(new SetAllDevicesMessage(FS20State.ON), getSelf());
        } else if (gesture == GestureRecognizer.Gesture.BothHands_DeactivateAll) {
                dispatcher.tell(new SetAllDevicesMessage(FS20State.OFF), getSelf());
        }
    }
    /**
     * Methode zum Ermitteln eines Gerätes.
     * <p/>
     * Diese Methode erhält Skelett-Daten und bestimmt anhand dieser mittels eines Aufrufs
     * der statischen Methode getPointingLine(...) eine neue Line ausgehend von der zeigenden Hand.
     * Darauffolgend wird überprüft, ob in Richtung der Line ein Geräte vorhanden ist. Hierfür wird
     * der Winkel zwischen der Line und den Geräte-Positionen berechnet. Das Gerät mit dem geringsten Winkel zur
     * Line wird als erkanntes Gerät zurückgegeben.
     *
     * @param skeleton Empfangene Skelett-Daten
     * @return Erkanntes Gerät
     */
    public Device getDevice(Skeleton skeleton, GestureRecognizer.Hand[] handGestures) {
        Device detectedDevice = null;
        Line line = null;

        // Bestimmen der neuen Line, abhängig davon welcher Hand als zeigende Hand erkannt wurde.
        if (handGestures[0] == GestureRecognizer.Hand.RightHand_Pointer) {

            double maxAngle = 15;
            line = getPointingLine(skeleton, GestureRecognizer.Hand.RightHand_Pointer);

            if(line != null) {
                for (Device device : devices.values()) {
                    if(device.locationX_Right != null &&
                            device.locationY_Right != null &&
                            device.locationZ_Right != null) {

                        double[] point = new double[3];
                        point[0] = device.locationX_Right;
                        point[1] = device.locationY_Right;
                        point[2] = device.locationZ_Right;
                        double angleToPoint = Math.abs(line.angleToGivenPoint(point));

                        if (angleToPoint <= maxAngle) {

                            detectedDevice = device;
                        }
                    }
                }
                System.out.println("Detected device: " + detectedDevice);

                return detectedDevice;
            }
        } else if (handGestures[1] == GestureRecognizer.Hand.LeftHand_Pointer) {

            double maxAngle = 15;
            line = getPointingLine(skeleton, GestureRecognizer.Hand.LeftHand_Pointer);

            if(line != null) {
                for (Device device : devices.values()) {
                    if(device.locationX_Left != null &&
                            device.locationY_Left != null &&
                            device.locationZ_Left != null) {

                        double[] point = new double[3];
                        point[0] = device.locationX_Left;
                        point[1] = device.locationY_Left;
                        point[2] = device.locationZ_Left;
                        double angleToPoint = Math.abs(line.angleToGivenPoint(point));

                        if (angleToPoint <= maxAngle) {

                            detectedDevice = device;
                        }
                    }
                }
                System.out.println("Detected device: " + detectedDevice);

                return detectedDevice;
            }
        }
        return null;
    }

    /**
     * Methode zum Ermitteln eines neuen Geräte-Position.
     * <p/>
     * Diese Methode erhält Skelett-Daten und bestimmt anhand dieser eine neue GerätePosition. Hierfür muss die
     * Methode zwei Mal aufgerufen werden. Beim ersten Mal wird eine Line erzeugt und zwischengespeichert. Beim
     * zweiten Mal wird mit einer weiteren erzeugten Line sowie der gespeicherten Line eine Position errechnet.
     *
     * @param skeleton Empfangene Skelett-Daten
     * @return Erkanntes Gerät
     */
    private void getDevicePosition(Skeleton skeleton, GestureRecognizer.Hand[] handGestures) {

        // Bestimmen der neuen Line, abhängig davon welcher Hand als zeigende Hand erkannt wurde.
        if (handGestures[0] == GestureRecognizer.Hand.RightHand_Pointer) {
            Line line = getPointingLine(skeleton, GestureRecognizer.Hand.RightHand_Pointer);

            if (savedConfigLine_Right == null) {
                savedConfigLine_Right = line;
                System.out.println("Erste Linie (rechte Hand) gespeichert. Wechseln Sie die Position und zeigen Sie erneut auf das Objekt.");
            } else {

                double[] point = savedConfigLine_Right.calcLineIntersection(line);
                System.out.println("x: " + point[0] + "y: " + point[1] + "z: " + point[2]);
                savedConfigLine_Right = null;
                HandConfigModeActive_RightHand = false;

                if(dispatcher != null){
                    dispatcher.tell(new SetDeviceLocationMessage(currentConfigDevice, point, Hand.RIGHT), getSelf());
                }

            }
        }
        else if (handGestures[1] == GestureRecognizer.Hand.LeftHand_Pointer) {
            Line line = getPointingLine(skeleton, GestureRecognizer.Hand.LeftHand_Pointer);

            if (savedConfigLine_Left == null) {
                savedConfigLine_Left = line;
                System.out.println("Erste Linie (linke Hand) gespeichert. Wechseln Sie die Position und zeigen Sie erneut auf das Objekt.");
            } else {

                double[] point = savedConfigLine_Left.calcLineIntersection(line);
                System.out.println("x: " + point[0] + "y: " + point[1] + "z: " + point[2]);
                savedConfigLine_Left = null;
                HandConfigModeActive_LeftHand = false;

                if(dispatcher != null){
                    dispatcher.tell(new SetDeviceLocationMessage(currentConfigDevice, point, Hand.LEFT), getSelf());
                }
            }
        }
    }
}