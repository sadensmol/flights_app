package me.sadensmol.flights.services.ticket;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import me.sadensmol.flights.model.AppModel;
import me.sadensmol.flights.model.Ticket;
import me.sadensmol.flights.web.RestVerticle;

import java.util.Optional;

public class TicketService {
    private AppModel appModel = AppModel.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(TicketService.class);

    public boolean book(int ticketId) {

        Optional<Ticket> ticket = appModel.findTicketById(ticketId);

        if (ticket.isPresent()) {
            return true;
        }

        return false;
    }
}
