package kinector;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.DepthImageMessage;
import messages.SkeletonMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

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
    public void onDepthFrameEvent(short[] depthFrame, byte[] arg1, float[] arg2, float[] arg3) {
        //actor.tell(new DepthImageMessage(depthFrame),ActorRef.noSender());
    }

    @Override
    public void onSkeletonFrameEvent(boolean[] flags, float[] positions, float[] orientations, byte[] state) {

        ArrayList<Skeleton> skeletons = new ArrayList<Skeleton>(Arrays.asList(this.getSkeletons()));

        Iterator<Skeleton> skelIterator = skeletons.iterator();
        while (skelIterator.hasNext()) {
            Skeleton skeleton = skelIterator.next();

            if(!skeleton.isTracked()) {
                skelIterator.remove();
            }
        }

        for(int i = 0; i < skeletons.size(); i++){
            skeletons.get(i).setPlayerID(i);
        }
            actor.tell(new SkeletonMessage(skeletons), ActorRef.noSender());
    }
}
