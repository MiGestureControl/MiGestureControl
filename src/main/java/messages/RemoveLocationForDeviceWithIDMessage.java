package messages;

import java.io.Serializable;

/**
 * Created by hagen on 19/04/16.
 */

/**
 * Diese Nachricht dient zum Entfernen der Konfuration der Location des Gerätes der id. Sie wird vom DeviceManagementActor behandelt
 */
public class RemoveLocationForDeviceWithIDMessage implements Serializable {
    public final String id;

    /**
     * Entfernen der Location des Geräts
     * @param id id des Geräts
     */
    public RemoveLocationForDeviceWithIDMessage(String id) {
        this.id = id;
    }
}
