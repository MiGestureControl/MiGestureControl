import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.*;

public class Main {

	public static void main(String[] args) {
		
		Main fhemConnector = new Main();
		fhemConnector.getJSON();
	}

	public void getJSON() {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://localhost:8083/fhem?cmd=jsonlist&XHR=1");
	
		try {
			HttpResponse response = client.execute(post);
			
			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			StringBuffer result = new StringBuffer();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			JSONObject resultSet = new JSONObject(result.toString());
			
			JSONArray results = resultSet.getJSONArray("Results");
			
			for(Object object : results){
				
				if(object instanceof JSONObject){
					JSONObject tempList = (JSONObject) object;
					
					if(tempList.get("list").equals("dummy")){
						System.out.println((tempList.get("list")));
						System.out.println(tempList);
						
						JSONArray tempDevices = tempList.getJSONArray("devices");
						
						for(Object tempDevice : tempDevices){
							if(tempDevice instanceof JSONObject){
								JSONObject device = (JSONObject) tempDevice;
								
								if(device.get("NAME").equals("testSchalter1")){
									System.out.println(device);
									
									String switchCommand;
									
									if(device.get("STATE").equals("off")){
										switchCommand = "http://localhost:8083/fhem?cmd=set%20" + device.get("NAME") + "%20" + "on" + "&XHR=1";
									}
									
									else{
										switchCommand = "http://localhost:8083/fhem?cmd=set%20" + device.get("NAME") + "%20" + "off" + "&XHR=1";
									}
									
									HttpPost request = new HttpPost(switchCommand);
									client.execute(request);
								}
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
