package messages;

import java.io.Serializable;

/**
 * Created by hagen on 19/04/16.
 */
public class ConfigureDeviceWithIDMessage implements Serializable {
    public final String id;

    public ConfigureDeviceWithIDMessage(String id){
        this.id = id;
    }
}
