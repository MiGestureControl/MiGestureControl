package messages;

import deviceManagement.models.FS20State;

/**
 * Created by johan on 19.04.2016.
 */
public class SetAllDevicesMessage {
    public final FS20State state;

    public SetAllDevicesMessage(FS20State state) {
        this.state = state;
    }


}
