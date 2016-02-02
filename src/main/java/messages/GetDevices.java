package messages;

import java.io.Serializable;

/**
 * Created by hagen on 02/02/16.
 */
public class GetDevices implements Serializable {
    public final String message;

    public GetDevices(String message) {
        this.message = message;
    }
}
