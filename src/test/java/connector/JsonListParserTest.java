package connector;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.Assert.*;

/**
 * Created by hagen on 02/02/16.
 */
public class JsonListParserTest {

    String toPase;
//    String path = "src/test/resources/jsonList/jsonList2.json";
    String path = "src/test/resources/jsonList/jsonList2.json";


    @Before
    public void setUp() throws Exception {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        this.toPase =  new String(encoded, StandardCharsets.UTF_8);
    }

//    @Test
//    public void testParseList() throws Exception {
//        //System.out.println(toPase);
//        boolean bool = JsonListParser.parseList(this.toPase);
//        assertTrue(bool);
//    }
}