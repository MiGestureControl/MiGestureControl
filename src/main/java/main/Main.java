package main;

import akka.actor.*;
import connector.FhemConector;
import kinector.GestureRecognizer;
import kinector.GestureInterpreter;
import kinector.Kinector;
import messages.GetDevices;
import models.DeviceModel;
import scala.concurrent.duration.Duration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by hagen on 01.02.16.
 */
public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("ff");


        final ActorSystem system = ActorSystem.create("mySystem");
        final Inbox inbox = Inbox.create(system);

        final ActorRef fhemConector = system.actorOf(Props.create(FhemConector.class), "FhemConector");
        final ActorRef deviceModel = system.actorOf(Props.create(DeviceModel.class), "DeviceModel");
        final ActorRef gestureInterpreter = system.actorOf(Props.create(GestureInterpreter.class), "GestureInterpreter");
        final ActorRef gestureRecognizer = system.actorOf(Props.create(GestureRecognizer.class, gestureInterpreter), "GestureRegognizer");


        //greeter.tell(new GetDevices("akka"), ActorRef.noSender());

        system.scheduler().
                schedule(Duration.Zero(), Duration.create(5, TimeUnit.SECONDS), fhemConector, new GetDevices(" "), system.dispatcher(), deviceModel);
        final Kinector kinector = new Kinector(gestureRecognizer);
    }

}
