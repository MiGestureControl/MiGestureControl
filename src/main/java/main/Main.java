package main;

import akka.actor.*;

/**
 * Created by hagen on 01.02.16.
 */
public class Main {

    public static void main(String[] args) {

        final ActorSystem system = ActorSystem.create("mySystem");
        final ActorRef dispatcher = system.actorOf(Props.create(DispatchActor.class), "Dispatcher");

    }

}
