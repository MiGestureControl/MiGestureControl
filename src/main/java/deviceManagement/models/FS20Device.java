package deviceManagement.models;

import messages.SetDeviceStateMessage;

/**
 * Created by hagen on 28/03/16.
 */
public class FS20Device extends Device implements Switcheble {
    public FS20State state;

    @Override
    public String toString() {
        return "FS20Device{" +
                "id='" + id + '\'' +
                " state=" + state +
                '}';
    }

    @Override
    public SetDeviceStateMessage turnOff() {
        return new SetDeviceStateMessage(this.id, "off");
    }

    @Override
    public SetDeviceStateMessage turnOn() {
        return new SetDeviceStateMessage(this.id, "on");
    }

    @Override
    public SetDeviceStateMessage toggle() {
        if (this.state == FS20State.ON) {
            return new SetDeviceStateMessage(this.id, "off");
        }else {
            return new SetDeviceStateMessage(this.id, "on");
        }
    }
}

