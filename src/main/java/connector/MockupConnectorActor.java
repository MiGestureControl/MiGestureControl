package connector;

import akka.actor.UntypedActor;
import connector.models.FhemJsonList;
import messages.GetDevicesMessage;
import messages.SetDeviceStateMessage;

/**
 * MockupConnector replaces FhemConnector for testing purposes
 * without any need to have FHEM installed
 *
 * Created by Marc on 22.06.2016.
 */
public class MockupConnectorActor extends UntypedActor {

    // string representing a single FHEM device (myButton1) for testing purposes
    private String initialJson = "{ \"Arg\": \"\", \"Results\": [ { \"Name\": \"myButton1\", \"PossibleSets\": \" \", " +
            "\"PossibleAttrs\": \"verbose:0,1,2,3,4,5 room group comment:textField-long alias eventMap userReadings " +
            "readingList setList event-on-change-reading event-on-update-reading event-aggregator event-min-interval " +
            "stateFormat cmdIcon devStateIcon devStateStyle icon sortby webCmd widgetOverride userattr\", " +
            "\"Internals\": { \"NAME\": \"myButton1\", \"NR\": \"20\", \"STATE\": \"off\", \"TYPE\": \"dummy\" }, " +
            "\"Readings\": { \"state\": { \"Value\": \"off\", \"Time\": \"2016-05-20 18:26:19\" } }, " +
            "\"Attributes\": { \"room\": \"testRoom\", \"webCmd\": \"on:off\" } } ], \"totalResultsReturned\": 1 }";

    // mockup device list created from JSON above
    private FhemJsonList mockupDeviceList;

    public MockupConnectorActor() {
        mockupDeviceList = FhemJsonList.parseList(initialJson);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof GetDevicesMessage) {
            getSender().tell(mockupDeviceList, getSelf());
        } else if (message instanceof SetDeviceStateMessage){
            SetDeviceStateMessage deviceStateMessage = (SetDeviceStateMessage) message;
            String id    = deviceStateMessage.deviceID;
            String state = deviceStateMessage.state;
            //String arg   = deviceStateMessage.arg;
            mockupDeviceList.updateDevice(id, state);
        }
        unhandled(message);
    }
}
