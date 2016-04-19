package messages;

/**
 * Created by hagen on 02/02/16.
 */
public class SetDeviceStateMessage {
    public final String deviceID;
    public final String state;

    public SetDeviceStateMessage(String deviceID, String state) {
        this.deviceID = deviceID;
        this.state = state;
    }
}
