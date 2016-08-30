package kinector.fsm;

import akka.actor.UntypedActor;
import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;
import messages.HelperEnums.Hand;
import messages.SingleSkeletonMessage;
import messages.SkeletonMessage;

import java.util.List;

/**
 * Created by Marc on 24.08.2016.
 */
abstract class AbstractGestureRecognizerFSM extends UntypedActor
{
    protected abstract void handleSkeleton(Skeleton skeleton, Hand hand);

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof SingleSkeletonMessage) {
            handleSkeleton(((SingleSkeletonMessage)message).skeleton, ((SingleSkeletonMessage)message).hand);
        }

        unhandled(message);
    }
}
