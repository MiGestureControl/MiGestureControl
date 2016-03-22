package kinector;

import akka.actor.UntypedActor;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.GestureMessage;
import models.Device;
import models.DeviceList;

import static kinector.GestureRecognizer.GetLinePointingToDevice;

/**
 * Created by johan on 20.03.2016.
 */
public class GestureInterpreter extends UntypedActor {

    DeviceList deviceList;

    public GestureInterpreter(){
        deviceList = DeviceList.getDeviceList();
    }
    boolean configActive = true;

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof GestureMessage) {

            Skeleton skeleton = ((GestureMessage) message).skeleton;
            GestureRecognizer.Gesture detectedGesture = ((GestureMessage) message).gesture;

            if (configActive) {

                if (detectedGesture != GestureRecognizer.Gesture.None) {
                        System.out.println("DONE");
                        GetDevicePosition(skeleton, detectedGesture);
                }
            }
            else
            {
                    UpdateDeviceBasedOnGesture(skeleton, detectedGesture);
            }
        }
    }

    private void UpdateDeviceBasedOnGesture(Skeleton skeleton, GestureRecognizer.Gesture gesture)
    {
        if(gesture != GestureRecognizer.Gesture.BothHands_ActivateAll && gesture != GestureRecognizer.Gesture.BothHands_DeactivateAll) {
            Device closestDevice = pointingTowardsFhemDevice(skeleton, gesture);
            if (closestDevice != null) {
                switch (gesture) {
                    case LeftHand_PointingTowardsDevice_DefaultActivate:
                    case RightHand_PointingTowardsDevice_DefaultActivate: {
                        if (!closestDevice.deviceOn) {
                            closestDevice.turnOn();
                        }
                        break;
                    }
                    case LeftHand_PointingTowardsDevice_DefaultDeactivate:
                    case RightHand_PointingTowardsDevice_DefaultDeactivate: {
                        if (closestDevice.deviceOn) {
                            closestDevice.turnOff();
                        }
                        break;
                    }

                    default:
                        break;
                }
            }
        }

        else if(gesture == GestureRecognizer.Gesture.BothHands_ActivateAll){
            for(Device device : deviceList.getDevicesAsArrayList()){
                if (!device.deviceOn) {
                    device.turnOn();
                }
            }
        }

        else if(gesture == GestureRecognizer.Gesture.BothHands_DeactivateAll){
            for(Device device : deviceList.getDevicesAsArrayList()){
                if (device.deviceOn) {
                    device.turnOff();
                }
            }
        }
    }

    public Device pointingTowardsFhemDevice(Skeleton skeleton, GestureRecognizer.Gesture gesture){
        Line line = GetLinePointingToDevice(skeleton, gesture);
        Device pointingToDevice = null;
        double shortestAngle = 20;

        for(Device device : deviceList.getDevicesAsArrayList())
        {
            double angleToPoint = Math.abs(line.angleToGivenPoint(device.getDevicePoint()));
            if (angleToPoint <= shortestAngle)
            {
                shortestAngle = angleToPoint;
                pointingToDevice = device;
            }
        }
        return pointingToDevice;
    }

    private Line savedFirstLine;
    private void GetDevicePosition(Skeleton skeleton, GestureRecognizer.Gesture gesture)
    {
        Line line = GetLinePointingToDevice(skeleton, gesture);
        if (savedFirstLine == null)
        {
            savedFirstLine = line;
            System.out.println("Erste Linie gespeichert. Wechseln Sie die Position und zeigen Sie erneut auf das Objekt.");
        }
        else
        {
            double[] point = line.calcLineIntersection(savedFirstLine);
            System.out.println("x: " + point[0] + "y: " + point[1] + "z: " + point[2]);
            savedFirstLine = null;
            configActive = false;
            deviceList.addDevice(new Device("demoDevice", point));
        }
    }



}
