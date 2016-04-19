package main;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import connector.FhemConectorActor;
import connector.models.FhemJsonList;
import deviceManagement.DeviceManagementActor;
import deviceManagement.models.DevicesMessage;
import deviceManagement.models.FS20State;
import httpServer.HTTPServer;
import kinector.GestureInterpreter;
import kinector.GestureRecognizer;
import kinector.Kinector;
import messages.*;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Created by hagen on 18/04/16.
 */
public class DispatchActor extends UntypedActor {

    final ActorSystem system = this.getContext().system();

    final ActorRef fhemConector
            = system.actorOf(Props.create(FhemConectorActor.class), "FhemConector");

    final ActorRef deviceManagementActor
            = system.actorOf(Props.create(DeviceManagementActor.class), "deviceManagementActor");

    final ActorRef howToUseDeviceManagementActor
            = system.actorOf(Props.create(deviceManagement.HowToUseDeviceManagementActor.class),  "HowToUseDeviceManagementActor");

    final ActorRef gestureInterpreter = system.actorOf(Props.create(GestureInterpreter.class), "GestureInterpreter");

    final ActorRef gestureRecognizer = system.actorOf(Props.create(GestureRecognizer.class, gestureInterpreter), "GestureRegognizer");
    final Kinector kinector = new Kinector(gestureRecognizer);

    FS20State lastDebounceState;
    long lastDebounceStartTime;
    final long debounceTime = 100000000;




    public DispatchActor() {
        new HTTPServer().bindRoute("127.0.0.1", 8080, system);


        system.scheduler().schedule(
                Duration.Zero(),
                Duration.create(5, TimeUnit.SECONDS),
                fhemConector,
                new GetDevicesMessage(),
                system.dispatcher(),
                getSelf()
        );
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof FhemJsonList) {
            System.out.println("FhemJsonList");

            this.deviceManagementActor.tell(message, getSelf());

        }else if(message instanceof DevicesMessage){
            System.out.println("DevicesMessage");

            this.gestureInterpreter.tell(message, getSelf());

        }else if(message instanceof SetDeviceStateMessage){
            System.out.println("SetDeviceStateMessage");
            System.out.println(((SetDeviceStateMessage) message).deviceID);
            this.fhemConector.tell(message, getSelf());
        } else if(message instanceof SetDeviceLocationMessage){
            System.out.println("SetDeviceLocationMessage");
            this.deviceManagementActor.tell(message, getSelf());
        } else if(message instanceof ConfigureDeviceWithIDMessage) {
            System.out.println("ConfigureDeviceWithIDMessage");
            this.gestureInterpreter.tell(message, getSelf());
        } else if(message instanceof SetAllDevicesMessage){
            if(((SetAllDevicesMessage) message).state != lastDebounceState){
                if(System.nanoTime() + debounceTime > lastDebounceStartTime ){
                    lastDebounceStartTime = System.nanoTime();
                    System.out.println("SetAllDevicesMessage");
                    this.deviceManagementActor.tell(message, getSelf());
                }

                lastDebounceState = ((SetAllDevicesMessage) message).state;
            }

        }


        unhandled(message);
    }
}