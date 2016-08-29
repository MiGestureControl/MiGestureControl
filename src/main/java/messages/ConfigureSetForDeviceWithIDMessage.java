package messages;

import deviceManagement.models.ActivSets;
import org.boon.Str;

/**
 * Created by hagen on 20/05/16.
 */

/**
 * Diese Nachricht dient zur Konfigurations eines Sets des Gerätes
 */
public class ConfigureSetForDeviceWithIDMessage {
    //Die id des Gerätes
    public String id;
    //die zu speicherden Sets
    public ActivSets activSets;

    /**
     *
     * @param id die id das Geräts
     * @param activSets die Sets
     */
    public ConfigureSetForDeviceWithIDMessage(String id, ActivSets activSets) {
        this.id = id;
        this.activSets = activSets;
    }
}
