package deviceManagement.models;

import messages.SetDeviceStateMessage;

public interface Switcheble {
    public SetDeviceStateMessage turnOff();
    public SetDeviceStateMessage turnOn();
    public SetDeviceStateMessage toggle();
}
