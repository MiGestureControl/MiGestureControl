package messages;

import java.io.Serializable;

/**
 * Created by hagen on 19/04/16.
 */
public class ConfigureDeviceWithIDMessage implements Serializable {
    public final String id;
    public final Hand hand;

    public ConfigureDeviceWithIDMessage(String id, Hand hand){
        this.id = id;
        this.hand = hand;
    }

    public ConfigureDeviceWithIDMessage(String id){
        this.id = id;
        this.hand = Hand.RIGHT;
    }
}
