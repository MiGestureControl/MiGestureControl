package httpServer;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.javadsl.model.MediaTypes;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.*;
import akka.http.javadsl.server.values.PathMatcher;
import akka.http.javadsl.server.values.PathMatchers;
import akka.pattern.Patterns;
import akka.util.Timeout;
import deviceManagement.models.DevicesMessage;
import messages.ConfigureDeviceFinishedMessage;
import messages.ConfigureDeviceWithIDMessage;
import messages.GetAllDevicesMessage;
import messages.RemoveLocationForDeviceWithIDMessage;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;


/**
 * Created by hagen on 10/04/16.
 */
public class HTTPServer extends HttpApp {

    private final ActorRef dispatchActor;
    private final ActorSystem system;
    private final Timeout timeout = new Timeout(Duration.create(30, "seconds"));


    public HTTPServer(ActorRef dispatchActor, ActorSystem system) {
        this.dispatchActor = dispatchActor;
        this.system = system;
    }

    @Override
    public Route createRoute() {

        final PathMatcher<String> deviceId = PathMatchers.segment();
        final ObjectMapper mapper =  JsonFactory.create();

        Handler getAllDevicesHandler = new Handler() {

            @Override
            public RouteResult apply(RequestContext ctx) {
                try {
                    Future<Object> future
                            = Patterns.ask(dispatchActor, new GetAllDevicesMessage(), timeout);

                    Object result =  Await.result(future, timeout.duration());

                    System.out.println(result);

                    if (result instanceof DevicesMessage){
                        DevicesMessage devicesMessage = (DevicesMessage) result;

                        return ctx.complete(
                                MediaTypes.APPLICATION_JSON.toContentType(),
                                mapper.toJson(devicesMessage.devices.values())
                        );
                    }
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);

                } catch (Exception e) {
                    e.printStackTrace();
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);
                }
            }
        };

        Handler configureLocationForDeviceWithIDHandler = new Handler() {
            @Override
            public RouteResult apply(RequestContext ctx) {
                try {
                    final String id = deviceId.get(ctx);
                    Future<Object> future
                            = Patterns.ask(dispatchActor, new ConfigureDeviceWithIDMessage(id), timeout);

                    Object result =  Await.result(future, timeout.duration());

                    System.out.println(result);
                    if (result instanceof ConfigureDeviceFinishedMessage){

                        return ctx.complete(MediaTypes.APPLICATION_JSON.toContentType(), "{\"status\": \"ok\"} ");

                    }
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);

                } catch (Exception e) {
                    e.printStackTrace();
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);
                }
            }
        };

        Handler removeLocationForDeviceWithIDHandler = new Handler() {
            @Override
            public RouteResult apply(RequestContext ctx) {
                try {
                    final String id = deviceId.get(ctx);
                    Future<Object> future
                            = Patterns.ask(dispatchActor, new RemoveLocationForDeviceWithIDMessage(id), timeout);

                    Object result =  Await.result(future, timeout.duration());

                    System.out.println(result);
                    if (result instanceof ConfigureDeviceFinishedMessage){

                        return ctx.complete(MediaTypes.APPLICATION_JSON.toContentType(), "{\"status\": \"removed\"} ");

                    }
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);

                } catch (Exception e) {
                    e.printStackTrace();
                    return ctx.completeWithStatus(StatusCodes.INTERNAL_SERVER_ERROR);
                }
            }
        };

        return route(

                pathSingleSlash().route(
                        getFromResource("web/index.html")
                ),
                pathPrefix("node_modules").route(
                        getFromResourceDirectory("web/node_modules/")
                ),
                pathPrefix("assets").route(
                        getFromResourceDirectory("web/assets/")
                ),
                pathPrefix("build").route(
                        getFromResourceDirectory("web/build/")
                ),
                pathPrefix("app").route(
                        getFromResourceDirectory("web/app/")
                ),
                pathPrefix("api").route(
                        pathPrefix("v1").route(
                                path("devices").route(
                                        get(handleWith(getAllDevicesHandler))
                                ),
                                path("devices", deviceId).route(
                                        get(handleWith(configureLocationForDeviceWithIDHandler, deviceId)),
                                        delete(handleWith(removeLocationForDeviceWithIDHandler, deviceId))
                                )
                        )

                )
        );
    }

}
