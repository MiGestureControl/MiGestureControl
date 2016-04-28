package deviceManagement;

import akka.actor.UntypedActor;
import connector.models.FhemDevice;
import connector.models.FhemJsonList;
import deviceManagement.models.Device;
import deviceManagement.models.DevicesMessage;
import deviceManagement.models.FS20State;
import messages.GetAllDevicesMessage;
import messages.SetAllDevicesMessage;
import messages.SetDeviceLocationMessage;

import java.util.Hashtable;

/**
 * Created by hagen on 28/03/16.
 */
public class DeviceManagementActor extends UntypedActor {

    Hashtable<String, Device> devices = new Hashtable();

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof FhemJsonList) {
            FhemJsonList fhemJsonList = (FhemJsonList) message;

            for (FhemDevice device : fhemJsonList.getDevices()) {
                String id = device.getName();

                if (devices.containsKey(id)) {
                    this.updateFhemDevice(device);
                }else {
                    this.addFhemDevice(device);
                }
            }

            getSender().tell(new DevicesMessage(devices), getSelf());
        } else if(message instanceof SetDeviceLocationMessage){
            SetDeviceLocationMessage setDeviceLocationMessage = (SetDeviceLocationMessage) message;
            devices.get(setDeviceLocationMessage.id).point = setDeviceLocationMessage.point;
        } else if(message instanceof SetAllDevicesMessage){
            FS20State state = ((SetAllDevicesMessage)message).state;

            for (Device device : devices.values()) {
                if(state.equals(FS20State.ON)){
                    getSender().tell(device.turnOn(), getSelf());
                } else if(state.equals(FS20State.OFF)){
                    getSender().tell(device.turnOff(), getSelf());
                }
            }
        }else if (message instanceof GetAllDevicesMessage) {

            getSender().tell(new DevicesMessage(devices),getSelf());

        }
    }

    private  void updateFhemDevice(FhemDevice fhemDevice){
        if (fhemDevice.getInternals().getTYPE().equals("HUEDevice")){

            System.out.println(fhemDevice.getName());
            Device device = devices.get(fhemDevice.getName());

            if (fhemDevice.getInternals().getSTATE().equals("on")){
                device.state = FS20State.ON;
            } else {
                device.state = FS20State.OFF;
            }

            devices.put(device.id, device);
        }
    }


    private void addFhemDevice(FhemDevice fhemDevice){
        if (fhemDevice.getInternals().getTYPE().equals("HUEDevice")){
            Device device = new Device();

            device.id = fhemDevice.getName();


            if (fhemDevice.getInternals().getSTATE().equals("on")){
                device.state = FS20State.ON;
            } else {
                device.state = FS20State.OFF;
            }

            //System.out.println(device);
            //System.out.println(device.id);

            devices.put(device.id, device);
        }
    }



}
