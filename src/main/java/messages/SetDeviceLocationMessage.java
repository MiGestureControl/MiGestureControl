package messages;

import java.io.Serializable;

/**
 * Created by johan on 19.04.2016.
 */
public class SetDeviceLocationMessage implements Serializable {
    public final String id;
    public final double[] point;

    public SetDeviceLocationMessage(String id, double[] point){
        this.id = id;
        this.point = point;
    }
}
