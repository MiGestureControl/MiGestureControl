package messages;

import java.io.Serializable;

/**
 * Created by johan on 19.04.2016.
 */
public class ConfigureDeviceWithIDMessage implements Serializable {
    public final String id;

    public ConfigureDeviceWithIDMessage(String id){
        this.id = id;
    }
}
