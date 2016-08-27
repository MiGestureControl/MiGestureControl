package kinector.fsm;

import akka.actor.UntypedActor;
import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;
import messages.SkeletonMessage;

import java.util.List;

/**
 * Created by Marc on 24.08.2016.
 */
abstract class AbstractGestureRecognizerFSM extends UntypedActor
{
    protected abstract void handleSkeletons(List<Skeleton> skeletons);

    @Override
    public void onReceive(Object message) throws Exception {
        if(message instanceof SkeletonMessage) {
            handleSkeletons(((SkeletonMessage)message).skeletons);
        }

        unhandled(message);
    }
}
