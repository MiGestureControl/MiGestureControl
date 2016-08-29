package messages;

import messages.HelperEnums.DeviceState;

/**
 * Created by johan on 19.04.2016.
 */

/**
 * Setzen des States aller Geräte über den DeviceManagementActor
 */
public class SetAllDevicesMessage {
    //der zu setzende sate
    public final DeviceState state;

    /**
     * Konstruktor zur Erzeugung der Nachricht mit dem Sate
     * @param state zu setzende sate
     */
    public SetAllDevicesMessage(DeviceState state) {
        this.state = state;
    }


}
