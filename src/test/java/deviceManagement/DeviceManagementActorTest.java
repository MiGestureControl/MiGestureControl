package deviceManagement;

import connector.FhemConectorActor;
import connector.models.FhemJsonList;
import deviceManagement.models.ActivSets;
import messages.*;
import messages.HelperEnums.DeviceState;
import messages.HelperEnums.Hand;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
import org.junit.*;

import akka.testkit.TestActor;
import akka.testkit.TestProbe;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import scala.concurrent.duration.Duration;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by hagen on 28.04.2016.
 */
public class DeviceManagementActorTest {

    static ActorSystem system;
    static ActorRef subject;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }


    @Test
    public void testOnReceiveFhemJsonList() throws Exception {

        byte[] encoded = Files.readAllBytes(Paths.get("src/test/resources/jsonList/jsonList2.json"));
        String s =  new String(encoded, StandardCharsets.UTF_8);


        ObjectMapper mapper = JsonFactory.create();
        FhemJsonList fhemJsonList = mapper.readValue(s, FhemJsonList.class);

        new JavaTestKit(system) {{
            final Props props = Props.create(DeviceManagementActor.class);
            final ActorRef subject = system.actorOf(props);

            // can also use JavaTestKit “from the outside”
            final JavaTestKit probe = new JavaTestKit(system);

            // the run() method needs to finish within 3 seconds
            new Within(duration("1 seconds")) {
                protected void run() {

                    subject.tell(fhemJsonList, getRef());


                    // response must have been enqueued to us before probe
                                       // check that the probe we injected earlier got the msg
                    //DevicesMessage devicesMessage = probe.expectMsgClass(duration("1 second"), DevicesMessage.class);

                    final Object[] one = receiveN(1);

                    Assert.assertEquals(one[0].getClass(), DevicesMessage.class);

                }
            };
        }};
    }

    @Test
    public void testOnReceiveSetDeviceLocationMessaget() throws Exception {
        new JavaTestKit(system) {{
            final Props props = Props.create(DeviceManagementActor.class);
            final ActorRef subject = system.actorOf(props);

            // can also use JavaTestKit “from the outside”
            final JavaTestKit probe = new JavaTestKit(system);

            // the run() method needs to finish within 3 seconds
            new Within(duration("1 seconds")) {
                protected void run() {

                    subject.tell(new SetDeviceLocationMessage("TV", new double[3], Hand.LEFT), getRef());

                    expectNoMsg();

                }
            };
        }};
    }

    @Test
    public void testOnReceiveSetAllDevicesMessage() throws Exception {

        byte[] encoded = Files.readAllBytes(Paths.get("src/test/resources/jsonList/jsonList2.json"));
        String s =  new String(encoded, StandardCharsets.UTF_8);


        ObjectMapper mapper = JsonFactory.create();
        FhemJsonList fhemJsonList = mapper.readValue(s, FhemJsonList.class);

        new JavaTestKit(system) {{
            final Props props = Props.create(DeviceManagementActor.class);
            final ActorRef subject = system.actorOf(props);

            final JavaTestKit probe = new JavaTestKit(system);

            new Within(duration("1 seconds")) {
                protected void run() {

                    subject.tell(fhemJsonList, getRef());

                    subject.tell(new SetAllDevicesMessage(DeviceState.ON), getRef());

                    final Object[] objects = receiveN(2);

                    Assert.assertEquals(objects[0].getClass(), DevicesMessage.class);
                    Assert.assertEquals(objects[1].getClass(), SetDeviceStateMessage.class);

                }
            };
        }};
    }

    @Test
    public void testOnReceiveConfigureSetForDeviceWithIDMessage() throws Exception {

        byte[] encoded = Files.readAllBytes(Paths.get("src/test/resources/jsonList/jsonList2.json"));
        String s =  new String(encoded, StandardCharsets.UTF_8);


        ObjectMapper mapper = JsonFactory.create();
        FhemJsonList fhemJsonList = mapper.readValue(s, FhemJsonList.class);

        new JavaTestKit(system) {{
            final Props props = Props.create(DeviceManagementActor.class);
            final ActorRef subject = system.actorOf(props);

            final JavaTestKit probe = new JavaTestKit(system);

            new Within(duration("1 seconds")) {
                protected void run() {

                    subject.tell(new ConfigureSetForDeviceWithIDMessage("TV", new ActivSets()), getRef());

                    final Object[] objects = receiveN(1);

                    Assert.assertEquals(objects[0].getClass(), ConfigureDeviceFinishedMessage.class);

                }
            };
        }};
    }
}