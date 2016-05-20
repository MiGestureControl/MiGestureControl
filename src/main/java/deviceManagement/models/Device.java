package deviceManagement.models;

import messages.SetDeviceStateMessage;
import org.boon.Str;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hagen on 28/03/16.
 */
public class Device implements Switcheble{
    public String id;

    public List<PossibleSet> possibleSets = new ArrayList<>();

    public FS20State state;

    public Double locationX_Left = Double.MAX_VALUE;
    public Double locationY_Left = Double.MAX_VALUE;
    public Double locationZ_Left = Double.MAX_VALUE;

    public Double locationX_Right = Double.MAX_VALUE;
    public Double locationY_Right = Double.MAX_VALUE;
    public Double locationZ_Right = Double.MAX_VALUE;


    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", possibleSets=" + possibleSets +
                ", state=" + state +
                ", locationX_Left=" + locationX_Left +
                ", locationY_Left=" + locationY_Left +
                ", locationZ_Left=" + locationZ_Left +
                ", locationX_Right=" + locationX_Right +
                ", locationY_Right=" + locationY_Right +
                ", locationZ_Right=" + locationZ_Right +
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
