package connector;

import akka.actor.UntypedActor;
import connector.models.FhemJsonList;
import messages.GetDevicesMessage;
import messages.SetDeviceStateMessage;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by hagen on 02/02/16.
 */
public class FhemConectorActor extends UntypedActor {

    String baseURL = "http://localhost:8083";
    URL fhemJSONListURL;
    URL fhemSendURL;


    public FhemConectorActor() {
        try {
            this.fhemJSONListURL = new URL(baseURL + "/fhem?cmd=jsonlist2&XHR=1");
            this.fhemSendURL = new URL(baseURL + "/fhem");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     * @param message
     */
    public void onReceive(Object message) {
        if (message instanceof GetDevicesMessage) {
            //System.out.println(((GetDevices) message).message);
            String content = this.getContentStringFromURL(this.fhemJSONListURL);

            if(content!=null){
                getSender().tell(parseList(content), getSelf());
            }
        } else if (message instanceof SetDeviceStateMessage){
            SetDeviceStateMessage deviceStateMessage = (SetDeviceStateMessage) message;
            String id = deviceStateMessage.deviceID;
            String state = deviceStateMessage.state;
            sendCommand("?cmd=set%20" + id + "%20" + state + "&XHR=1");
        }
        unhandled(message);
    }


    /**
     * Sendet das den übergebenen String als Comando an den FHEM-Server
     *
     * @param command
     * @return Wenn dabei kein Fehler aufgetreten ist true
     */
    private boolean sendCommand(String command) {

        System.out.println(command);

        try {
            HttpURLConnection con = (HttpURLConnection) this.fhemSendURL.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(command);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            //System.out.println("\nSending 'POST' request to URL : " + this.fhemJSONListURL);
            //System.out.println("Post parameters : " + command);
            //System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            //System.out.println(response.toString());
            return true;

        } catch (MalformedURLException e) {
            return false;
        } catch (ProtocolException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Liefert den Inhalt eines einer URL
     *
     * @param url die URL
     * @return Den Inhalt der sich hinter der URL befindet als String
     */
    private String getContentStringFromURL(URL url) {

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();


            // optional default is GET
            con.setRequestMethod("GET");

            //optional request header
            //con.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = con.getResponseCode();
            //System.out.println("\nSending 'GET' request to URL : " + url);
            //System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
//            System.out.println(response.toString());
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Methode die aus einem String eine geparste Jsonlist erzeugt
     * in der alle Geräte und so weiter auftrauchen
     * @param s der zu parsende String
     * @return die erzeugte FhemJsonList
     */
    private FhemJsonList parseList(String s)  {

        ObjectMapper mapper = JsonFactory.create();

        FhemJsonList device = mapper.readValue(s, FhemJsonList.class);

        return device;
    }

}
