package messages;

import messages.HelperEnums.DeviceState;

/**
 * Created by johan on 19.04.2016.
 */
public class SetAllDevicesMessage {
    public final DeviceState state;

    public SetAllDevicesMessage(DeviceState state) {
        this.state = state;
    }


}
