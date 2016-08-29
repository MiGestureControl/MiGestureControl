package messages;

import messages.HelperEnums.Hand;

import java.io.Serializable;

/**
 * Created by hagen on 19/04/16.
 */

/**
 * Diese Nachricht wird vom HTTPServer gesendet und versetzt den GestureInterpreter in den Konfigurationsmodus
 */
public class ConfigureDeviceWithIDMessage implements Serializable {
    public final String id;
    public final Hand hand;

    /**
     * Konsruktor mit hand Und Geräte id
     * @param id Geräte id
     * @param hand zu konfiguriende Hand
     */
    public ConfigureDeviceWithIDMessage(String id, Hand hand){
        this.id = id;
        this.hand = hand;
    }

    /**
     * Konstuktor für die rechte Hand
     * @param id
     */
    public ConfigureDeviceWithIDMessage(String id){
        this.id = id;
        this.hand = Hand.RIGHT;
    }
}
