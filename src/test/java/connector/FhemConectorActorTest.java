package connector;

import akka.testkit.TestActor;
import akka.testkit.TestProbe;
import messages.GetFhemDevicesMessage;
import org.junit.*;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import scala.concurrent.duration.Duration;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

/**
 * Created by hagen on 18/03/16.
 */
public class FhemConectorActorTest {

    static ActorSystem system;
    static ActorRef subject;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();

        final Props props = Props.create(FhemConectorActor.class);
        subject = system.actorOf(props);
    }

    @AfterClass
    public static void teardown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }

    @Before
    public void Before() {

        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 8083), 200);
            socket.close();
        } catch (Exception ex) {
            System.out.println("port zu Test wird nicht ausgeführt: Staten sie FHEM um das Problem zu beheben");

            Assume.assumeTrue(false);
        }

    }

    @Test
    public void testDoNoAnswerStringMessage () {

        TestProbe testProbe = TestProbe.apply(system);

        subject.tell("Hello", testProbe.ref());

        testProbe.expectNoMsg(Duration.apply(1000, TimeUnit.MILLISECONDS));

    }

    @Test
    public void testGetDeviceMessage () {

        TestProbe testProbe = TestProbe.apply(system);

        GetFhemDevicesMessage message = new GetFhemDevicesMessage();

        subject.tell(message, testProbe.ref());

        TestActor.Message answerMessage =  testProbe.lastMessage();

        System.out.println(answerMessage.getClass());

        //assertTrue(answerMessage.msg()!=null);

    }
}
