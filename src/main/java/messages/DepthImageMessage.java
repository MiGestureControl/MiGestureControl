package messages;

import java.io.Serializable;

/**
 * Created by hagen on 18/05/16.
 */
public class DepthImageMessage implements Serializable {
    public final short[] depthFrame;

    public DepthImageMessage(short[] depthFrame) {
        this.depthFrame = depthFrame;
    }
}
