package kinector;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import deviceManagement.models.Device;
import deviceManagement.models.DevicesMessage;
import deviceManagement.models.FS20State;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.ConfigureDeviceWithIDMessage;
import messages.GestureMessage;
import messages.SetAllDevicesMessage;
import messages.SetDeviceLocationMessage;

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
    boolean configModeActive = false;
    String currentConfigDevice;

    /**
     * Zwischengespeicherte Line zum Bestimmen einer Geräte-Position.
     */
    private Line savedConfigLine;

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

            if (configModeActive &&
                    detectedGesture != GestureRecognizer.Gesture.BothHands_ActivateAll &&
                    detectedGesture != GestureRecognizer.Gesture.BothHands_DeactivateAll) {
                getDevicePosition(skeleton);
            } else {
                interpretGesture(skeleton, detectedGesture);
            }
        } else if(message instanceof DevicesMessage){
            if(dispatcher == null){
                dispatcher = getSender();
            }
            devices = ((DevicesMessage) message).devices;
        } else if(message instanceof ConfigureDeviceWithIDMessage){
            this.currentConfigDevice = ((ConfigureDeviceWithIDMessage) message).id;
            configModeActive = true;
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
    private void interpretGesture(Skeleton skeleton, GestureRecognizer.Gesture gesture) {
        if (gesture != GestureRecognizer.Gesture.BothHands_ActivateAll && gesture != GestureRecognizer.Gesture.BothHands_DeactivateAll) {
            Device device = getDevice(skeleton);
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
    public Device getDevice(Skeleton skeleton) {
        Line line = getPointingLine(skeleton);
        Device detectedDevice = null;

        double maxAngle = 30;

        for (Device device : devices.values()) {
            if(device.point != null) {

                double angleToPoint = Math.abs(line.angleToGivenPoint(device.point));
                if (angleToPoint <= maxAngle) {
                    maxAngle = angleToPoint;
                    detectedDevice = device;
                }
            }
        }
        System.out.println(detectedDevice);
        return detectedDevice;
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
    private void getDevicePosition(Skeleton skeleton) {
        Line line = getPointingLine(skeleton);
        if (savedConfigLine == null) {
            savedConfigLine = line;
            System.out.println("Erste Linie gespeichert. Wechseln Sie die Position und zeigen Sie erneut auf das Objekt.");
        } else {
            double[] point = line.calcLineIntersection(savedConfigLine);
            System.out.println("x: " + point[0] + "y: " + point[1] + "z: " + point[2]);
            savedConfigLine = null;
            configModeActive = false;

            // deviceList.addDevice(new Device("demoDevice", point));
            if(dispatcher != null){
                dispatcher.tell(new SetDeviceLocationMessage(currentConfigDevice, point), getSelf());
            }

        }
    }
}