package senarios;

import akka.actor.UntypedActor;
import messages.DepthImageMessage;

/**
 * Created by hagen on 18/05/16.
 */
public class ImageComperatorActor extends UntypedActor {

    float[][] prevSignatur = new float[5][5];

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof DepthImageMessage) {
            float[][] sig = this.calculatDescriptionMatrix(((DepthImageMessage) message).depthFrame);
            this.calcDistance(sig,prevSignatur);
            prevSignatur = sig;
        }
    }

    private double calcDistance(float[][] sig1, float[][] sig2) {
        double distance = 0;
        for (int w = 0; w < sig1.length; w++) {
            for (int h = 0; h < sig1[0].length; h++) {
                distance += Math.sqrt((sig1[w][h] - sig2[w][h]) * (sig1[w][h] - sig2[w][h]) );
            }
        }
        System.out.println(distance);
        return distance;
    }


    private float[][] calculatDescriptionMatrix(short[] depthFrame) {
        int videoWidth = 0;
        int videoHeight = 0;

        float[][] sig = new float[5][5];


        if (depthFrame.length == 320 * 240 * 4) {
            videoWidth = 320;
            videoHeight = 240;
        } else if (depthFrame.length == 640 * 480 * 4) {
            videoWidth = 640;
            videoHeight = 480;
        }

        short[][] depthImage = new short[videoWidth][videoHeight];

        for (int w = 0; w < videoWidth; w++){
            for (int h = 0; h < videoWidth; h++){
                depthImage[w][h] = depthFrame[w*videoWidth * w];
            }
        }

        //Die einzellenen Zellen
        float[] prop
                = new float[]{
                    0,
                    2f  / 10f,
                    4f  / 10f,
                    6f  / 10f,
                    8f  / 10f,
                    1
            };

        for (int x = 0; x < 5; x++)
            for (int y = 0; y < 5; y++) {
                int sum = 0;
                int numPixels = 0;

                int chopWidth = videoWidth / 5;
                int chopHeight = videoHeight / 5;

                //Duchschnittbilden
                for (int w = chopWidth*x; w < videoWidth || w < chopWidth*x+1; w++){
                    for (int h = chopHeight*x; h < chopHeight || h < chopHeight*x+1; h++){
                        sum += depthImage[x][y];
                        numPixels++;
                    }
                }

                sig[x][y] = sum / numPixels;
            }

        return sig;
    }

}
