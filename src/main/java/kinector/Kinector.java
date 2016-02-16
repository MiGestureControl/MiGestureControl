package kinector;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.SkeletonMessage;

public class Kinector extends J4KSDK {

    private final ActorRef actor;

    public Kinector(ActorRef actor){
        this.actor = actor;


        start(J4KSDK.COLOR|J4KSDK.DEPTH|J4KSDK.SKELETON);
    }

    @Override
    public void onColorFrameEvent(byte[] arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDepthFrameEvent(short[] arg0, byte[] arg1, float[] arg2, float[] arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSkeletonFrameEvent(boolean[] flags, float[] positions, float[] orientations, byte[] state) {
        for(int i=0;i<getSkeletonCountLimit();i++)
        {
            Skeleton skeleton = Skeleton.getSkeleton(i, flags,positions, orientations,state,this);
            actor.tell(new SkeletonMessage(skeleton), ActorRef.noSender());
        }
    }
}
