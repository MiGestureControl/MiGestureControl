package deviceManagement;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import deviceManagement.models.Device;
import deviceManagement.models.DevicesMessage;
import deviceManagement.models.FS20Device;

import java.util.Hashtable;
import java.util.Random;

/**
 * Created by hagen on 18/04/16.
 */
public class HowToUseDeviceManagementActor extends UntypedActor {

    ActorRef managementActor;

    Random random = new Random();

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof DevicesMessage) {

            Hashtable<String, Device> devices = ((DevicesMessage) message).devices;

            FS20Device d = (FS20Device) devices.values()
                    .toArray()[random.nextInt(devices.size())];

            getSender().tell(d.toggle(), getSelf());
        }
    }
}
