package kinector;

import akka.actor.UntypedActor;
import edu.ufl.digitalworlds.j4k.Skeleton;
import messages.SkeletonMessage;

/**
 * Created by johan on 16.02.2016.
 */
public class GestureRecognizer extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof SkeletonMessage) {
            System.out.println("YAY Skeleton YAY: "+ (SkeletonMessage)message);
        }
    }
}
