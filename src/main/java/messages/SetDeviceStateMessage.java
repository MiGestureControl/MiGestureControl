package messages;

/**
 * Created by hagen on 02/02/16.
 */

/**
 * Dieser nachricht enthält die id des Gerätes den zu veränderen State und das Agurment
 * Diese wird vom FhemConnectorActor empfangen, dieser sendet den Befhel an FHEM
 */
public class SetDeviceStateMessage {

    public final String deviceID;
    public final String state;
    public final String arg;


    /**
     * Erzeugt eine Formatierte Ausgabe der Nachright
     * @return Formatierte Ausgabe
     */
    @Override
    public String toString() {
        return "SetDeviceStateMessage{" +
                "deviceID='" + deviceID + '\'' +
                ", state='" + state + '\'' +
                ", arg='" + arg + '\'' +
                '}';
    }

    /**
     * Konstruktort mit
     * @param deviceID id des Geräts
     * @param state zu setzender State
     * @param arg und dem zu setzenden Argument
     */
    public SetDeviceStateMessage(String deviceID, String state, String arg) {
        this.deviceID = deviceID;
        this.state = state;
        this.arg = arg;
    }
}
