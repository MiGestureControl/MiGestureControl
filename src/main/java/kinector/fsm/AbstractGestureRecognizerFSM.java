package kinector.fsm;

import akka.actor.UntypedActor;
import kinector.GestureRecognizer;

/**
 * Created by Marc on 24.08.2016.
 */
abstract class AbstractGestureRecognizerFSM extends UntypedActor
{
    public abstract GestureRecognizer.Gesture GetRecognizableGesture();
}
