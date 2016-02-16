package main;

import akka.actor.*;
import connector.FhemConector;
import kinector.GestureRecognizer;
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

        /*String directory = new File(".").getCanonicalPath();

        if(System.getProperty("os.arch").toLowerCase().indexOf("64")<0)
        {
            File j4k_x86 = new File (directory+"\\lib\\native-libs\\x86\\ufdw_j4k_32bit.dll");
            File j4k_x86_2 = new File (directory+"\\lib\\native-libs\\x86\\ufdw_j4k2_32bit.dll");

            System.load(j4k_x86.getAbsolutePath());
            System.load(j4k_x86_2.getAbsolutePath());
        }

        else{
            File j4k_x64 = new File (directory+"\\lib\\native-libs\\x86_64\\ufdw_j4k_64bit.dll");
            File j4k_x64_2 = new File (directory+"\\lib\\native-libs\\x86_64\\ufdw_j4k2_64bit.dll");

            System.load(j4k_x64.getAbsolutePath());
            System.load(j4k_x64_2.getAbsolutePath());
        }*/

        System.out.println("ff");


        final ActorSystem system = ActorSystem.create("mySystem");
        final Inbox inbox = Inbox.create(system);

        final ActorRef fhemConector = system.actorOf(Props.create(FhemConector.class), "FhemConector");
        final ActorRef deviceModel = system.actorOf(Props.create(DeviceModel.class), "DeviceModel");
        final ActorRef GestureRecognizer = system.actorOf(Props.create(GestureRecognizer.class), "GestureRegognizer");

        //greeter.tell(new GetDevices("akka"), ActorRef.noSender());

        system.scheduler().
                schedule(Duration.Zero(), Duration.create(5, TimeUnit.SECONDS), fhemConector, new GetDevices(" "), system.dispatcher(), deviceModel);
        final Kinector kinector = new Kinector(GestureRecognizer);
    }

}
