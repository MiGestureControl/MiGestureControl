package messages;

/**
 * Created by hagen on 02/02/16.
 */
public class SetDeviceStateMessage {
    public final String deviceID;
    public final String state;
    public final String arg;

    @Override
    public String toString() {
        return "SetDeviceStateMessage{" +
                "deviceID='" + deviceID + '\'' +
                ", state='" + state + '\'' +
                ", arg='" + arg + '\'' +
                '}';
    }

    public SetDeviceStateMessage(String deviceID, String state, String arg) {
        this.deviceID = deviceID;
        this.state = state;
        this.arg = arg;
    }
}
