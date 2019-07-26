package me.sadensmol.flights.services.ticket;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import me.sadensmol.flights.model.AppModel;
import me.sadensmol.flights.model.Ticket;

import java.util.Optional;

public class TicketsService {
    private AppModel appModel = AppModel.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(TicketsService.class);

    public boolean book(int ticketId) {

        Optional<Ticket> ticket = appModel.findTicketById(ticketId);

        if (ticket.isPresent()) {
            return true;
        }

        return false;
    }
}
