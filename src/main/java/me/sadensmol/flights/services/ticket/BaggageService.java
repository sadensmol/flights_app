package me.sadensmol.flights.services.ticket;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import me.sadensmol.flights.model.AppModel;
import me.sadensmol.flights.model.Baggage;
import me.sadensmol.flights.model.BaggageToDest;
import me.sadensmol.flights.model.Ticket;

import java.util.Optional;

public class BaggageService {
    private AppModel appModel = AppModel.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(BaggageService.class);

    public boolean book(int baggageId, int destId) {
        Optional<BaggageToDest> baggageToDest = appModel.findBaggageByIdAndDest(baggageId, destId);
        if (baggageToDest.isPresent()) {
            return true;
        }

        return false;
    }
}
