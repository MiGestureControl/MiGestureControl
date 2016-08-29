package messages;

import deviceManagement.models.Device;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by hagen on 18/04/16.
 */

/**
 * Diese Nachricht enthält eine Liste von Geräten als Hashtable und wird vom DeviceManagementActor versand
 */
public final class DevicesMessage implements Serializable {

    /**
     * Diese Hachtables beinhaltet die Geräte als Value und deren ids als Key
     */
    public final Hashtable<String, Device> devices;

    public DevicesMessage(Hashtable<String, Device> devices) {
        this.devices = devices;
    }
}
