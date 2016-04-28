package connector.models;

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
}
