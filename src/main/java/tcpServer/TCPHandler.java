package tcpServer;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.io.Tcp;
import akka.util.ByteIterator;
import akka.util.ByteString;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.SkeletonMessage;

import java.nio.ByteOrder;
import java.util.ArrayList;

/**
 * Created by Marc on 14.08.2016.
 */
public class TCPHandler extends UntypedActor {

    private ActorRef dispatchActor;

    public TCPHandler(ActorRef dispatchActor)
    {
        this.dispatchActor = dispatchActor;
    }

    public static Props props(ActorRef dispatchActor)
    {
        return Props.create(TCPHandler.class, dispatchActor);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof Tcp.Received)
        {
            final ByteString recv = ((Tcp.Received)message).data();
            final ByteIterator it = recv.iterator();

            float jointData[] = new float[Skeleton.JOINT_COUNT * 3];
            for(int i=0; i<jointData.length; i++)
            {
                jointData[i] = it.getFloat(ByteOrder.nativeOrder());
            }

            Skeleton skeleton = new Skeleton();
            skeleton.setPlayerID(0);
            skeleton.setIsTracked(true);
            skeleton.setJointPositions(jointData);

            ArrayList<Skeleton> skeletons = new ArrayList<>();
            skeletons.add(skeleton);

            dispatchActor.tell(new SkeletonMessage(skeletons), getSelf());
        }
        else if(message instanceof Tcp.ConnectionClosed)
        {
            getContext().stop(getSelf());
        }
    }
}
