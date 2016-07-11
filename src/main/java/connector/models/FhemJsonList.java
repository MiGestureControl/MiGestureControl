package connector.models;

import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import java.util.List;

/**
 * Created by hagen on 06/02/16.
 */
public class FhemJsonList {

    private String Arg;
    private List<FhemDevice> Results;
    private int totalResultsReturned;

    public String getArg() {
        return Arg;
    }

    public List<FhemDevice> getDevices() {
        return Results;
    }

    public int getTotalResultsReturned() {
        return totalResultsReturned;
    }


    @Override
    public String toString() {
        return "FhemJsonList{" +
                "Arg='" + Arg + '\'' +
                ", Results=" + Results +
                ", totalResultsReturned=" + totalResultsReturned +
                '}';
    }

    public static FhemJsonList parseList(String s) {
        ObjectMapper mapper = JsonFactory.create();

        FhemJsonList device = mapper.readValue(s, FhemJsonList.class);

        return device;
    }

    public void updateDevice(String id, String state) {
        for(FhemDevice device : Results) {
            if(device.getName().equals(id)) {
                device.setState(state);
            }
        }
    }
}
