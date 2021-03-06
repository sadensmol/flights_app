package me.sadensmol.flights;

import static org.junit.Assert.assertTrue;

import me.sadensmol.flights.model.AppModel;
import me.sadensmol.flights.model.Ticket;
import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;

// according to the task - just minimum unit testing
public class AppTest
{
    @Test
    public void testModel()
    {
        Optional<Ticket> ticket1ById = AppModel.getInstance().findTicketById(10);
        Optional<Ticket> ticket2ById = AppModel.getInstance().findTicketById(4124123);

        assertTrue( ticket1ById.isPresent());
        assertTrue( !ticket2ById.isPresent());
    }

    @Test
    public void cacheTest() {

        long start1 = System.currentTimeMillis();
        Optional<Ticket> ticket1ById = AppModel.getInstance().findTicketById(100000);

        long start2 = System.currentTimeMillis();
        Optional<Ticket> ticket2ById = AppModel.getInstance().findTicketById(100000);

        long end = System.currentTimeMillis();

        long firstTime =start2 - start1;
        long secondTime =end - start2;

        Assert.assertTrue( firstTime> secondTime);
    }


    //todo add verticles test with vertx unit
}
