package connector;

import models.JsonList;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

/**
 * Created by hagen on 02/02/16.
 */
public class JsonListParser {

    public static boolean parseList(String s)  {

        ObjectMapper mapper = JsonFactory.create();

        JsonList device = mapper.readValue(s, JsonList.class);

        System.out.print(device);

        return true;
    }

}





