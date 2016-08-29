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
    String configPath = "config.json";

    /**
     * Klassen deklaration für die Konfiguration
     * wird nirgendwo anders genutzt daher die Deklaration in der klasse
     */
    class MIGCConfig {
        @Override
        public String toString() {
            return "MIGCConfig{" +
                    "devices=" + devices +
                    '}';
        }

        //Beinhaltet im wesenlich die Gerätelist
        List<Device> devices = new ArrayList<Device>();
    }

    /**
     * Konstuktor ohne Konfig
     */
    public DeviceManagementActor() {}
    /**
     * Konstruktor bekommt den Pfad der Konfig übergeben
     * @param configPath
     */
    public DeviceManagementActor(String configPath) {
        //Wenn der String leer ist dann laden wir keine Konfig

        if (configPath.length() > 0) {
            this.configPath= configPath;

            //versuch des ladens
            try {
                File f = new File(configPath);
                if(f.exists() && !f.isDirectory()) {
                    byte[] encoded = Files.readAllBytes(Paths.get(configPath));

                    //ist der Inhalt der Configdatei nicht vorhandne dann parsen wir sie nicht
                    if (encoded.length > 0) {
                        String configString = new String(encoded, StandardCharsets.UTF_8);
                        System.out.println(configPath);
                        //System.out.println(configString);

                        //Mittels ObjectMapper wird die Konfig geparst
                        ObjectMapper mapper =  JsonFactory.create();
                        MIGCConfig config = mapper.fromJson(configString, MIGCConfig.class);
                        System.out.println(config);

                        //die Geräte der Konfig werden der Geräteloste des DeviceManagementActor hinzugefügt
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

    /**
     * Speichern der aktuellen Geräteliste in die Konfigurationsdatei
     */
    private void saveConfig(){
        //Erzeugen der Konfig
        ObjectMapper mapper =  JsonFactory.create();
        MIGCConfig migcConfig = new MIGCConfig();
        migcConfig.devices = new ArrayList<Device>(this.devices.values());

        //Schreiben der Konfig
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

            //liste wird durchlaufen
            for (FhemDevice device : fhemJsonList.getDevices()) {
                String id = device.getName();


                if (devices.containsKey(id)) {
                    //Wenn das Gerät vorhanden ist wird es akutalisiert
                    this.updateFhemDevice(device);
                }else {
                    //Wenn nicht  neu hinzugefügt
                    this.addFhemDevice(device);
                }
            }

            //die neue Liste wird dem Sender zurück geschickt
            getSender().tell(new DevicesMessage(devices), getSelf());

        } else if(message instanceof SetDeviceLocationMessage){
            //setzen des neuen Orts für ein Gerät

            SetDeviceLocationMessage setDeviceLocationMessage = (SetDeviceLocationMessage) message;
            Device device = devices.get(setDeviceLocationMessage.id);

            System.out.println(setDeviceLocationMessage);

            //Setzen der Kordinaten des Gerätes
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

            //wenn das Gerät aktualiset wurde wird die Konfig gesichert
            this.saveConfig();

        } else if(message instanceof SetAllDevicesMessage){
            //setzen des Zustandes aller Geräte
            DeviceState state = ((SetAllDevicesMessage)message).state;

            for (Device device : devices.values()) {
                if(state.equals(DeviceState.ON)){
                    getSender().tell(device.turnOn(), getSelf());
                } else if(state.equals(DeviceState.OFF)){
                    getSender().tell(device.turnOff(), getSelf());
                }
            }
        } else if (message instanceof GetAllDevicesMessage) {

            //liefert dem Sender eine Liste aller Geräte
            getSender().tell(new DevicesMessage(devices),getSelf());

        } else if (message instanceof ConfigureSetForDeviceWithIDMessage) {

            //Speichert den neuen Set für ein Gerät
            Device device = devices.get(((ConfigureSetForDeviceWithIDMessage) message).id);

            if (device != null){
                ActivSets activSets= ((ConfigureSetForDeviceWithIDMessage) message).activSets;
                System.out.println(activSets);
                device.activSets = activSets;
            }

            //wenn das Gerät aktualiset wurde wird die Konfig gesichert
            this.saveConfig();
            //Antworten des Senders
            getSender().tell(new ConfigureDeviceFinishedMessage(), getSelf());
        }
        unhandled(message);
    }

    /**
     *
     * @param fhemDevice
     */
    private  void updateFhemDevice(FhemDevice fhemDevice){
        if (!fhemDevice.getName().startsWith("WEB_") || !fhemDevice.getName().startsWith("FHEMWEB")){

            //Suchen des Gerätes in der Hashtable
//            System.out.println(fhemDevice.getName());
            Device device = devices.get(fhemDevice.getName());

            //setzen des neuen States
            device.state = fhemDevice.getInternals().getSTATE();

            devices.put(device.id, device);
        }
    }


    /**
     * Hinzufügen eines FhemDevice zu der Hashtable
     * @param fhemDevice
     */
    private void addFhemDevice(FhemDevice fhemDevice){
        if (!fhemDevice.getName().startsWith("WEB_") || !fhemDevice.getName().startsWith("FHEMWEB")){
            Device device = new Device();

            device.id = fhemDevice.getName();

            //Durchlaufen der sets und eintragen in das Device
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
