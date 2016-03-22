package kinector;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;
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
    public void onDepthFrameEvent(short[] arg0, byte[] arg1, float[] arg2, float[] arg3) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSkeletonFrameEvent(boolean[] flags, float[] positions, float[] orientations, byte[] state) {
        //for(int i=0;i<getSkeletonCountLimit();i++)
        //{
            //Skeleton skeleton = Skeleton.getSkeleton(i, flags,positions, orientations,state,this);
            ArrayList<Skeleton> skeletons = new ArrayList<Skeleton>(Arrays.asList(this.getSkeletons()));

            /*for(Skeleton skeleton : skeletons){
                if(!skeleton.isTracked()){
                    skeletons.remove(skeleton);
                }
            }*/

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

            //System.out.println(skeleton.isTracked());
            //if(skeleton.isTracked()) {
            actor.tell(new SkeletonMessage(skeletons), ActorRef.noSender());
            //}
        //}
    }
}
