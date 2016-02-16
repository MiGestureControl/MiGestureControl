package connector;

import akka.actor.UntypedActor;
import messages.GetDevices;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by hagen on 02/02/16.
 */
public class FhemConector extends UntypedActor {

    URL fhemURL;

    public FhemConector() {
        try {
            this.fhemURL = new URL("http://localhost:8083/fhem?cmd=jsonlist2&XHR=1");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }


    public void onReceive(Object message) {
        if (message instanceof GetDevices) {
            /*//System.out.println(((GetDevices) message).message);
            String content = this.getContentStringFromURL(this.fhemURL);

            if(content!=null){
                getSender().tell(JsonListParser.parseList(content), getSelf());
            }*/
        }

        unhandled(message);
    }


    private boolean sendCommand(String command) {


        String url = "http://localhost:8083/fhem";
        URL obj = null;
        try {
            obj = new URL(url);

            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(command);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + command);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
            return true;

        } catch (MalformedURLException e) {
            return false;
        } catch (ProtocolException e) {
            return false;
        } catch (IOException e) {
            return false;
        }


    }

    private String getContentStringFromURL(URL url) {

        //String url = "http://www.google.com/search?q=mkyong";

        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection) url.openConnection();


            // optional default is GET
            con.setRequestMethod("GET");

            //optional request header
            //con.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            System.out.println(response.toString());
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

//    private void parseJson(String content) {
//
//        JSONObject resultSet = new JSONObject(content);
//
//        JSONArray results = resultSet.getJSONArray("Results");
//
//        for (Object object : results) {
//
//            if (object instanceof JSONObject) {
//                JSONObject tempList = (JSONObject) object;
//
//                if (tempList.get("list").equals("FS20")) {
//                    System.out.println((tempList.get("list")));
//                    System.out.println(tempList);
//
//                    JSONArray tempDevices = tempList.getJSONArray("devices");
//
//                    for (Object tempDevice : tempDevices) {
//                        if (tempDevice instanceof JSONObject) {
//                            JSONObject device = (JSONObject) tempDevice;
//
//                            if (device.get("NAME").equals("ReadingLight")) {
//                                System.out.println(device);
//
//                                if (device.get("STATE").equals("off")) {
//                                    this.
//                                            sendCommand("?cmd=set%20" + device.get("NAME") + "%20" + "on" + "&XHR=1");
//                                } else {
//                                    this.sendCommand("?cmd=set%20" + device.get("NAME") + "%20" + "off" + "&XHR=1");
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }

}
