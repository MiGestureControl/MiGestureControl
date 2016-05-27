package deviceManagement.models;

import messages.SetDeviceStateMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hagen on 28/03/16.
 */
public class Device{
    public String id;

    public List<PossibleSet> possibleSets = new ArrayList<PossibleSet>();

    public String state;

    public Double locationX_Left = Double.MAX_VALUE;
    public Double locationY_Left = Double.MAX_VALUE;
    public Double locationZ_Left = Double.MAX_VALUE;

    public Double locationX_Right = Double.MAX_VALUE;
    public Double locationY_Right = Double.MAX_VALUE;
    public Double locationZ_Right = Double.MAX_VALUE;

    public ActivSets activSets = new ActivSets();

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", possibleSets=" + possibleSets +
                ", state='" + state + '\'' +
                ", locationX_Left=" + locationX_Left +
                ", locationY_Left=" + locationY_Left +
                ", locationZ_Left=" + locationZ_Left +
                ", locationX_Right=" + locationX_Right +
                ", locationY_Right=" + locationY_Right +
                ", locationZ_Right=" + locationZ_Right +
                ", activSets=" + activSets +
                '}';
    }

    public SetDeviceStateMessage SetForTrunOffGesture() {
        return new SetDeviceStateMessage(this.id, activSets.activSetTrunOffGesture.name);
    }

    public SetDeviceStateMessage SetForTrunOnGesture() {
        return new SetDeviceStateMessage(this.id, activSets.activSetTrunOnGesture.name);
    }

    public SetDeviceStateMessage turnOff() {
        return new SetDeviceStateMessage(this.id, "off");
    }

    public SetDeviceStateMessage turnOn() {
        return new SetDeviceStateMessage(this.id, "on");
    }
}
