package messages;

import messages.HelperEnums.Hand;

import java.io.Serializable;

/**
 * Created by johan on 19.04.2016.
 */

/**
 * Dieser nachricht enthält die id des Gerätes eine Hand und eine Location
 * Diese wird vom DeviceManagementActor empfangen und dieser speichert die Location
 */
public class SetDeviceLocationMessage implements Serializable {
    //die id des Geräts
    public final String id;

    // genutzte Hand rechts/links
    public final Hand hand;

    //Location
    public final Double locationX;
    public final Double locationY;
    public final Double locationZ;


    /**
     * Erzeugt eine Formatierte Ausgabe der Nachright
     * @return Formatierte Ausgabe
     */
    @Override
    public String toString() {
        return "SetDeviceLocationMessage{" +
                "id='" + id + '\'' +
                ", hand=" + hand +
                ", locationX=" + locationX +
                ", locationY=" + locationY +
                ", locationZ=" + locationZ +
                '}';
    }

    /**
     * Konstruktort mit
     * @param id Id des Geräts
     * @param point Punkt mit der Location
     * @param hand die zu konfigierende Hand
     */
    public SetDeviceLocationMessage(String id, double[] point, Hand hand){
        this.id = id;
        this.locationX = point[0];
        this.locationY = point[1];
        this.locationZ = point[2];
        this.hand = hand;
    }
}
