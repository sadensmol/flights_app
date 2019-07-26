package me.sadensmol.flights;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import me.sadensmol.flights.web.RestVerticle;

public class Launcher extends AbstractVerticle {

    private static final Logger log = LoggerFactory.getLogger(Launcher.class);
    @Override
    public void start(Future<Void> startFuture) throws Exception {

        log.debug("Starting flight application...");

        Future<String> webDeployment = Future.future();

        vertx.deployVerticle(new RestVerticle(), new DeploymentOptions().setInstances(1), webDeployment.completer());
        webDeployment.setHandler(ar -> {
            if (ar.succeeded()) {
                log.debug("Verticle started with no problems!");
                startFuture.complete();
            } else {
                log.error("Verticle failed to start");
                startFuture.fail(ar.cause());

            }
        });
    }
}
