package messages;

import java.io.Serializable;

/**
 * Created by hagen on 19/04/16.
 */
public class RemoveLocationForDeviceWithIDMessage implements Serializable {
    public final String id;
    public RemoveLocationForDeviceWithIDMessage(String id) {
        this.id = id;
    }
}
