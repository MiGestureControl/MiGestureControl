package messages;

import edu.ufl.digitalworlds.j4k.Skeleton;

import java.io.Serializable;

/**
 * Created by johan on 16.02.2016.
 */
public class SkeletonMessage implements Serializable {
    public final Skeleton skeleton;

    public SkeletonMessage(Skeleton skeleton) {
        this.skeleton = skeleton;
    }

    @Override
    public String toString() {
        return "SkeletonMessage{" +
                "skeleton=" + skeleton +
                '}';
    }
}
