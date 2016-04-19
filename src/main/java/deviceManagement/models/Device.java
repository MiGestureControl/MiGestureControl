package deviceManagement.models;

import messages.SetDeviceStateMessage;

import java.util.Arrays;

/**
 * Created by hagen on 28/03/16.
 */
public class Device implements Switcheble{
    public String id;

    public double[] point;

    public FS20State state;

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", point=" + Arrays.toString(point) +
                ", state=" + state +
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
