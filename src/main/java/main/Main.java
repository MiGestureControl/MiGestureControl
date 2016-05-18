package main;

import akka.actor.*;
import messages.ConfigureDeviceWithIDMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by hagen on 01.02.16.
 */
public class Main {

    public static void main(String[] args) {

        final ActorSystem system = ActorSystem.create("mySystem");
        final ActorRef dispatcher = system.actorOf(Props.create(DispatchActor.class), "Dispatcher");
    }

}
