package me.sadensmol.flights.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import me.sadensmol.flights.services.ticket.BaggageService;
import me.sadensmol.flights.services.ticket.TicketService;

// simple implementation of REST
// todo rework it to different verticles - verticle per service
// this will improve total bandwidth/load
public class RestVerticle extends AbstractVerticle {
    //rework with Vert.x service
    private TicketService ticketService = new TicketService();
    private BaggageService baggageService = new BaggageService();

    private static final Logger logger = LoggerFactory.getLogger(RestVerticle.class);

    public static final String CONFIG_HTTP_SERVER_PORT = "http.server.port";
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.get("/ticket:id").handler(this::ticketHandler);
        router.get("/baggage:id/dest:destId").handler(this::baggageHandler);
        int portNumber = config().getInteger(CONFIG_HTTP_SERVER_PORT, 8080);
        server
                .requestHandler(router)
                .listen(portNumber, ar -> {
                    if (ar.succeeded()) {
                        logger.info("REST started on port " + portNumber);
                        startFuture.complete();
                    } else {
                        logger.error("Could not start a REST server", ar.cause());
                        startFuture.fail(ar.cause());
                    }
                });
    }


    private void ticketHandler(RoutingContext context) {
        int ticketId = 0;
        boolean result = false;

        String id = context.request().getParam("id");

        try {
            ticketId = Integer.parseInt(id);
        }catch (NumberFormatException exception) {
            logger.error("Wrong ticket id format");
        }

        HttpServerResponse response = context.response().putHeader("content-type", "text/plain");
        result = ticketService.book(ticketId);

        response.end(result? "1":"0");
    }

    private void baggageHandler(RoutingContext context) {

        int baggageId = 0;
        int destId = 0;

        boolean result = false;

        try {
            baggageId = Integer.parseInt(context.request().getParam("id"));
        }catch (NumberFormatException exception) {
            logger.error("Wrong baggage id format");
        }

        try {
            destId = Integer.parseInt(context.request().getParam("destId"));
        }catch (NumberFormatException exception) {
            logger.error("Wrong dest id format");
        }

        HttpServerResponse response = context.response().putHeader("content-type", "text/plain");
        result = baggageService.book(baggageId, destId);

        response.end(result?"1":"0");
    }

}
