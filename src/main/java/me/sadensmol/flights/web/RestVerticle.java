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
import me.sadensmol.flights.services.ticket.CouponsService;
import me.sadensmol.flights.services.ticket.TicketsService;

// simple implementation of REST
// todo rework it to different verticles - verticle per service
// this will improve total bandwidth/load
// add event bus for interaction between the services
public class RestVerticle extends AbstractVerticle {
    //rework with Vert.x service
    private TicketsService ticketService = new TicketsService();
    private BaggageService baggageService = new BaggageService();
    private CouponsService couponsServiceService = new CouponsService();

    private static final Logger logger = LoggerFactory.getLogger(RestVerticle.class);

    public static final String CONFIG_HTTP_SERVER_PORT = "http.server.port";
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        router.get("/ticket:id").handler(this::ticketHandler);
        router.get("/baggage:id/dest:destId").handler(this::baggageHandler);
        router.get("/coupon:id/price:price").handler(this::couponHandler);
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

    private void couponHandler(RoutingContext context) {

        int couponId = 0;
        double price = 0;

        boolean result = false;

        try {
            couponId = Integer.parseInt(context.request().getParam("id"));
        }catch (NumberFormatException exception) {
            logger.error("Wrong coupon id format ");
        }

        try {
            price = Double.parseDouble(context.request().getParam("price"));
        }catch (NumberFormatException exception) {
            logger.error("Wrong price id format");
        }


        HttpServerResponse response = context.response().putHeader("content-type", "text/plain");
        result = couponsServiceService.book(couponId, price);

        response.end(result ? "1":"0 the price is : " + price);
    }

}
