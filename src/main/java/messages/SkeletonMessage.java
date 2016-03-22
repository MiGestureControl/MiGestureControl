package messages;

import edu.ufl.digitalworlds.j4k.Skeleton;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by johan on 16.02.2016.
 */
public class SkeletonMessage implements Serializable {
    public final ArrayList<Skeleton> skeletons;

    public SkeletonMessage(ArrayList<Skeleton> skeletons) {
        this.skeletons = skeletons;
    }

    @Override
    public String toString() {
        return "SkeletonMessage{" +
                "skeleton=" + skeletons +
                '}';
    }
}
