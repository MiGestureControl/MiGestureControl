package models;

import java.util.ArrayList;

/**
 * Created by johan on 16.03.2016.
 */
public class DeviceList {
    private static DeviceList deviceList;
    private ArrayList<Device> devices;

    private DeviceList(){
        initializeList();
    }

    private void initializeList(){
        devices = new ArrayList<Device>();

        /*double[] rightPoint = new double[]{-2.0342924332129204, 1.0692246943538926, -0.3915819585502882};
        addDevice(new Device("Laptop", rightPoint));

        double[] leftPoint = new double[]{1.50942199413912, 0.4826545824515349, 0.391209082170634};
        addDevice(new Device("Stuhl", leftPoint));*/
    }

    public static DeviceList getDeviceList(){
        if(deviceList == null){
            deviceList = new DeviceList();
        }
        return deviceList;
    }

    public ArrayList<Device> getDevicesAsArrayList(){
        return devices;
    }

    public void addDevice(Device device){
        devices.add(device);
    }

    public Device getDevice(String id){
        for (Device device : devices) {
            if(device.getId().equals(id)){
                return device;
            }
        }
        return null;
    }

}
