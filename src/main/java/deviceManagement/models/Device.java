package deviceManagement.models;

import messages.SetDeviceStateMessage;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by hagen on 28/03/16.
 */
public class Device implements Switcheble{
    public String id;

//    public double[] point;

    public FS20State state;

    public Double locationX = 0.0;
    public Double locationY = 0.0;
    public Double locationZ = 0.0;


    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", state=" + state +
                ", locationX=" + locationX +
                ", locationY=" + locationY +
                ", locationZ=" + locationZ +
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
