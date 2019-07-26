package me.sadensmol.flights.services.ticket;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import me.sadensmol.flights.model.AppModel;
import me.sadensmol.flights.model.BaggageToDest;
import me.sadensmol.flights.model.Coupon;

import java.util.Optional;
import java.util.Random;

//todo rework with interfaces in real vertx. services
public class CouponsService {
    private AppModel appModel = AppModel.getInstance();

    private static final Logger logger = LoggerFactory.getLogger(CouponsService.class);

    private int getDiscount () {
     int[] d = {10,20,50};

        int rnd = new Random().nextInt(4);
        return  d[rnd];
    }

    public boolean book(int couponId, double price) {
        Optional<Coupon> coupon = appModel.findCouponById(couponId);
        if (!coupon.isPresent()) {
            return false;
        }

        int discount = getDiscount();
        logger.info("coupon :" +coupon.get() + ",discount = " + discount);


        double finalPrice = (coupon.get().getPrice() * discount )/ 100.0;

        logger.info("Final price :" + finalPrice);

        if (price < finalPrice) return false;

        return true;
    }
}
