package models;

import akka.actor.UntypedActor;

/**
 * Created by hagen on 02/02/16.
 */
public class DeviceModel extends UntypedActor {

    public void onReceive(Object message) {
        if (message instanceof String){
            System.out.println(message);
        }
    }
}
