package deviceManagement.models;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by hagen on 19/05/16.
 */
public class PossibleSet {
    String name;
    ArrayList<String> args;

    public PossibleSet(String name, Collection<String> args) {
        this.name = name;
        this.args = new ArrayList<>(args);
    }

    @Override
    public String toString() {
        return "PossibleSet{" +
                "name='" + name + '\'' +
                ", args=" + args +
                '}';
    }
}
