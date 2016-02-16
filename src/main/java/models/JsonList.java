package models;

import java.util.List;

/**
 * Created by hagen on 06/02/16.
 */
public class JsonList {
    private String Arg;
    private List<Device> Results;
    private int totalResultsReturned;

    @Override
    public String toString() {
        return "JsonList{" +
                "Arg='" + Arg + '\'' +
                ", Results=" + Results +
                ", totalResultsReturned=" + totalResultsReturned +
                '}';
    }
}
