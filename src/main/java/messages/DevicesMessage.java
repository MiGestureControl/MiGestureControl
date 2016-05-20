package messages;

import deviceManagement.models.Device;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Created by hagen on 18/04/16.
 */
public final class DevicesMessage implements Serializable {

    public final Hashtable<String, Device> devices;

    public DevicesMessage(Hashtable<String, Device> devices) {
        this.devices = devices;
    }
}
