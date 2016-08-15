package tcpServer;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.io.TcpMessage;

import java.net.InetSocketAddress;

/**
 * Created by Marc on 14.08.2016.
 */
public class TCPServer extends UntypedActor {

    private ActorRef dispatchActor;

    public TCPServer(ActorRef dispatchActor)
    {
        this.dispatchActor = dispatchActor;
    }

    public static Props props(ActorRef tcpManager)
    {
        return Props.create(TCPServer.class, tcpManager);
    }

    @Override
    public void preStart() throws Exception {
        final ActorRef tcp = Tcp.get(getContext().system()).manager();
        tcp.tell(TcpMessage.bind(getSelf(), new InetSocketAddress("localhost", 5555), 100), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof Tcp.Connected) {
            final Tcp.Connected conn = (Tcp.Connected)message;
            final ActorRef handler = getContext().actorOf(TCPHandler.props(dispatchActor));
            getSender().tell(TcpMessage.register(handler), getSelf());
        }
    }
}
