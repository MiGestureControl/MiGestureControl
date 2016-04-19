package httpServer;

import akka.http.javadsl.model.*;
import akka.http.javadsl.server.HttpApp;
import akka.http.javadsl.server.Route;


/**
 * Created by hagen on 10/04/16.
 */
public class HTTPServer extends HttpApp {


    @Override
    public Route createRoute() {
        return route(

                pathSingleSlash().route(
                        getFromResource("web/index.html")
                ),
                pathPrefix("node_modules").route(
                        getFromResourceDirectory("web/node_modules/")
                ),
                pathPrefix("app").route(
                        getFromResourceDirectory("web/app/")
                ),
                pathPrefix("api").route(
                        pathPrefix("v1").route(
                                pathPrefix("devices").route(
                                        get(
                                                pathEndOrSingleSlash().route(
                                                        complete(MediaTypes.APPLICATION_JSON.toContentType(), "Test")
                                                )
                                        )
                                )
                        )

                )
        );
    }

}
