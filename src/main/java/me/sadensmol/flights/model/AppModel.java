package me.sadensmol.flights.model;


//just hardcoded model

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import me.sadensmol.flights.model.cache.Cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppModel {

    private static AppModel instance;

    private Cache cache = new Cache();

    private List<Ticket> tickets;
    private List<Coupon> coupons;

    private List<BaggageToDest> baggageToDests;



    private static final Logger logger = LoggerFactory.getLogger(AppModel.class);

    public static AppModel getInstance() {
        if (instance == null)
            instance = new AppModel();

        return instance;
    }

    public AppModel () {
        createModel();
    }

    private void createModel() {
        tickets = new ArrayList<>();
        tickets.add(new Ticket(10));
        tickets.add(new Ticket(11));
        tickets.add(new Ticket(12));
        tickets.add(new Ticket(22));

        //put lots of ticket for cache testing

        for (int i = 100; i< 1000000; i++) {
            tickets.add(new Ticket(i));
        }

        coupons = new ArrayList<>();
        coupons.add(new Coupon(10, 666));
        coupons.add(new Coupon(11,2000));
        coupons.add(new Coupon(12, 1000));

        baggageToDests = new ArrayList<>();
        baggageToDests.add(new BaggageToDest(new Baggage(11), new Destination(12)));
        baggageToDests.add(new BaggageToDest(new Baggage(11), new Destination(12)));
        baggageToDests.add(new BaggageToDest(new Baggage(11), new Destination(12)));
    }

    // todo methods to access internal models are provided in the main app model
    // to make caching solution simpler
    // but they should be injected into the related services (via some Data Access layer -DAO etc...)
    // rework to good way of working with model

    //fixme - add functionality for auto caching with load check and maybe per object expiration time (to not cache rare requests)
    public Optional<Ticket> findTicketById(int ticketId) {
        //check if cache first
        Object obj =  cache.get("ticket" + ticketId);

        if (obj!= null)
            return (Optional<Ticket>) obj;

        logger.debug("Searching ticket by id " + ticketId);
        Optional<Ticket> foundTicket = tickets.stream().filter(it -> it.getId() == ticketId).findFirst();

        //put into the cache
        cache.add("ticket"+ticketId, foundTicket);

        return  foundTicket;
    }

    public Optional<BaggageToDest> findBaggageByIdAndDest(int baggageId, int destId) {
        return baggageToDests.stream().filter(btd -> btd.getDest().getId() == destId && btd.getBaggage().getId() == baggageId).findFirst();
    }

    public Optional<Coupon> findCouponById(int couponId) {
        return coupons.stream().filter(coupon -> coupon.getId() == couponId).findFirst();
    }
}
