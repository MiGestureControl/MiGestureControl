package messages;

import deviceManagement.models.ActivSets;
import org.boon.Str;

/**
 * Created by hagen on 20/05/16.
 */
public class ConfigureSetForDeviceWithIDMessage {
    public String id;
    public ActivSets activSets;

    public ConfigureSetForDeviceWithIDMessage(String id, ActivSets activSets) {
        this.id = id;
        this.activSets = activSets;
    }
}
