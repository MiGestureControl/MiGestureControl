package httpServer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.MediaTypes;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.*;
import akka.http.javadsl.server.values.PathMatcher;
import akka.http.javadsl.server.values.PathMatchers;
import akka.pattern.Patterns;
import akka.util.Timeout;
import deviceManagement.models.ActivSets;
import messages.DevicesMessage;
import messages.*;
import messages.HelperEnums.Hand;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.concurrent.TimeoutException;

import static akka.http.javadsl.marshallers.jackson.Jackson.jsonAs;

/**
 * Created by hagen on 10/04/16.
 */
public class HTTPServer extends HttpApp {

    //HTTPServer hat einen verweiß auf den dispatchActor um diesen Nachrichten senden zu können
    private final ActorRef dispatchActor;
    //30 sec beträgt der Timeout für das Konfigurieren von Geräten
    private final Timeout timeout = new Timeout(Duration.create(30, "seconds"));

    /**
     *
     * @param dispatchActor ein verweis auf den dispatchActor
     */
    public HTTPServer(ActorRef dispatchActor) {
        this.dispatchActor = dispatchActor;
    }

    /**
     * überschreiben der createRoute Methode ist alles was nötig ist um den Server zu implementieren
     * @return Route die die Konfiguration des Servers beinhaltet
     */
    @Override
    public Route createRoute() {


        final PathMatcher<String> deviceId = PathMatchers.segment();
        final ObjectMapper mapper =  JsonFactory.create();

        //neuer Handler zur Ausgabe der Geräteliste
        Handler getAllDevicesHandler = new Handler() {
            @Override
            public RouteResult apply(RequestContext ctx) {
                try {
                    //Eine Future mit dem Ergebnis der Anfrage
                    Future<Object> future
                            = Patterns.ask(dispatchActor, new GetAllDevicesMessage(), timeout);

                    //Warten bis die Anfrage beantwortet ist oder die zeit abgelaufen ist
                    Object result =  Await.result(future, timeout.duration());

                    System.out.println(result);

                    //ist die Nachricht vom richtigen Type wird geantwortet
                    if (result instanceof DevicesMessage){
                        DevicesMessage devicesMessage = (DevicesMessage) result;

                        //erzeugen der Antwort
                        return ctx.complete(
                                MediaTypes.APPLICATION_JSON.toContentType(),
                                mapper.toJson(devicesMessage.devices.values())
                        );
                    }
                    //falls eine Fehler auftrat
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);

                } catch (Exception e) {
//                    e.printStackTrace();
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);
                }
            }
        };

        //neuer Handler zum konfigurieren der rechten Hand
        Handler configureRigthHandLocationForDeviceWithIDHandler = new Handler() {
            @Override
            public RouteResult apply(RequestContext ctx) {
                try {
                    final String id = deviceId.get(ctx);
                    System.out.println(id);

                    //Eine Future mit dem Ergebnis der Anfrage
                    Future<Object> future
                            = Patterns.ask(dispatchActor, new ConfigureDeviceWithIDMessage(id, Hand.RIGHT), timeout);

                    Object result = new Object();

                    try {
                        //Warten bis die Anfrage beantwortet ist oder die zeit abgelaufen ist
                        result =  Await.result(future, timeout.duration());
                    } catch (TimeoutException exception){
                        //wenn es zu lange dauerte wird diese dem Dispatcher mitgeteils, Die Konfiguration wird damit abgebrochen
                        dispatchActor.tell(new ConfigureDeviceWithIdFailedMessage(id), ActorRef.noSender());
                    }

                    System.out.println(result);
                    if (result instanceof ConfigureDeviceFinishedMessage){

                        //Dem Client wird das erfolgreiche Konfigurieren mitgeteilt
                        return ctx.complete(MediaTypes.APPLICATION_JSON.toContentType(), "{\"status\": \"ok\"} ");

                    }
                    //oder eine Fehlermedlung gesendet
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);

                } catch (Exception e) {
//                    e.printStackTrace();
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);
                }
            }
        };

        //neuer Handler zum konfigurieren der linken Hand
        Handler configureLeftHandLocationForDeviceWithIDHandler = new Handler() {
            @Override
            public RouteResult apply(RequestContext ctx) {
                try {
                    final String id = deviceId.get(ctx);
                    System.out.println(id);

                    //Eine Future mit dem Ergebnis der Anfrage
                    Future<Object> future
                            = Patterns.ask(dispatchActor, new ConfigureDeviceWithIDMessage(id, Hand.LEFT), timeout);

                    Object result = new Object();

                    try{
                        //Warten bis die Anfrage beantwortet ist oder die zeit abgelaufen ist
                        result =  Await.result(future, timeout.duration());
                        System.out.println(result);
                    }catch (TimeoutException exception){
                        dispatchActor.tell(new ConfigureDeviceWithIdFailedMessage(id), ActorRef.noSender());
                    }

                    if (result instanceof ConfigureDeviceFinishedMessage){
                        //Erfolgreiche Konfiguration wird dem Client mitgeteil
                        return ctx.complete(MediaTypes.APPLICATION_JSON.toContentType(), "{\"status\": \"ok\"} ");
                    }

                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);

                } catch (Exception e) {
//                    e.printStackTrace();
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);
                }
            }
        };

        //neuer Handler zum entfernen einer Gerätekonfiguration
        Handler removeLocationForDeviceWithIDHandler = new Handler() {
            @Override
            public RouteResult apply(RequestContext ctx) {
                try {
                    final String id = deviceId.get(ctx);

                    //Eine Future mit dem Ergebnis der Anfrage
                    Future<Object> future
                            = Patterns.ask(dispatchActor, new RemoveLocationForDeviceWithIDMessage(id), timeout);

                    //Warten bis die Anfrage beantwortet ist oder die zeit abgelaufen ist
                    Object result =  Await.result(future, timeout.duration());

                    System.out.println(result);
                    if (result instanceof ConfigureDeviceFinishedMessage){

                        return ctx.complete(MediaTypes.APPLICATION_JSON.toContentType(), "{\"status\": \"removed\"} ");

                    }
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);

                } catch (Exception e) {
//                    e.printStackTrace();
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);
                }
            }
        };

        //neuer Handler zum konfigurieren der Sets
        Handler configureSetForDeviceWithIDHandler = new Handler() {
            @Override
            public RouteResult apply(RequestContext ctx) {
                try {
                    final String id = deviceId.get(ctx);

                    HttpRequest request = ctx. request();

                    String requestEntityString = request.entity().toString();

                    int begin = requestEntityString.indexOf("{");
                    int end   = requestEntityString.lastIndexOf("}");


                    //parsen der Nachricht des Clients
                    ObjectMapper mapper =  JsonFactory.create();
                    ActivSets activSets = mapper.fromJson(requestEntityString.substring(begin, end), ActivSets.class);

                    System.out.println(activSets);

                    //Eine Future mit dem Ergebnis der Anfrage
                    Future<Object> future
                            = Patterns.ask(dispatchActor, new ConfigureSetForDeviceWithIDMessage(id, activSets), timeout);

                    //Warten bis die Anfrage beantwortet ist oder die zeit abgelaufen ist
                    Object result =  Await.result(future, timeout.duration());

                    System.out.println(result);
                    if (result instanceof ConfigureDeviceFinishedMessage){

                        return ctx.complete(MediaTypes.APPLICATION_JSON.toContentType(), "{\"status\": \"ok\"} ");

                    }
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);

                } catch (Exception e) {
//                    e.printStackTrace();
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);
                }
            }
        };

        //Erzeugen der Route
        return route(

                pathSingleSlash().route(
                        //ausliefern von statischen Inhalten
                        getFromResource("web/index.html")
                ),
                pathPrefix("node_modules").route(
                        getFromResourceDirectory("web/node_modules/")
                ),
                pathPrefix("assets").route(
                        getFromResourceDirectory("web/assets/")
                ),
                pathPrefix("dist").route(
                        getFromResourceDirectory("web/dist/")
                ),
                pathPrefix("app").route(
                        getFromResourceDirectory("web/app/")
                ),
                //Die api an sich
                pathPrefix("api").route(
                        //v1 falls es eine neue version gibt
                        pathPrefix("v1").route(
                                //liefert die Geräteliste
                                path("devices").route(
                                        //der getAllDevicesHandler kümmert sich um diese Anfrage
                                        get(
                                                handleWith(
                                                        getAllDevicesHandler
                                                )
                                        )
                                ),
                                path("devices", deviceId).route(
                                        //der configureRigthHandLocationForDeviceWithIDHandler kümmert sich um diese Anfrage
                                        get(
                                                handleWith(
                                                        configureRigthHandLocationForDeviceWithIDHandler, deviceId
                                                )
                                        ),
                                        //der removeLocationForDeviceWithIDHandler kümmert sich um diese Anfrage
                                        delete(
                                                handleWith(
                                                        removeLocationForDeviceWithIDHandler, deviceId
                                                )
                                        ),
                                        //der configureSetForDeviceWithIDHandler kümmert sich um diese Anfrage
                                        post(
                                                handleWith(
                                                    configureSetForDeviceWithIDHandler, deviceId
                                                )
                                        )
                                ),
                                path("devicesleft", deviceId).route(
                                        //der configureLeftHandLocationForDeviceWithIDHandler kümmert sich um diese Anfrage
                                        get(
                                                handleWith(
                                                        configureLeftHandLocationForDeviceWithIDHandler, deviceId
                                                )
                                        )
                                )
                        )

                )
        );
    }

}
