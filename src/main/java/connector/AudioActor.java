package connector;

import akka.actor.UntypedActor;
import connector.models.FhemJsonList;
import messages.FlashMessage;
import messages.GetDevicesMessage;
import messages.SetDeviceStateMessage;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import javax.sound.sampled.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by hagen on 02/02/16.
 */
public class AudioActor extends UntypedActor {

    Clip flashClip;
    URL flashUrl;
    AudioInputStream flashAudioIn;

    public AudioActor() {
        try {
            File file = new File("flash.wav");
            flashUrl = file.toURI().toURL();

            flashAudioIn = AudioSystem.getAudioInputStream(flashUrl);
            flashClip = AudioSystem.getClip();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     * @param message
     */
    public void onReceive(Object message) {
        if (message instanceof FlashMessage) {
                if(!flashClip.isRunning() ){
                    try {
                        flashAudioIn = AudioSystem.getAudioInputStream(flashUrl);
                        flashClip = AudioSystem.getClip();

                        flashClip.open(flashAudioIn);
                        flashClip.start();

                        System.out.println("flash");
                    } catch (LineUnavailableException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (UnsupportedAudioFileException e) {
                        e.printStackTrace();
                    }
                }
        }
        unhandled(message);
    }




}
