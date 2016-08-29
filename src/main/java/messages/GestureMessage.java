package messages;

import edu.ufl.digitalworlds.j4k.Skeleton;
import kinector.GestureRecognizer;

import java.io.Serializable;

/**
 * Created by johan on 20.03.2016.
 */

/**
 * Diese Nachricht wird vom GestureRecognizer erzeugt und enthält eine Geste und ein Skeleton
 */
public class GestureMessage implements Serializable {
    //
    public final GestureRecognizer.Gesture gesture;
    public final Skeleton skeleton;

    /**
     * Konstruktor zur Erzeugung der Nachricht
     * @param gesture die Geste die gesendet werden soll
     * @param skeleton das zu sendene Skelett
     */
    public GestureMessage(GestureRecognizer.Gesture gesture, Skeleton skeleton) {
        this.gesture = gesture;
        this.skeleton = skeleton;
    }
}
