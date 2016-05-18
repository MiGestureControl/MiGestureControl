package messages;

import java.io.Serializable;

/**
 * Created by johan on 19.04.2016.
 */
public class SetDeviceLocationMessage implements Serializable {
    public final String id;

    public Double locationX;
    public Double locationY;
    public Double locationZ;

    public SetDeviceLocationMessage(String id, double[] point){
        this.id = id;
        this.locationX = point[0];
        this.locationY = point[1];
        this.locationZ = point[2];
    }
}
