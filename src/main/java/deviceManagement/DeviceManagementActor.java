package deviceManagement;

import akka.actor.UntypedActor;
import connector.models.FhemDevice;
import connector.models.FhemJsonList;
import deviceManagement.models.ActivSets;
import deviceManagement.models.Device;
import messages.*;
import messages.HelperEnums.DeviceState;
import deviceManagement.models.PossibleSet;
import messages.HelperEnums.Hand;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by hagen on 28/03/16.
 */
public class DeviceManagementActor extends UntypedActor {

    Hashtable<String, Device> devices = new Hashtable();
    String configPath;

    class MIGCConfig {
        @Override
        public String toString() {
            return "MIGCConfig{" +
                    "devices=" + devices +
                    '}';
        }

        List<Device> devices = new ArrayList<Device>();
    }

    public DeviceManagementActor(String configPath) {
        //Wenn der String leer ist dann laden wir keine Konfig
        if (configPath.length() > 0) {
            this.configPath= configPath;

            try {
                File f = new File(configPath);
                if(f.exists() && !f.isDirectory()) {
                    byte[] encoded = Files.readAllBytes(Paths.get(configPath));

                    //ist der Inhalt der Configdatei nicht vorhandne dann parsen wir sie nicht
                    if (encoded.length > 0) {
                        String configString = new String(encoded, StandardCharsets.UTF_8);
                        System.out.println(configPath);
                        //System.out.println(configString);

                        ObjectMapper mapper =  JsonFactory.create();
                        MIGCConfig config = mapper.fromJson(configString, MIGCConfig.class);
                        System.out.println(config);

                        for (Device dev :config.devices) {
                            this.devices.put(dev.id, dev);
                        }

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }

    private void saveConfig(){
        ObjectMapper mapper =  JsonFactory.create();
        MIGCConfig migcConfig = new MIGCConfig();
        migcConfig.devices = new ArrayList<Device>(this.devices.values());

        try {
            FileOutputStream fos =  new FileOutputStream(new File(configPath));
            mapper.writeValue(fos, migcConfig);

            System.out.println("saveConfig");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReceive(Object message) throws Exception {
        //Liste von Geräten vom Connector
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

            //setzen des neuen Orts für ein Gerät
        } else if(message instanceof SetDeviceLocationMessage){

            SetDeviceLocationMessage setDeviceLocationMessage = (SetDeviceLocationMessage) message;
            Device device = devices.get(setDeviceLocationMessage.id);

            System.out.println(setDeviceLocationMessage);

            if (device != null) {
                if (setDeviceLocationMessage.hand == Hand.LEFT){
                    device.locationX_Left = setDeviceLocationMessage.locationX;
                    device.locationY_Left = setDeviceLocationMessage.locationY;
                    device.locationZ_Left = setDeviceLocationMessage.locationZ;
                } else {
                    device.locationX_Right = setDeviceLocationMessage.locationX;
                    device.locationY_Right = setDeviceLocationMessage.locationY;
                    device.locationZ_Right = setDeviceLocationMessage.locationZ;
                }
            }

            this.saveConfig();

        } else if(message instanceof SetAllDevicesMessage){
            DeviceState state = ((SetAllDevicesMessage)message).state;

            for (Device device : devices.values()) {
                if(state.equals(DeviceState.ON)){
                    getSender().tell(device.turnOn(), getSelf());
                } else if(state.equals(DeviceState.OFF)){
                    getSender().tell(device.turnOff(), getSelf());
                }
            }
        } else if (message instanceof GetAllDevicesMessage) {

            getSender().tell(new DevicesMessage(devices),getSelf());

        } else if (message instanceof ConfigureSetForDeviceWithIDMessage) {
            Device device = devices.get(((ConfigureSetForDeviceWithIDMessage) message).id);

            if (device != null){
                ActivSets activSets= ((ConfigureSetForDeviceWithIDMessage) message).activSets;
                System.out.println(activSets);
                device.activSets = activSets;
            }

            this.saveConfig();

            getSender().tell(new ConfigureDeviceFinishedMessage(), getSelf());
        }
    }

    private  void updateFhemDevice(FhemDevice fhemDevice){
        if (!fhemDevice.getName().startsWith("WEB_") || !fhemDevice.getName().startsWith("FHEMWEB")){

//            System.out.println(fhemDevice.getName());
            Device device = devices.get(fhemDevice.getName());

            device.state = fhemDevice.getInternals().getSTATE();

            devices.put(device.id, device);
        }
    }


    private void addFhemDevice(FhemDevice fhemDevice){
        if (!fhemDevice.getName().startsWith("WEB_") || !fhemDevice.getName().startsWith("FHEMWEB")){
            Device device = new Device();

            device.id = fhemDevice.getName();

            for (String a : fhemDevice.getPossibleSets().split(" ")){
                String[] b = a.split(":");

                if (b.length == 1) {
                    device.possibleSets.add(new PossibleSet(b[0]));
                } else if (b.length == 2) {
                    String[] c = b[1].split(",");
                    PossibleSet possibleSet = new PossibleSet(b[0], Arrays.asList(c));
                    device.possibleSets.add(possibleSet);
                }
            }

            device.state = fhemDevice.getInternals().getSTATE();

//            System.out.println(device);
            //System.out.println(device.id);

            devices.put(device.id, device);
        }
    }



}
